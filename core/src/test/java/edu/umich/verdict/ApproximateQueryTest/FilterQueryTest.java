package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FilterQueryTest extends ApproximateQueryTestBase {

    /*
        One Predicate
     */
    @Test
    public void FilterQuery1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from customer where c_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from orders where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from orders where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from orders where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_orderstatus) from orders where o_shippriority = 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery3_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from supplier where s_acctbal > 100";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery3_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(s_acctbal) from supplier where s_acctbal > 100";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery3_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(s_acctbal) from supplier where s_acctbal > 100";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery3_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct s_nationkey) from supplier where s_acctbal > 100";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery4_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from supplier where s_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery4_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(s_acctbal) from supplier where s_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery4_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(s_acctbal) from supplier where s_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FlatQuery4_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct s_nationkey) from supplier where s_nationkey > 10";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
        Two Predicates
     */
    @Test
    public void FilterQuery5_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer where c_nationkey > 10 and c_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery5_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_acctbal) from customer where c_nationkey > 10 and c_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery5_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_acctbal) from customer where c_nationkey > 10 and c_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery5_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from customer where c_nationkey > 10 and c_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery6_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from supplier where s_nationkey > 10 and s_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery6_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(s_acctbal) from supplier where s_nationkey > 10 and s_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery6_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(s_acctbal) from supplier where s_nationkey > 10 and s_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery6_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct s_nationkey) from supplier where s_nationkey > 10 and s_acctbal > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery7_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from orders where o_shippriority=0 and o_totalprice > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery7_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from orders where o_shippriority=0 and o_totalprice > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery7_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from orders where o_shippriority=0 and o_totalprice > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery7_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from orders where o_shippriority=0 and o_totalprice > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery8_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from orders where o_custkey>500 and o_shippriority > 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void FilterQuery8_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from orders where o_custkey>500 and o_shippriority > 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FilterQuery8_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from orders where o_custkey>500 and o_shippriority > 0";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void FlatQuery8_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from orders where o_custkey>500 and o_totalprice > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }
}
