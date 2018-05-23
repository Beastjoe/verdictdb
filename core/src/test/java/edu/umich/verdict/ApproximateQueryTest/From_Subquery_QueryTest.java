package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class From_Subquery_QueryTest extends ApproximateQueryTestBase {
    /*
        Substitute Flat Query
     */
    @Test
    public void FlatQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from (select * from customer where c_nationkey > 10)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FlatQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from (select * from customer where c_nationkey > 10)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FlatQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from (select * from customer where c_nationkey > 10)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FlatQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from (select * from customer where c_nationkey > 10)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FlatQuery2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from (select o_totalprice from orders where o_shippriority=0)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FlatQuery2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from (select o_totalprice from orders where o_shippriority=0)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FlatQuery2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from (select o_totalprice from orders where o_shippriority=0)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FlatQuery2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_orderstatus) from (select o_orderstatus, o_totalprice from orders where o_shippriority=0)";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
    Substitute Filter Query
     */
    @Test
    public void FilterQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from (select * from customer where c_acctbal > 1000) where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from (select * from customer where c_acctbal > 1000) where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from (select * from customer where c_acctbal > 1000) where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from (select * from customer where c_acctbal > 1000) where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from (select o_totalprice, o_shippriority from orders where o_custkey>10) where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from (select o_totalprice, o_shippriority from orders where o_custkey>10) where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from (select o_totalprice, o_shippriority from orders where o_custkey>10) where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_orderstatus) from (select o_totalprice, o_shippriority, o_orderstatus from orders where o_custkey>10) where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
        Substitute Group by Query
     */
    @Test
    public void GroupbyQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from (select * from customer where c_acctbal > 1000) group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, sum(c_nationkey) from (select * from customer where c_acctbal > 1000) group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, avg(c_nationkey) from (select * from customer where c_acctbal > 1000) group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, count(distinct c_nationkey) from (select * from customer where c_acctbal > 1000) group by c_nationkey order by c_nationkey";
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
        String sql = "Select s_nationkey, count(1) from (select s_nationkey from supplier where s_acctbal > 1000 and s_nationkey <15) group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, sum(s_acctbal) from (select s_nationkey, s_acctbal from supplier where s_acctbal > 1000 and s_nationkey <15) group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, avg(s_acctbal) from (select s_nationkey, s_acctbal from supplier where s_acctbal > 1000 and s_nationkey <15) group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, count(distinct s_nationkey) from (select s_nationkey, s_acctbal from supplier where s_acctbal > 1000 and s_nationkey <15) group by s_nationkey order by s_nationkey";
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
        Substitute Filter Group by Query
     */
    @Test
    public void FilterGroupbyQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from (select * from customer where c_nationkey > 10) where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
    public void FilterGroupbyQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, sum(c_nationkey) from (select * from customer where c_nationkey > 10) where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
    public void FilterGroupbyQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, avg(c_nationkey) from (select * from customer where c_nationkey > 10) where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
    public void FilterGroupByQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(distinct c_nationkey) from (select * from customer where c_nationkey > 10) where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
    public void FilterGroupbyQuery2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, count(1) from (select s_nationkey, s_acctbal from supplier where s_suppkey > 10) where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey";
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
    public void FilterGroupbyQuery2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, sum(s_acctbal) from (select s_nationkey, s_acctbal from supplier where s_suppkey > 10) where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey";
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
    public void FilterGroupbyQuery2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, avg(s_acctbal) from (select s_nationkey, s_acctbal from supplier where s_suppkey > 10) where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey";
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
    public void FilterGroupByQuery2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select s_nationkey, count(distinct s_nationkey) from (select s_nationkey, s_acctbal from supplier where s_suppkey > 10) where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey";
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
}
