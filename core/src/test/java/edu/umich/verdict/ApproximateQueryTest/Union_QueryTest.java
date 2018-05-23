package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Union_QueryTest extends ApproximateQueryTestBase {
    /*
        Union of Group by Query
     */
    @Test
    public void GroupbyQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer group by c_nationkey order by c_nationkey union Select s_nationkey, count(1) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select c_nationkey, sum(c_nationkey) from customer group by c_nationkey order by c_nationkey union Select s_nationkey, sum(s_nationkey) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select c_nationkey, avg(c_nationkey) from customer group by c_nationkey order by c_nationkey union Select s_nationkey, avg(s_nationkey) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select c_nationkey, count(distinct c_nationkey) from customer group by c_nationkey order by c_nationkey union Select s_nationkey, count(distinct s_nationkey) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, count(1) from supplier group by s_nationkey order by s_nationkey union Select s_nationkey, count(1) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, sum(s_acctbal) from supplier group by s_nationkey order by s_nationkey union Select s_nationkey, sum(s_acctbal) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, avg(s_acctbal) from supplier group by s_nationkey order by s_nationkey union Select s_nationkey, avg(s_acctbal) from supplier group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, count(distinct s_nationkey) from supplier group by s_nationkey order by s_nationkey union Select s_nationkey, count(distinct s_nationkey) from supplier group by s_nationkey order by s_nationkey";
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
        Union Filter Group by Query
     */
    @Test
    public void FilterGroupbyQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey union Select c_nationkey, count(1) from customer where c_nationkey>10 group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, sum(c_nationkey) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey union Select c_nationkey, sum(c_nationkey) from customer where c_nationkey>10 group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, avg(c_nationkey) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey union Select c_nationkey, avg(c_nationkey) from customer where c_nationkey>10 group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, count(distinct c_nationkey) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey union Select c_nationkey, count(distinct c_nationkey) from customer where c_nationkey>10 group by c_nationkey order by c_nationkey";
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
        String sql = "Select s_nationkey, count(1) from supplier where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey union select s_nationkey, count(1) from supplier where s_suppkey > 50 group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, sum(s_acctbal) from supplier where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey union select s_nationkey, sum(s_acctbal) from supplier where s_suppkey > 50 group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, avg(s_acctbal) from supplier where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey union select s_nationkey, avg(s_acctbal) from supplier where s_suppkey > 50 group by s_nationkey order by s_nationkey";
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
        String sql = "Select s_nationkey, count(distinct s_nationkey) from supplier where s_nationkey > 5 and s_acctbal < 5000 group by s_nationkey order by s_nationkey union select s_nationkey, count(distinct s_nationkey) from supplier where s_suppkey > 50 group by s_nationkey order by s_nationkey";
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
        Filter Groupby Join2 Query
     */
    @Test
    public void FilterGroupbyJoin2Query1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer left join supplier on c_nationkey = s_nationkey " +
                "where c_nationkey > 10 group by c_nationkey order by c_nationekey union Select c_nationkey, count(1) from " +
                "customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey " +
                "order by c_nationekey";
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
    }

    @Test
    public void FilterGroupbyJoin2Query1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, sum(c_acctbal) from customer left join supplier on c_nationkey = s_nationkey " +
                "where c_nationkey > 10 group by c_nationkey order by c_nationekey union Select c_nationkey, sum(c_acctbal) from " +
                "customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey " +
                "order by c_nationekey";
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
    }

    @Test
    public void FilterGroupbyJoin2Query1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, avg(c_acctbal) from customer left join supplier on c_nationkey = s_nationkey " +
                "where c_nationkey > 10 group by c_nationkey order by c_nationekey union Select c_nationkey, avg(c_acctbal) from " +
                "customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey " +
                "order by c_nationekey";
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
    }

    @Test
    public void FilterGroupbyJoin2Query1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(distinct s_nationkey) from customer left join supplier on c_nationkey = s_nationkey " +
                "where c_nationkey > 10 group by c_nationkey order by c_nationekey union Select c_nationkey, count(distinct s_nationkey) from " +
                "customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey " +
                "order by c_nationekey";
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
    }

    @Test
    public void FilterGroupbyJoin2Query2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(1) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 " +
                "where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority union " +
                "Select o_shippriority, count(1) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 " +
                "where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    }

    @Test
    public void FilterGroupbyJoin2Query2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, sum(c_acctbal) from customer left join orders on o_custkey = c_custkey and " +
                "o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority " +
                "order by shippriority union Select o_shippriority, sum(c_acctbal) from customer left join orders on " +
                "o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 " +
                "group by o_shippriority order by shippriority";
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
    }

    @Test
    public void FilterGroupbyJoin2Query2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, avg(c_acctbal) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 " +
                "where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority union Select " +
                "o_shippriority, avg(c_acctbal) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 " +
                "where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    }

    @Test
    public void FilterGroupbyJoin2Query2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(distinct c_nationkey) from customer left join orders on o_custkey = c_custkey " +
                "and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority " +
                "order by shippriority union Select o_shippriority, count(distinct c_nationkey) from customer left join orders on " +
                "o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority " +
                "order by shippriority";
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
    }
}
