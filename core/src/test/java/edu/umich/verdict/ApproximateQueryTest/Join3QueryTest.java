package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Join3QueryTest extends ApproximateQueryTestBase {
    /*
       Left Join
    */
    @Test
    public void Join3Query1_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer left join supplier on c_nationkey = s_nationkey left join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query1_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from customer left join supplier on c_nationkey = s_nationkey left join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query1_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from customer left join supplier on c_nationkey = s_nationkey left join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query1_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from customer left join supplier on c_nationkey = s_nationkey left join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query2_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query2_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query2_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000 left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query2_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer left join orders on o_custkey = c_custkey and o_totalprice > 1000";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query3_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000  left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query3_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query3_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query3_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000  left join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
        Right Join
     */
    @Test
    public void Join3Query4_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer right join supplier on c_nationkey = s_nationkey right join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query4_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from customer right join supplier on c_nationkey = s_nationkey right join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query4_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from customer right join supplier on c_nationkey = s_nationkey right join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query4_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from customer right join supplier on c_nationkey = s_nationkey right join orders on c_custkey = o_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query5_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query5_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query5_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query5_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer right join orders on o_custkey = c_custkey and o_totalprice > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query6_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer left join orders on o_custkey = c_custkey and c_acctbal > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query6_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query6_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query6_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer right join orders on o_custkey = c_custkey and c_acctbal > 1000 right join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
        Inner Join
     */
    @Test
    public void Join3Query7_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer inner join supplier on c_nationkey = s_nationkey inner join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query7_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from customer inner join supplier on c_nationkey = s_nationkey inner join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query7_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from customer inner join supplier on c_nationkey = s_nationkey inner join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query7_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from customer inner join supplier on c_nationkey = s_nationkey inner join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query8_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query8_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query8_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query8_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer inner join orders on o_custkey = c_custkey and o_totalprice > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query9_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query9_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query9_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query9_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer inner join orders on o_custkey = c_custkey and c_acctbal > 1000 inner join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    /*
        Outer Join
     */
    @Test
    public void Join3Query10_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer outer join supplier on c_nationkey = s_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query10_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(c_nationkey) from customer outer join supplier on c_nationkey = s_nationkey outer join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query10_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(c_nationkey) from customer outer join supplier on c_nationkey = s_nationkey outer join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query10_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct c_nationkey) from customer outer join supplier on c_nationkey = s_nationkey outer join orders on o_custkey = c_custkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query11_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query11_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query11_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query11_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer outer join orders on o_custkey = c_custkey and o_totalprice > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query12_Count() throws VerdictException, SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(1) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }

    @Test
    public void Join3Query12_Sum() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select sum(o_totalprice) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query12_Avg() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select avg(o_totalprice) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "double");
    }

    @Test
    public void Join3Query12_CountDistinct() throws VerdictException,SQLException {
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        String sql = "Select count(distinct o_shippriority) from customer outer join orders on o_custkey = c_custkey and c_acctbal > 1000 outer join supplier on s_nationkey = c_nationkey";
        ResultSet sample = vc.executeJdbcQuery(sql);
        ResultSet origin = stmt.executeQuery(sql);
        Compare(sample, origin, "long");
    }
}
