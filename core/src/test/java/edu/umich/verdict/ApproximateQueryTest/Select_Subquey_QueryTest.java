package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Select_Subquey_QueryTest extends ApproximateQueryTestBase {
    /*
        Substitute Flat Query
    */
    @Test
    public void FlatQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(1) from customer) from customer";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FlatQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select sum(c_nationkey) from customer) from customer";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FlatQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select avg(c_nationkey) from customer) from customer";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FlatQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(distinct c_nationkey) from customer) from customer";
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
        String sql = "Select (select count(1) from customer) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select sum(c_nationkey) from customer) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select avg(c_nationkey) from customer) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(distinct c_nationkey) from customer) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
    Substitute Join2
     */
    @Test
    public void Join2Query1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(1) from customer) from customer left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join2Query1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select sum(c_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join2Query1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select avg(c_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join2Query1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(distinct c_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey";
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
        String sql = "Select c_nationkey, select(count(1) from customer) from customer group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, (select sum(c_nationkey) from customer) from customer group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, (select avg(c_nationkey) from customer) from customer group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, (select count(distinct c_nationkey) from customer) from customer group by c_nationkey order by c_nationkey";
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
    Substitute Filter Join2 Query
     */
    @Test
    public void FilterJoin2Query1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(1) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterJoin2Query1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select sum(c_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterJoin2Query1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select avg(c_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterJoin2Query1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select (select count(distinct c_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
    Substitute Filter Group by Query
     */
    @Test
    public void FilterGroupbyQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, (select count(1) from customer) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, (select sum(c_nationkey) from customer) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, (select avg(c_nationkey) from customer) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
        String sql = "Select c_nationkey, (select count(distinct c_nationkey) from customer) from customer where c_acctbal>1000 group by c_nationkey order by c_nationkey";
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
    Substitute Filter Group by Join2 Query
     */
    @Test
    public void FilterGroupbyJoin2Query1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select c_nationkey, (select count(1) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select c_nationkey, (select sum(c_acctbal) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select c_nationkey, (select avg(c_acctbal) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
        String sql = "Select c_nationkey, (select count(distinct s_nationkey) from customer) from customer left join supplier on c_nationkey = s_nationkey where c_nationkey > 10 group by c_nationkey order by c_nationekey";
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
