package edu.umich.verdict.dbms;

import com.google.common.base.Joiner;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.datatypes.SampleParam;
import edu.umich.verdict.datatypes.SampleSizeInfo;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.ExactRelation;
import edu.umich.verdict.relation.Relation;
import edu.umich.verdict.relation.SingleRelation;
import edu.umich.verdict.relation.expr.Expr;
import edu.umich.verdict.util.StringManipulations;
import edu.umich.verdict.util.VerdictLogger;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dong Young Yoon on 4/10/18.
 */
public class DbmsH2 extends DbmsJDBC{

    public DbmsH2(VerdictContext vc, String dbName, String host, String port, String schema, String user,
                  String password, String jdbcClassName) throws VerdictException {
        super(vc, dbName, host, port, schema, user, password, jdbcClassName);
    }


    @Override
    public String getQuoteString() {
        return "`";
    }

    protected String modOfRand(int mod) {
        return String.format("random() %% %d", mod);
    }

    @Override
    public String modOfHash(String col, int mod) {
        return String.format("abs(cast(cast(hash('SHA256',rpad(cast(%s%s%s as varchar(255)),256,'0'),1000) as varbinary(4)) as int)) %% %d", getQuoteString(), col, getQuoteString(), mod);
    }

    @Override
    public ResultSet getDatabaseNamesInResultSet() throws VerdictException {
        return executeJdbcQuery("show schemas");
    }

    @Override
    public void insertEntry(TableUniqueName tableName, List<Object> values) throws VerdictException {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(String.format("insert into %s values ", tableName));
        sql.append("(");
        String with = "'";
        sql.append(Joiner.on(", ").join(StringManipulations.quoteString(values, with)));
        sql.append(")");
        executeUpdate(sql.toString());
    }

    @Override
    public ResultSet describeTableInResultSet(TableUniqueName tableUniqueName) throws VerdictException {
        if (!tableUniqueName.getSchemaName().equalsIgnoreCase("information_schema")) {
            String sql = String.format(
                    "SELECT COLUMN_NAME, TYPE_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'",
                    tableUniqueName.getSchemaName().toUpperCase(), // H2 stores them with upper case.
                    tableUniqueName.getTableName().toUpperCase());
            return executeJdbcQuery(sql);
        }
        return null;
    }

    @Override
    public void dropTable(TableUniqueName tableName, boolean check) throws VerdictException {
        Set<String> databases = vc.getMeta().getDatabases();

        // TODO: this is buggy when the database created while a query is executed.
        // it can happen during sample creations.
        // We force them to be a lowercase.
        if (check && !databases.contains(tableName.getSchemaName().toLowerCase())) {
            VerdictLogger.debug(this,
                    String.format(
                            "Database, %s, does not exists. Verdict doesn't bother to run a drop table statement.",
                            tableName.getSchemaName()));
            return;
        }

        // This check is useful for Spark 1.6, since it throws an error even though "if exists" is used
        // in the "drop table" statement.
        // We force them to be a lowercase.
        Set<String> tables = vc.getMeta().getTables(tableName.getDatabaseName());
        if (check && !tables.contains(tableName.getTableName().toLowerCase())) {
            VerdictLogger.debug(this, String.format(
                    "Table, %s, does not exists. Verdict doesn't bother to run a drop table statement.", tableName));
            return;
        }

        String sql = String.format("DROP TABLE IF EXISTS %s", tableName);
        VerdictLogger.debug(this, String.format("Drops table: %s", sql));
        executeUpdate(sql);
        vc.getMeta().refreshTables(tableName.getDatabaseName());
        VerdictLogger.debug(this, tableName + " has been dropped.");
    }

    @Override
    public void createMetaTablesInDBMS(TableUniqueName originalTableName, TableUniqueName sizeTableName, TableUniqueName nameTableName) throws VerdictException {
        VerdictLogger.debug(this, "Creates meta tables if not exist.");
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s", sizeTableName) + " (schemaname VARCHAR, "
                + " tablename VARCHAR, " + " samplesize BIGINT, " + " originaltablesize BIGINT)";
        executeUpdate(sql);

        sql = String.format("CREATE TABLE IF NOT EXISTS %s", nameTableName) + " (originalschemaname VARCHAR, "
                + " originaltablename VARCHAR, " + " sampleschemaaname VARCHAR, " + " sampletablename VARCHAR, "
                + " sampletype VARCHAR, " + " samplingratio DOUBLE, " + " columnnames VARCHAR)";
        executeUpdate(sql);

        VerdictLogger.debug(this, "Meta tables created.");
        vc.getMeta().refreshTables(sizeTableName.getDatabaseName());
    }

    @Override
    public String modOfHash(List<String> columns, int mod) {
        String concatStr = "";
        for (int i = 0; i < columns.size(); ++i) {
            String col = columns.get(i);
            String castStr = String.format("rpad(cast(rawtohex(%s%s%s) as varchar(255)),256,'0')", getQuoteString(), col, getQuoteString());
            if (i < columns.size() - 1) {
                castStr += ",";
            }
            concatStr += castStr;
        }
        return String.format("abs(cast(cast(hash('SHA256',concat_ws('%s', %s),1000) as varbinary(4)) as int)) %% %d", HASH_DELIM, concatStr, mod);
    }

    @Override
    public ResultSet getTablesInResultSet(String schema) throws VerdictException {
        return executeJdbcQuery("show tables from " + schema);
    }

    @Override
    protected String randomNumberExpression(SampleParam param) {
        String expr = "random()";
        return expr;
    }

    @Override
    protected String randomPartitionColumn() {
        int pcount = partitionCount();
        return String.format("mod(round(random()*%d), %d) AS %s", pcount, pcount, partitionColumnName());
    }

    @Override
    protected void createStratifiedSampleFromGroupSizeTemp(SampleParam param, TableUniqueName groupSizeTemp)
            throws VerdictException {
        Map<String, String> col2types = vc.getMeta().getColumn2Types(param.getOriginalTable());
        SampleSizeInfo info = vc.getMeta()
                .getSampleSizeOf(new SampleParam(vc, param.getOriginalTable(), "uniform", null, new ArrayList<String>()));
        long originalTableSize = info.originalTableSize;
        long groupCount = SingleRelation.from(vc, groupSizeTemp).countValue();
        String samplingProbColName = vc.getDbms().samplingProbabilityColumnName();

        // equijoin expression that considers possible null values
        List<Pair<Expr, Expr>> joinExprs = new ArrayList<Pair<Expr, Expr>>();
        for (String col : param.getColumnNames()) {
            boolean isString = false;
            boolean isTimeStamp = false;

            if (col2types.containsKey(col)) {
                if (col2types.get(col).toLowerCase().contains("char")
                        || col2types.get(col).toLowerCase().contains("str")) {
                    isString = true;
                } else if (col2types.get(col).toLowerCase().contains("time")) {
                    isTimeStamp = true;
                }
            }

            if (isString) {
                Expr left = Expr.from(vc,
                        String.format("case when s.%s%s%s is null then '%s' else s.%s%s%s end",
                                getQuoteString(), col, getQuoteString(),
                                NULL_STRING,
                                getQuoteString(), col, getQuoteString()));
                Expr right = Expr.from(vc,
                        String.format("case when t.%s%s%s is null then '%s' else t.%s%s%s end",
                                getQuoteString(), col, getQuoteString(),
                                NULL_STRING,
                                getQuoteString(), col, getQuoteString()));
                joinExprs.add(Pair.of(left, right));
            } else if (isTimeStamp) {
                Expr left = Expr.from(vc,
                        String.format("case when s.%s%s%s is null then '%s' else s.%s%s%s end",
                                getQuoteString(), col, getQuoteString(),
                                NULL_TIMESTAMP,
                                getQuoteString(), col, getQuoteString()));
                Expr right = Expr.from(vc,
                        String.format("case when t.%s%s%s is null then '%s' else t.%s%s%s end",
                                getQuoteString(), col, getQuoteString(),
                                NULL_TIMESTAMP,
                                getQuoteString(), col, getQuoteString()));
                joinExprs.add(Pair.of(left, right));
            } else {
                Expr left = Expr.from(vc,
                        String.format("case when s.%s%s%s is null then %d else s.%s%s%s end",
                                getQuoteString(), col, getQuoteString(),
                                NULL_LONG,
                                getQuoteString(), col, getQuoteString()));
                Expr right = Expr.from(vc,
                        String.format("case when t.%s%s%s is null then %d else t.%s%s%s end",
                                getQuoteString(), col, getQuoteString(),
                                NULL_LONG,
                                getQuoteString(), col, getQuoteString()));
                joinExprs.add(Pair.of(left, right));
            }
        }

        // where clause using rand function
        String whereClause = String.format("%s < %d * %f / %d / %s", randNumColname, originalTableSize,
                param.getSamplingRatio(), groupCount, groupSizeColName);

        // this should set to an appropriate variable.
        List<Pair<Integer, Double>> samplingProbForSize = vc.getConf().samplingProbabilitiesForStratifiedSamples();

        whereClause += String.format(" OR %s < (case", randNumColname);

        for (Pair<Integer, Double> sizeProb : samplingProbForSize) {
            int size = sizeProb.getKey();
            double prob = sizeProb.getValue();
            whereClause += String.format(" when %s >= %d then %f * %d / %s", groupSizeColName, size, prob, size,
                    groupSizeColName);
        }
        whereClause += " else 1.0 end)";

        // aliased select list
        List<String> selectElems = new ArrayList<String>();
        for (String col : col2types.keySet()) {
            selectElems.add(String.format("s.%s%s%s", getQuoteString(), col, getQuoteString()));
        }

        // sample table
        TableUniqueName sampledNoRand = Relation.getTempTableName(vc, param.sampleTableName().getSchemaName());
        ExactRelation sampled = SingleRelation.from(vc, param.getOriginalTable())
                .select(String.format("*, %s as %s", randomNumberExpression(param), randNumColname)).withAlias("s")
                .join(SingleRelation.from(vc, groupSizeTemp).withAlias("t"), joinExprs).where(whereClause)
                .select(Joiner.on(", ").join(selectElems) + ", " + groupSizeColName);
        String sql1 = String.format("create table %s as %s", sampledNoRand, sampled.toSql());
//        VerdictLogger.debug(this, "The query used for creating a stratified sample without sampling probabilities.");
//        VerdictLogger.debugPretty(this, Relation.prettyfySql(vc, sql1), "  ");
        executeUpdate(sql1);

        // attach sampling probabilities and random partition number
        ExactRelation sampledGroupSize = SingleRelation.from(vc, sampledNoRand).groupby(param.getColumnNames())
                .agg("count(*) AS " + groupSizeInSampleColName);
        ExactRelation withRand = SingleRelation.from(vc, sampledNoRand).withAlias("s")
                .join(sampledGroupSize.withAlias("t"), joinExprs)
                .select(Joiner.on(", ").join(selectElems) + String.format(", %s  / %s as %s", groupSizeInSampleColName,
                        groupSizeColName, samplingProbColName) + ", " + randomPartitionColumn());

        String storeString = "";

        if (vc.getConf().areSamplesStoredAsParquet()) {
            storeString = getParquetString();
        }
        if (vc.getConf().areHiveSampleStoredAsOrc()) {
            storeString = getORCString();
        }
        // cast verdict_group_size and verdict_group_size_in_sample into Double type
        String withRandSql = withRand.toSql();
        withRandSql = withRandSql.replace("(`verdict_group_size_in_sample` / `verdict_group_size`)",
                "(CAST(`verdict_group_size_in_sample` AS DOUBLE) / CAST(`verdict_group_size` AS DOUBLE))");
        String sql2 = String.format("create table %s%s as %s", param.sampleTableName(), storeString,
                withRandSql);
        VerdictLogger.debug(this, "The query used for creating a stratified sample with sampling probabilities.");
//        VerdictLogger.debugPretty(this, Relation.prettyfySql(vc, sql2), "  ");
//        VerdictLogger.debug(this, sql2);
        executeUpdate(sql2);

        dropTable(sampledNoRand, false);
    }

    @Override
    protected long createUniverseSampleWithProbFromSample(SampleParam param, TableUniqueName temp)
            throws VerdictException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        boolean f = false;
        if (param.getColumnNames().get(0).equals("c_name")){
            System.out.println("here");
        }
        String samplingProbCol = vc.getDbms().samplingProbabilityColumnName();
        ExactRelation sampled = SingleRelation.from(vc, temp);
        long total_size = vc.getMeta().getTableSize(param.getOriginalTable());
        long sample_size = vc.getMeta().getTableSize(temp);

        ExactRelation withProb = sampled
                .select(String.format("*, %f / %f AS %s", (double)sample_size, (double)total_size, samplingProbCol) + ", "
                        + universePartitionColumn(param.getColumnNames()));

        String storeString = "";
        if (vc.getConf().areSamplesStoredAsParquet()) {
            storeString = getParquetString();
        }
        if (vc.getConf().areHiveSampleStoredAsOrc()) {
            storeString = getORCString();
        }

        String sql = String.format("create table %s%s AS %s", param.sampleTableName(), storeString, withProb.toSql());
        VerdictLogger.debug(this, "The query used for creating a universe sample with sampling probability:");
//        VerdictLogger.debugPretty(this, Relation.prettyfySql(vc, sql), "  ");
//        VerdictLogger.debug(this, sql);
        executeUpdate(sql);
        return sample_size;
    }
}
