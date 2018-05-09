package edu.umich.verdict;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ApproxQueryTest extends VerdictTestBase {
    private final static long CUSTOMER_ROW_COUNT = 7500;
    private final static String tableName = "customer";
    private final static String typeShortName = "st";
    private final static Double samplingRatio = 0.2;
    private final static String sampleTableName = String.format("test_verdict.vs_%s_%s_%s", tableName, typeShortName,
            String.format("%.4f", samplingRatio).replace('.', '_'));

    @BeforeClass
    public static void createUniformSample() throws VerdictException, SQLException {
        vc.executeJdbcQuery("create 20% stratified sample of customer on c_nationkey");
    }

    @Test
    public void AggregationQueryTest1() throws VerdictException, SQLException {
        vc.executeJdbcQuery("select count(*) from customer");
        ResultSet rs = vc.getResultSet();
        if (rs.next()){
           long rowCount = rs.getLong(1);
           System.out.println(rowCount);
           assertEquals(CUSTOMER_ROW_COUNT, rowCount);
        }
    }

    @Test
    public void AggregationQueryTest2() throws  VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long original_res = 0, sample_res = 0;
        vc.executeJdbcQuery("select count(*) from customer where c_nationkey=1");
        ResultSet rs = vc.getResultSet();
        if (rs.next()){
            sample_res = rs.getLong(1);
        }
        rs = stmt.executeQuery("select count(*) from customer where c_nationkey=1");
        if (rs.next()){
            original_res = rs.getLong(1);
        }
        assertEquals(true, 0.9*(double)original_res<(double)sample_res && (double)sample_res < 1.1*(double)original_res);
    }

    @Test
    public void AggregateQueryTest3() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        vc.executeJdbcQuery("select c_nationkey, count(*) as cnt from customer where c_nationkey > 5 group by c_nationkey");
        ResultSet rs = vc.getResultSet();
        if (rs.next()){
            sample_res.add(rs.getLong(1));
        }
        rs = stmt.executeQuery("select c_nationkey, count(*) as cnt from customer where c_nationkey > 5 group by c_nationkey");
        if (rs.next()){
           original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    0.9*(long)original_res.get(i)<(long)sample_res.get(i) && (long)sample_res.get(i) < 1.1*(long)original_res.get(i));
        }

    }
}
