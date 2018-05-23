package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import javafx.util.Pair;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupbyQueryTest extends ApproximateQueryTestBase {
    /*
        Group by one column
     */
    @Test
    public void GroupbyQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer group by c_nationkey order by c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, sum(c_nationkey) from customer group by c_nationkey order by c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, avg(c_nationkey) from customer group by c_nationkey order by c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getDouble(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getDouble(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupByQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(distinct c_nationkey) from customer group by c_nationkey order by c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, count(1) from supplier group by s_nationkey order by s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, sum(s_acctbal) from supplier group by s_nationkey order by s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getDouble(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getDouble(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, avg(s_acctbal) from supplier group by s_nationkey order by s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getDouble(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getDouble(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupByQuery2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, count(distinct s_nationkey) from supplier group by s_nationkey order by s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery3_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(1) from orders group by o_shippriority order by o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery3_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, sum(o_totalprice) from orders group by o_shippriority order by o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getDouble(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery3_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, avg(o_totalprice) from orders group by o_shippriority order by o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getDouble(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getDouble(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupByQuery3_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(distinct o_custkey) from orders group by o_shippriority order by o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(sample.getLong(1));
            sampleRes.add(sample.getLong(2));
        }
        while (origin.next()){
            originGroupBy.add(origin.getLong(1));
            originRes.add(origin.getLong(2));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    /*
        Group by two columns
     */
    @Test
    public void GroupbyQuery4_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, count(1) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery4_Sum() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, sum(o_custkey) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery4_Avg() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, avg(o_custkey) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery4_CountDistinct() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, count(distinct o_custkey) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery5_Sum() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, sum(o_totalprice) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery5_Avg() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, avg(o_totalprice) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

    @Test
    public void GroupbyQuery5_CountDistinct() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, o_orderpriority, count(distinct o_clerk) from orders group by o_shippriority, o_orderpriority order by o_shippriority, o_orderpriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1), sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1), origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
        Compare(sampleRes, originRes, sampleGroupBy, originGroupBy);
    }

}
