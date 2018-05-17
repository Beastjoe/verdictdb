package edu.umich.verdict;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApproxQueryTest extends VerdictTestBase {
    private final static long CUSTOMER_ROW_COUNT = 7500;
    private final static String tableName = "customer";
    private final static String typeShortName = "uf";
    private final static Double samplingRatio = 0.2;
    private final static String sampleTableName = String.format("test_verdict.vs_%s_%s_%s", tableName, typeShortName,
            String.format("%.4f", samplingRatio).replace('.', '_'));

    @BeforeClass
    public static void createUniformSample() throws VerdictException, SQLException {
        vc.executeJdbcQuery("drop samples of customer");
        vc.executeJdbcQuery("drop samples of supplier");
        vc.executeJdbcQuery("drop samples of nation");
        //vc.executeJdbcQuery("create 20% uniform sample of customer");
        vc.executeJdbcQuery("create 20% stratified sample of customer on c_nationkey");
        // vc.executeJdbcQuery("create 20% sample of supplier");

        vc.executeJdbcQuery("create 20% stratified sample of nation on n_nationkey");
        //vc.executeJdbcQuery("create 20% stratified sample of customer on c_nationkey");
        //vc.executeJdbcQuery("create 20% universe sample of customer on c_nationkey");
        //vc.executeJdbcQuery("create 20% universe sample of customer on c_nationkey");
        //vc.executeJdbcQuery("create 20% uniform sample of supplier");
        vc.executeJdbcQuery("create 20% stratified sample of supplier on s_nationkey");
        //vc.executeJdbcQuery("create 20% universe sample of supplier on s_nationkey");
    }

    /*
    All test have confidence of 95% to pass.
    */

    // @Test
    public void CountTest() throws VerdictException, SQLException {
        vc.executeJdbcQuery("select count(*) from customer");
        ResultSet rs = vc.getResultSet();
        if (rs.next()){
           long rowCount = rs.getLong(1);
           System.out.println(rowCount);
           assertEquals(CUSTOMER_ROW_COUNT, rowCount);
        }
    }

    //@Test //BUG
    public void CountDistinctTest1() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        vc.executeJdbcQuery("select count(distinct c_nationkey) from customer");
        ResultSet rs = vc.getResultSet();
        long sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getLong(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select count(distinct c_nationkey) from customer");
        if (rs.next()){
            double count = rs.getLong(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }

    //@Test//BUG
    public void CountDistinctTest2() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        vc.executeJdbcQuery("select count(*) from (select DISTINCT c_nationkey from customer)");
        ResultSet rs = vc.getResultSet();
        long sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getLong(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select count(*) from (select DISTINCT c_nationkey from customer)");
        if (rs.next()){
            double count = rs.getLong(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }

    // @Test
    public void SumTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        vc.executeJdbcQuery("select sum(c_acctbal) from customer");
        ResultSet rs = vc.getResultSet();
        double sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getDouble(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select sum(c_acctbal) from customer");
        if (rs.next()){
            double count = rs.getLong(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }


    // @Test
    public void AvgTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        vc.executeJdbcQuery("select avg(c_acctbal) from customer");
        ResultSet rs = vc.getResultSet();
        double sampleAvg = 0;
        double err = 0;
        if (rs.next()){
            sampleAvg = rs.getDouble(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select avg(c_acctbal) from customer");
        if (rs.next()){
            double avg = rs.getDouble(1);
            assertEquals(true, sampleAvg-err<=avg && sampleAvg+err>=avg);
        }
    }

    // @Test //When using universe sample, it is likeyly to have no c_nationkey = 1
    public void FilterTest1() throws  VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long original_res = 0, sample_res = 0;
        vc.executeJdbcQuery("select count(*) from customer where c_nationkey=1");
        ResultSet rs = vc.getResultSet();
        Double err = 0.0;
        if (rs.next()){
            sample_res = rs.getLong(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select count(*) from customer where c_nationkey=1");
        if (rs.next()){
            original_res = rs.getLong(1);
        }
        assertEquals(true, original_res-err<=sample_res && sample_res <= original_res+err);
    }

    // @Test
    public void FilterTest2() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        vc.executeJdbcQuery("select c_nationkey, count(*)  from customer where c_nationkey > 5 group by c_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select c_nationkey, count(*) from customer where c_nationkey > 5 group by c_nationkey");
        while (rs.next()){
            original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i) &&
                            (long)sample_res.get(i) < (double)err.get(i)+(long)original_res.get(i));
        }
    }

    // @Test
    public void FilterTest3() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        vc.executeJdbcQuery("select c_nationkey, count(*)  from customer where c_nationkey > 5 and c_acctbal > 100 group by c_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select c_nationkey, count(*)  from customer where c_nationkey > 5 and c_acctbal > 100 group by c_nationkey");
        while (rs.next()){
            original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i) &&
                            (long)sample_res.get(i) < (double)err.get(i)+(long)original_res.get(i));
        }
    }

    // @Test
    public void InCondTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long original_res = 0, sample_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select count(*) from customer where c_nationkey in (1,2,3,4,5,6,99)");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getLong(1));
            err = (rs.getDouble(2));
        }
        rs = stmt.executeQuery("select count(*) from customer where c_nationkey in (1,2,3,4,5,6,99)");
        while (rs.next()){
            original_res = (rs.getLong(1));
        }
        assertEquals(true, original_res - err < sample_res && sample_res < original_res + err);
    }

    // @Test
    public void ExistCondTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long original_res = 0, sample_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select count(*) from (select c_name from customer AS C where exists (select * from supplier where C.c_nationkey = s_nationkey and s_acctbal > 5000))");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getLong(1));
            err = (rs.getDouble(2));
        }
        rs = stmt.executeQuery("select count(*) from (select c_name from customer AS C where exists (select * from supplier where C.c_nationkey = s_nationkey and s_acctbal > 5000))");
        while (rs.next()){
            original_res = (rs.getLong(1));
        }
        assertEquals(true, original_res - err <= sample_res && sample_res <= original_res + err);
    }

    // @Test //BUG: No feasible plan is found
    public void LimitTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        double original_res = 0, sample_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select avg(c_acctbal) from (select * from customer where c_nationkey > 5 LIMIT 100)");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getDouble(1));
            err = (rs.getDouble(2));
        }
        rs = stmt.executeQuery("select avg(c_acctbal) from (select * from customer where c_nationkey > 5 LIMIT 100)");
        while (rs.next()){
            original_res = (rs.getDouble(1));
        }
        assertEquals(true, original_res - err < sample_res && sample_res < original_res + err);
    }

    // @Test //BUG: No feasible plan is found
    public void OrderbyTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        double original_res = 0, sample_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select avg(c_acctbal) from (select * from customer where c_nationkey > 5 order by c_acctbal ASC LIMIT 100)");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getDouble(1));
            err = (rs.getDouble(2));
        }
        rs = stmt.executeQuery("select avg(c_acctbal) from (select * from customer where c_nationkey > 5 order by c_acctbal ASC LIMIT 100)");
        while (rs.next()){
            original_res = (rs.getDouble(1));
        }
        assertEquals(true, original_res - err < sample_res && sample_res < original_res + err);
    }

    // @Test
    public void GroupbyTest1() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        vc.executeJdbcQuery("select c_nationkey, count(*) as cnt from customer where c_nationkey > 5 group by c_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select c_nationkey, count(*) as cnt from customer where c_nationkey > 5 group by c_nationkey");
        while (rs.next()){
           original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i)
                    && (long)sample_res.get(i) < (long)original_res.get(i)+(double)err.get(i));
        }
    }

    // @Test
    public void GroupbyTest2() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        vc.executeJdbcQuery("select c_nationkey, count(*) from customer, supplier where c_nationkey=s_nationkey group by c_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select c_nationkey, count(*) from customer, supplier where c_nationkey=s_nationkey group by c_nationkey");
        while (rs.next()){
            original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i) &&
                    (long)sample_res.get(i) < (double)err.get(i)+(long)original_res.get(i));
        }
    }

    // @Test
    public void GroupbyTest3() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        vc.executeJdbcQuery("select c_nationkey, s_nationkey, count(*) from customer, supplier group by c_nationkey, s_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(3));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select c_nationkey, s_nationkey, count(*) from customer, supplier group by c_nationkey, s_nationkey");
        while (rs.next()){
            original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i) &&
                    (long)sample_res.get(i) < (double)err.get(i)+(long)original_res.get(i));
        }
    }

    // @Test
    public void CrossProductTest() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long sample_res = 0, original_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select count(*) as cnt from customer, supplier where c_nationkey=s_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getLong(1));
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select count(*) as cnt from customer, supplier where c_nationkey=s_nationkey");
        while (rs.next()){
            original_res = (rs.getLong(1));
        }
        assertEquals(true, (double)original_res-err<(double)sample_res && (double)sample_res < (double)original_res+err);
    }

    // @Test
    public void Join2Test() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long sample_res = 0, original_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select count(*) as cnt from customer inner join supplier on c_nationkey=s_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getLong(1));
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select count(*) as cnt from customer inner join supplier on c_nationkey=s_nationkey");
        while (rs.next()){
            original_res = (rs.getLong(1));
        }
        assertEquals(true, (double)original_res-err<(double)sample_res && (double)sample_res < err+(double)original_res);
    }

    // @Test
    public void Join2Test2() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        ResultSet rs;
        vc.executeJdbcQuery("select c_nationkey, count(*) from customer inner join supplier on c_nationkey=s_nationkey group by c_nationkey");
        rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select c_nationkey, count(*) from customer inner join supplier on c_nationkey=s_nationkey group by c_nationkey");
        while (rs.next()){
            original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i) &&
                    (long)sample_res.get(i) < (double)err.get(i)+(long)original_res.get(i));
        }
    }


    // @Test  //BUG: Exception in ExactRelation.from()
    public void Join2Test3() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        ArrayList original_res = new ArrayList();
        ArrayList sample_res = new ArrayList();
        ArrayList err = new ArrayList();
        ResultSet rs;
        vc.executeJdbcQuery("select count(*) from customer inner join supplier on (c_nationkey=s_nationkey and (c_acctbal>1000 or s_acctbal>1000))");
        rs = vc.getResultSet();
        while (rs.next()){
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        rs = stmt.executeQuery("select count(*) from customer inner join supplier on (c_nationkey=s_nationkey and (c_acctbal>1000 or s_acctbal>1000))");
        while (rs.next()){
            original_res.add(rs.getLong(1));
        }
        for (int i=0;i<original_res.size();i++){
            assertEquals(true,
                    (long)original_res.get(i)-(double)err.get(i)<(long)sample_res.get(i) &&
                            (long)sample_res.get(i) < (double)err.get(i)+(long)original_res.get(i));
        }
    }

    // @Test //BUG: Go wrong when rewriting sql. No agg
    public void UnionTest1() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long sample_res = 0, original_res = 0;
        double err = 0;
        ResultSet rs;
        vc.executeJdbcQuery("select c_nationkey as nationkey from customer union select s_nationkey as nationkey from supplier");
        rs = vc.getResultSet();
        while (rs.next()) {
            sample_res = (rs.getLong(1));
            err = rs.getDouble(2);
        }
        stmt.executeQuery("select c_nationkey as nationkey from customer union select s_nationkey as nationkey from supplier");
        while (rs.next()) {
            original_res = (rs.getLong(1));
        }
        assertEquals(true, (double) original_res - err < (double) sample_res && (double) sample_res < err + (double) original_res);
    }

    //@Test //BUG: Go wrong when rewriting sql. With agg
    public void UnionTesT2() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        List sample_res = new ArrayList(), original_res = new ArrayList(), err = new ArrayList();
        ResultSet rs;
        vc.executeJdbcQuery("select count(c_nationkey) as cnt from customer union select count(s_nationkey) as cnt from supplier");
        rs = vc.getResultSet();
        while (rs.next()) {
            sample_res.add(rs.getLong(1));
            err.add(rs.getDouble(2));
        }
        stmt.executeQuery("select count(c_nationkey) from customer union select count(s_nationkey) from supplier");
        while (rs.next()) {
            original_res.add(rs.getLong(1));
        }
        assertEquals(true, (long) original_res.get(0) - (double)err.get(0) < (long) sample_res.get(0)
                && (long) sample_res.get(0) < (double)err.get(0) + (long) original_res.get(0));
        assertEquals(true, (long) original_res.get(1) - (double)err.get(1) < (long) sample_res.get(1)
                && (long) sample_res.get(1) < (double)err.get(1) + (long) original_res.get(1));
    }

    // @Test
    public void Join3Test() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        long sample_res = 0, original_res = 0;
        double err = 0;
        vc.executeJdbcQuery("select count(*) as cnt from nation inner join customer on c_nationkey = n_nationkey inner join supplier on c_nationkey=s_nationkey");
        ResultSet rs = vc.getResultSet();
        while (rs.next()){
            sample_res = (rs.getLong(1));
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select count(*) as cnt from nation inner join customer on c_nationkey = n_nationkey inner join supplier on c_nationkey=s_nationkey");
        while (rs.next()){
            original_res = (rs.getLong(1));
        }
        assertEquals(true, (double)original_res-err<(double)sample_res && (double)sample_res < err+(double)original_res);
    }

    //@Test
    public void SubqueryTest1() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        vc.executeJdbcQuery("select sum(c_acctbal) from (select * from customer)");
        ResultSet rs = vc.getResultSet();
        long sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getLong(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select sum(c_acctbal) from (select * from customer)");
        if (rs.next()){
            double count = rs.getLong(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }

    //@Test
    public void SubqueryTest2() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        vc.executeJdbcQuery("select sum(c_acctbal) from (select * from customer where c_nationkey > 10)");
        ResultSet rs = vc.getResultSet();
        long sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getLong(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery("select sum(c_acctbal) from (select * from customer where c_nationkey > 10)");
        if (rs.next()){
            double count = rs.getLong(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }


    @Test //Wrong Answer
    public void SubqueryTest3() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "select sum(temp.s) from (select c_nationkey, sum(c_acctbal) as s from customer where c_nationkey > 10 group by c_nationkey) AS temp where temp.s>100";
        vc.executeJdbcQuery(sql);
        ResultSet rs = vc.getResultSet();
        double sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getDouble(1);
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery(sql);
        if (rs.next()){
            double count = rs.getLong(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }


    //@Test //BUG:Wrong Answer
    public void SubqueryTest4() throws VerdictException, SQLException{
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "select sum(s) from (select sum(c_acctbal) as s from customer group by c_nationkey)";
        vc.executeJdbcQuery(sql);
        ResultSet rs = vc.getResultSet();
        double sampleCount = 0;
        double err = 0;
        if (rs.next()){
            sampleCount = rs.getBigDecimal(1).doubleValue();
            err = rs.getDouble(2);
        }
        rs = stmt.executeQuery(sql);
        if (rs.next()){
            double count = rs.getDouble(1);
            assertEquals(true, sampleCount-err<=count && sampleCount+err>=count);
        }
    }



}
