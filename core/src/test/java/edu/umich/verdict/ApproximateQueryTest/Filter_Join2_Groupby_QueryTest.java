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

public class Filter_Join2_Groupby_QueryTest extends ApproximateQueryTestBase {
    /*
        Left Join
     */
    @Test
    public void FilterGroupbyJoin2Query1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select c_nationkey, sum(c_acctbal) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select c_nationkey, avg(c_acctbal) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select c_nationkey, count(distinct s_nationkey) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select o_shippriority, count(1) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
        String sql = "Select o_shippriority, sum(c_acctbal) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
        String sql = "Select o_shippriority, avg(c_acctbal) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
        String sql = "Select o_shippriority, count(distinct c_nationkey) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query3_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(1) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query3_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, sum(c_acctbal) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query3_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, avg(c_acctbal) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query3_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(distinct c_custkey) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    /*
        Right Join
     */
    @Test
    public void FilterGroupbyJoin2Query4_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer right join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query4_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, sum(c_acctbal) from customer right join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query4_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, avg(c_acctbal) from customer right join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query4_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(distinct s_nationkey) from customer right join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query5_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(1) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query5_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, sum(c_acctbal) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query5_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, avg(c_acctbal) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query5_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(distinct c_nationkey) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query6_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(1) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query6_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, sum(c_acctbal) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query6_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, avg(c_acctbal) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query6_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(distinct c_custkey) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    /*
        Inner Join
     */
    @Test
    public void FilterGroupbyJoin2Query7_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer inner join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query7_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, sum(c_acctbal) from customer inner join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query7_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, avg(c_acctbal) from customer inner join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query7_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(distinct s_nationkey) from customer inner join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query8_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(1) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query8_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, sum(c_acctbal) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query8_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, avg(c_acctbal) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query8_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(distinct c_nationkey) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query9_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(1) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query9_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, sum(c_acctbal) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query9_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, avg(c_acctbal) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query9_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(distinct c_custkey) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    /*
        Outer Join
     */
    @Test
    public void FilterGroupbyJoin2Query10_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(1) from customer outer join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query10_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, sum(c_acctbal) from customer outer join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query10_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, avg(c_acctbal) from customer outer join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query10_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, count(distinct s_nationkey) from customer outer join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
    public void FilterGroupbyJoin2Query11_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(1) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query11_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, sum(c_acctbal) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query11_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, avg(c_acctbal) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query11_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select o_shippriority, count(distinct c_nationkey) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 where o_shippriority = 0 and c_nationkey > 5 group by o_shippriority order by shippriority";
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
    public void FilterGroupbyJoin2Query12_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(1) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query12_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, sum(c_acctbal) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getDouble(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getDouble(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query12_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, avg(c_acctbal) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }

    @Test
    public void FilterGroupbyJoin2Query12_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, o_shippriority, count(distinct c_custkey) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 where c_nationkey > 5 and o_totalprice > 1000 group by c_nationkey, o_shippriority order by c_nationkey, o_shippriority";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        List sampleRes = new ArrayList(), sampleGroupBy = new ArrayList(),
                originRes = new ArrayList(), originGroupBy = new ArrayList();
        while (sample.next()){
            sampleGroupBy.add(new Pair(sample.getLong(1),sample.getLong(2)));
            sampleRes.add(sample.getLong(3));
        }
        while (origin.next()){
            originGroupBy.add(new Pair(origin.getLong(1),origin.getLong(2)));
            originRes.add(origin.getLong(3));
        }
    }
}
