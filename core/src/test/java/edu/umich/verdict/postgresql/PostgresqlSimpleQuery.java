package edu.umich.verdict.postgresql;

import edu.umich.verdict.VerdictConf;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictJDBCContext;
import edu.umich.verdict.dbms.DbmsJDBC;
import edu.umich.verdict.exceptions.VerdictException;


import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static edu.umich.verdict.postgresql.PostgresqlBasicTest.connect;
import edu.umich.verdict.postgresql.PostgresqlBasicTest;

public class PostgresqlSimpleQuery {
    public static PostgresqlBasicTest postgresqlBasicTest;
    public static void main(String[] args) throws VerdictException, FileNotFoundException, SQLException{
        VerdictConf conf = new VerdictConf();
        conf.setDbms("postgresql");
        conf.setHost("localhost");
        conf.setPort("5432");
        conf.setDbmsSchema("tpch1g");
        conf.setUser("postgres");
        conf.setPassword("zhongshucheng123");
        conf.set("loglevel", "debug");

        VerdictContext vc = VerdictJDBCContext.from(conf);
        String sql = "create stratified sample of customer on c_nationkey";
        String sql2 = "select count(c_nationkey) from customer";
        vc.executeJdbcQuery(sql);
        ResultSet rs = vc.executeJdbcQuery(sql2);
        Double err;
        DbmsJDBC dbms = vc.getDbms().getDbmsJDBC();
        Statement stmt = dbms.createStatement();
        if (rs.next()) {
            Long cnt = rs.getLong(1);
            //err = rs.getDouble(2);
            System.out.println(cnt);
        }
        sql = "drop samples of orders";
        vc.executeJdbcQuery(sql);
        vc.destroy();
    }
}
