package edu.umich.verdict;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class CreateSampleTest extends VerdictTestBase {
    private final static long CUSTOMER_ROW_COUNT = 7500;

    @BeforeClass
    static public void dropSample() throws VerdictException, SQLException {
        vc.executeJdbcQuery("drop samples of customer");
        vc.executeJdbcQuery("create 20% uniform sample of customer");
    }

    @Test
    public void createUniformSampleTest() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String tableName = "customer";
        String typeShortName = "uf";
        Double samplingRatio = 0.2;
        vc.executeJdbcQuery("create 20% uniform sample of customer");
        String sampleTableName = String.format("test_verdict.vs_%s_%s_%s", tableName, typeShortName,
                String.format("%.4f", samplingRatio).replace('.', '_'));
        ResultSet rs = stmt.executeQuery(String.format("select count(*) from %s", sampleTableName));
        if (rs.next()){
            long rowCount = rs.getLong(1);
            assertEquals(true, (double)CUSTOMER_ROW_COUNT*samplingRatio*0.95<rowCount && (double)CUSTOMER_ROW_COUNT*samplingRatio*1.05>rowCount);
        }
        rs.close();
        stmt.close();
    }

    @Test
    public void CreateStratifiedSampleTest() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String tableName = "customer";
        String typeShortName = "st";
        Double samplingRatio = 0.2;
        String colName = "c_nationkey";
        vc.executeJdbcQuery("create 20% stratified sample of customer on c_nationkey");
        String sampleTableName = String.format("test_verdict.vs_%s_%s_%s", tableName, typeShortName,
                String.format("%.4f", samplingRatio).replace('.', '_')+'_'+colName);
        ResultSet rs = stmt.executeQuery(String.format("select count(*) from %s", sampleTableName));
        if (rs.next()){
            long rowCount = rs.getLong(1);
            assertEquals(true, (double)CUSTOMER_ROW_COUNT*samplingRatio*0.95<rowCount && (double)CUSTOMER_ROW_COUNT*samplingRatio*1.05>rowCount);
        }
        long original_cardinality = 0, sample_cardinality = 0;
        rs = stmt.executeQuery(String.format("select count(*) from (select distinct c_nationkey from customer)"));
        if (rs.next()){
            original_cardinality = rs.getLong(1);
        }
        rs = stmt.executeQuery(String.format("select count(*) from (select distinct c_nationkey from %s)", sampleTableName));
        if (rs.next()){
            sample_cardinality = rs.getLong(1);
        }
        assertEquals(original_cardinality, sample_cardinality);
        rs.close();
        stmt.close();
    }

    @Test
    public void CreateUniverseSampleTest() throws  VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String tableName = "customer";
        String typeShortName = "uv";
        Double samplingRatio = 0.2;
        String colName = "c_nationkey";
        vc.executeJdbcQuery("create 20% universe sample of customer on c_nationkey");
        String sampleTableName = String.format("test_verdict.vs_%s_%s_%s", tableName, typeShortName,
                String.format("%.4f", samplingRatio).replace('.', '_')+'_'+colName);
        ResultSet rs = stmt.executeQuery(String.format("select count(*) from %s", sampleTableName));
        if (rs.next()){
            long rowCount = rs.getLong(1);
           // assertEquals(true, (double)CUSTOMER_ROW_COUNT*samplingRatio*0.95<rowCount && (double)CUSTOMER_ROW_COUNT*samplingRatio*1.05>rowCount);
        }
        long original_cardinality = 0, sample_cardinality = 0;
        rs = stmt.executeQuery(String.format("select count(*) from (select distinct c_nationkey from customer)"));
        if (rs.next()){
            original_cardinality = rs.getLong(1);
        }
        rs = stmt.executeQuery(String.format("select count(*) from (select distinct c_nationkey from %s)", sampleTableName));
        if (rs.next()){
            sample_cardinality = rs.getLong(1);
        }
        assertEquals(true, original_cardinality*0.8 < sample_cardinality && original_cardinality*1.2 > sample_cardinality);
        rs.close();
        stmt.close();
    }

}
