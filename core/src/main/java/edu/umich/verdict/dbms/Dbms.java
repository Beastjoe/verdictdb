package edu.umich.verdict.dbms;

import edu.umich.verdict.datatypes.SampleParam;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.datatypes.VerdictResultSet;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.util.StackTraceReader;
import edu.umich.verdict.util.VerdictLogger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

/**
 * This class is responsible for choosing a right DBMS class.
 */
public class Dbms {
	
	protected final Connection conn;
	protected final String dbName;
	protected Optional<String> currentSchema;
	protected VerdictContext vc;
	private Statement stmt;		// created Statements must be registered here.
	
	/**
	 * Copy constructor for not sharing the underlying statement.
	 * @param another
	 */
	public Dbms(Dbms another) {
		conn = another.conn;
		dbName = another.dbName;
		currentSchema = another.currentSchema;
		vc = another.vc;
		stmt = null;
		VerdictLogger.debug(this, "A new dbms connection with schema: " + currentSchema);
	}
	
	protected Dbms(VerdictContext vc,
			    String dbName,
			    String host,
			    String port,
			    String schema,
			    String user,
			    String password,
			    String jdbcClassName)
			throws VerdictException {
		this.vc = vc;
		this.dbName = dbName;
		currentSchema = Optional.fromNullable(schema);
		String url = composeUrl(dbName,
							   host,
							   port,
							   schema,
							   user,
							   password);
		conn = makeDbmsConnection(url, jdbcClassName);
	}
	
	public static Dbms getInstance(VerdictContext vc,
			String dbName,
			String host,
			String port,
			String schema,
			String user,
			String password,
			String jdbcClassName) throws VerdictException {
		Dbms dbms = null;
		if (dbName.equals("mysql")) {
			dbms = new DbmsMySQL(vc, dbName, host, port, schema, user, password, jdbcClassName);
		} else if (dbName.equals("impala")) {
			dbms = new DbmsImpala(vc, dbName, host, port, schema, user, password, jdbcClassName);
		} else if (dbName.equals("hive") || dbName.equals("hive2")) {
			dbms = new DbmsHive(vc, dbName, host, port, schema, user, password, jdbcClassName);
		} else if (dbName.equals("dummy")) {
			dbms = new DbmsDummy(vc);
		} else {
			String msg = String.format("Unsupported DBMS: %s", dbName);
			VerdictLogger.error("Dbms", msg);
			throw new VerdictException(msg);
		}
		return dbms;
	}
	
	public Statement createStatement() throws VerdictException {
		try {
			if (stmt != null) closeStatement();
			stmt = conn.createStatement();
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
		return stmt;
	}
	
	public Statement createNewStatementWithoutClosing() throws VerdictException {
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
		return stmt;
	}
	
	public Statement createStatementIfNotExists() throws VerdictException {
		if (stmt == null) createStatement();
		return stmt;
	}
	
	public void closeStatement() throws VerdictException {
		try {
			if (stmt != null) stmt.close();
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	protected String composeUrl(String dbms, String host, String port, String schema, String user, String password) throws VerdictException {
		StringBuilder url = new StringBuilder();
		url.append(String.format("jdbc:%s://%s:%s", dbms, host, port));
		
		if (schema != null) {
			url.append(String.format("/%s", schema));
		}

		boolean isFirstParam = true;
		if (user != null && user.length() != 0) {
			url.append((isFirstParam)? "?" : "&");
			url.append(String.format("user=%s", user));
			isFirstParam = false;
		}
		if (password != null && password.length() != 0) {
			url.append((isFirstParam)? "?" : "&");
			url.append(String.format("password=%s", password));
			isFirstParam = false;
		}
		
		if (vc.getConf().doesContain("principal")) {
			String principal = vc.getConf().get("principal");
			
			Pattern princPattern = Pattern.compile("(?<service>.*)/(?<host>.*)@(?<realm>.*)");
			
			Matcher princMatcher = princPattern.matcher(principal);
			
			if (princMatcher.find()) {
				String service = princMatcher.group("service");
				String krbRealm = princMatcher.group("realm");
				String krbHost = princMatcher.group("host");
				
				url.append(String.format(";AuthMech=%s;KrbRealm=%s;KrbHostFQDN=%s;KrbServiceName=%s;KrbAuthType=%s",
						 "1", krbRealm, krbHost, service, "2"));
			} else {
				VerdictLogger.error("Error: principal \"" + principal + "\" could not be parsed.\n"
						+ "Make sure the principal is in the form service/host@REALM");
			}		
		}
		
		return url.toString();
	}

	public String getName() {
		return dbName;
	}
	
	public Optional<String> getCurrentSchema() {
		return currentSchema;
	}
	
	public boolean execute(String query) throws VerdictException {
		createStatementIfNotExists();
		try {
			return stmt.execute(query);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public ResultSet executeQuery(String query) throws VerdictException {
		createStatementIfNotExists();
		ResultSet rs;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
		return rs;
	}
	
	public void executeUpdate(String query) throws VerdictException { 
		createStatementIfNotExists();
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	protected Connection makeDbmsConnection(String url, String className) throws VerdictException  {
		try {
			Class.forName(className);
			VerdictLogger.debug(this, "JDBC connection string: " + url);
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (ClassNotFoundException | SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	/**
	 * changes to another database (or equivalently, schema). This is conceptually equal to the use statement in MySQL.
	 * @throws VerdictException
	 */
	public void changeDatabase(String schemaName) throws VerdictException {
		try {
			conn.setCatalog(schemaName);
			currentSchema = Optional.fromNullable(schemaName);
			VerdictLogger.info("Database changed to: " + schemaName);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public ResultSet showTables() throws VerdictException {
		try {
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			return rs;
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public void dropTable(TableUniqueName tableName) throws VerdictException {
		String sql = String.format("DROP TABLE IF EXISTS %s", tableName);
		VerdictLogger.debug(this, String.format("Drop table: %s", sql));
		this.executeUpdate(sql);
	}
	
	protected void moveTable(TableUniqueName from, TableUniqueName to) throws VerdictException {
		String sql = String.format("CREATE TABLE %s AS SELECT * FROM %s", to, from);
		VerdictLogger.debug(this, String.format("Move %s to %s", from, to));
		executeUpdate(sql);
	}

	
	/**
	 * Creates a sample table without dropping an old table.
	 * @param originalTableName
	 * @param sampleRatio
	 * @throws VerdictException
	 */
	protected TableUniqueName justCreateUniformRandomSampleTableOf(SampleParam param) throws VerdictException {
		TableUniqueName sampleTableName = param.sampleTableName();
		String sql = String.format("CREATE TABLE %s SELECT * FROM %s WHERE rand() < %f;",
										sampleTableName, param.originalTable, param.samplingRatio);
		VerdictLogger.debug(this, String.format("Create a table: %s", sql));
		this.executeUpdate(sql);
		return sampleTableName;
	}
	
	public long getTableSize(TableUniqueName tableName) throws VerdictException {
		ResultSet rs;
		long cnt = 0;
		try {
			String sql = String.format("SELECT COUNT(*) FROM %s", tableName);
			rs = this.executeQuery(sql);
			while(rs.next()) {cnt = rs.getLong(1);	}
			rs.close();
		} catch (SQLException e) {
			throw new VerdictException(StackTraceReader.stackTrace2String(e));
		}
		return cnt;
	}

	public Pair<Long, Long> createUniformRandomSampleTableOf(SampleParam param) throws VerdictException {
		dropTable(param.sampleTableName());
		justCreateUniformRandomSampleTableOf(param);
		return Pair.of(getTableSize(param.sampleTableName()), getTableSize(param.originalTable));
	}
	
	/**
	 * Creates a universe sample table without dropping an old table.
	 * @param originalTableName
	 * @param sampleRatio
	 * @throws VerdictException
	 */
	protected TableUniqueName justCreateUniverseSampleTableOf(SampleParam param) throws VerdictException {
		TableUniqueName sampleTableName = param.sampleTableName();
		String sql = String.format("CREATE TABLE %s SELECT * FROM %s "
								 + "WHERE mod(cast(conv(substr(md5(%s),17,32),16,10) as unsigned), 10000) <= %.4f",
								 sampleTableName, param.originalTable, param.columnNames.get(0), param.samplingRatio*10000);
		VerdictLogger.debug(this, String.format("Create a table: %s", sql));
		this.executeUpdate(sql);
		return sampleTableName;
	}
	
	public Pair<Long, Long> createUniverseSampleTableOf(SampleParam param) throws VerdictException {
		dropTable(param.sampleTableName());
		justCreateUniverseSampleTableOf(param);
		return Pair.of(getTableSize(param.sampleTableName()), getTableSize(param.originalTable));
	}
	
	public Pair<Long, Long> createStratifiedSampleTableOf(SampleParam param) throws VerdictException {
		dropTable(param.sampleTableName());
		justCreateStratifiedSampleTableof(param);
		return Pair.of(getTableSize(param.sampleTableName()), getTableSize(param.originalTable));
	}
	
	protected void justCreateStratifiedSampleTableof(SampleParam param) throws VerdictException {
		throw new VerdictException("unimplemented");
	}

	public Connection getDbmsConnection() {
		return conn;
	}

	public void close() throws VerdictException {
		try {
			closeStatement();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	protected String columnNameListToString(List<String> columnNames) {
		StringBuilder b = new StringBuilder();
		boolean isFirst = true;
		for (String c : columnNames) {
			if (isFirst) b.append(c);
			else b.append(","+c);
			isFirst = false;
		}
		return b.toString();
	}
	
	protected String samplingRatioToString(double samplingRatio) {
		return String.format("%.4f", samplingRatio);
	}
	
	public void updateSampleNameEntryIntoDBMS(SampleParam param, TableUniqueName metaNameTableName) throws VerdictException {
		deleteSampleNameEntryFromDBMS(param, metaNameTableName);
		insertSampleNameEntryIntoDBMS(param, metaNameTableName);
	}
	
	public void deleteSampleNameEntryFromDBMS(SampleParam param, TableUniqueName metaNameTableName)
			throws VerdictException {
		TableUniqueName originalTableName = param.originalTable;
		String sql = String.format("DELETE FROM %s WHERE originalschemaname = \"%s\" AND originaltablename = \"%s\" "
				+ "AND sampletype = \"%s\" AND samplingratio = %s AND columnnames = \"%s\" ",
				metaNameTableName, originalTableName.schemaName, originalTableName.tableName,
				param.sampleType, samplingRatioToString(param.samplingRatio), columnNameListToString(param.columnNames));
		executeUpdate(sql);
	}
	
	protected void insertSampleNameEntryIntoDBMS(SampleParam param, TableUniqueName metaNameTableName) throws VerdictException {
		TableUniqueName originalTableName = param.originalTable;
		TableUniqueName sampleTableName = param.sampleTableName();
		String sql = String.format("INSERT INTO %s VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %s, \"%s\")", metaNameTableName,
				originalTableName.schemaName, originalTableName.tableName, sampleTableName.schemaName, sampleTableName.tableName,
				param.sampleType, samplingRatioToString(param.samplingRatio), columnNameListToString(param.columnNames));
		executeUpdate(sql);
	}
	
	public void updateSampleSizeEntryIntoDBMS(SampleParam param, long sampleSize, long originalTableSize, TableUniqueName metaSizeTableName) throws VerdictException {
		deleteSampleSizeEntryFromDBMS(param, metaSizeTableName);
		insertSampleSizeEntryIntoDBMS(param, sampleSize, originalTableSize, metaSizeTableName);
	}
	
	public void deleteSampleSizeEntryFromDBMS(SampleParam param, TableUniqueName metaSizeTableName) throws VerdictException {
		TableUniqueName sampleTableName = param.sampleTableName();
		String sql = String.format("DELETE FROM %s WHERE schemaname = \"%s\" AND tablename = \"%s\" ",
				metaSizeTableName, sampleTableName.schemaName, sampleTableName.tableName);
		executeUpdate(sql);
	}
	
	protected void insertSampleSizeEntryIntoDBMS(SampleParam param,	long sampleSize, long originalTableSize, TableUniqueName metaSizeTableName) throws VerdictException {
		TableUniqueName sampleTableName = param.sampleTableName();
		String sql = String.format("INSERT INTO %s VALUES (\"%s\", \"%s\", %d, %d)",
				metaSizeTableName, sampleTableName.schemaName, sampleTableName.tableName, sampleSize, originalTableSize);
		executeUpdate(sql);
	}
	
	/**
	 * This method does not guarantee fast deletion (especially in Impala or Hive)
	 * @param tableName
	 * @param condition
	 * @throws VerdictException 
	 */
	public void deleteRowsIn(String tableName, String condition) throws VerdictException {
		String sql = String.format("DELETE FROM %s WHERE %s", tableName, condition);
		this.executeUpdate(sql);
	}
	
	/**
	 * This method does not guarantee fast insertion (especially in Impala or Hive)
	 * @param tableName
	 * @param condition
	 * @throws VerdictException 
	 */
	public void insertRowsIn(String tableName, String values) throws VerdictException {
		String sql = String.format("INSERT INTO %s VALUES (%s)", tableName, values);
		this.executeUpdate(sql);
	}
	
	/**
	 * This method is not thread-safe
	 * @return
	 */
	public TableUniqueName generateTempTableName() {
		String tableName = String.format("verdict_temp_table_%d", System.nanoTime());
		return TableUniqueName.uname(vc, tableName);
	}
	
	public ResultSet getDatabaseNames() throws VerdictException {
		try {
			return conn.getMetaData().getCatalogs();
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public ResultSet getAllTableAndColumns(String schemaName) throws VerdictException {
		try {
			ResultSet rs = conn.getMetaData().getColumns(schemaName, null, "%", "%");
			Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();
			columnMap.put(1, 3);	// table name
			columnMap.put(2, 4);	// column name
			return new VerdictResultSet(rs, null, columnMap);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public ResultSet getTableNames(String schemaName) throws VerdictException {
		try {
			String[] types = {"TABLE", "VIEW"};
			ResultSet rs = conn.getMetaData().getTables(schemaName, null, "%", types);
			Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();
			columnMap.put(1, 3);	// table name
			columnMap.put(2, 4);	// table type
			return new VerdictResultSet(rs, null, columnMap);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public ResultSet describeTable(TableUniqueName tableUniqueName)  throws VerdictException {
		try {
			ResultSet rs = conn.getMetaData().getColumns(
					tableUniqueName.schemaName, null, tableUniqueName.tableName, "%");
			Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();
			columnMap.put(1, 4);	// column name
			columnMap.put(2, 6); 	// data type name
			columnMap.put(3, 12); 	// remarks
			return new VerdictResultSet(rs, null, columnMap);
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public void createMetaTablesInDMBS(
			TableUniqueName originalTableName,
			TableUniqueName sizeTableName,
			TableUniqueName nameTableName) throws VerdictException {
		VerdictLogger.debug(this, "Creating meta tables if not exist.");
		
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s", nameTableName)
				+ " (originalschemaname VARCHAR(50), "
				+ " originaltablename VARCHAR(50), "
				+ " sampleschemaaname VARCHAR(50), "
				+ " sampletablename VARCHAR(50), "
				+ " sampletype VARCHAR(20), "
				+ " samplingratio DOUBLE, "
				+ " columnnames VARCHAR(200))";
		executeUpdate(sql);
		
		// sample info
		sql = String.format("CREATE TABLE IF NOT EXISTS %s", sizeTableName)
				+ " (schemaname VARCHAR(50), "
				+ " tablename VARCHAR(50), "
				+ " samplesize BIGINT, "
				+ " originaltablesize BIGINT)";
		executeUpdate(sql);
	}
	
	public boolean doesMetaTablesExist(String schemaName) throws VerdictException {
		String[] types = {"TABLE"};
		try {
			ResultSet rs = vc.getDbms().getDbmsConnection().getMetaData().getTables(
					null, schemaName, vc.getMeta().getMetaNameTableName(currentSchema.get()).tableName, types);
			if (!rs.next()) return false;
			else return true;
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
	}
	
	public String getQuoteString() {
		return "\"";
	}

	public String varianceFunction() {
		return "VAR_SAMP";
	}
	
	public String stddevFunction() {
		return "STDDEV";
	}

}
