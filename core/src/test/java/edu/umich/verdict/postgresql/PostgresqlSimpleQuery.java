package edu.umich.verdict.postgresql;

import edu.umich.verdict.VerdictConf;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictJDBCContext;
import edu.umich.verdict.exceptions.VerdictException;


import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        conf.set("loglevel", "info");

        VerdictContext vc = VerdictJDBCContext.from(conf);
        String sql = "create 2% uniform sample of orders";
        String sql2 = "select count(distinct) as res from orders";
        vc.executeJdbcQuery(sql);
        ResultSet rs = vc.executeJdbcQuery(sql2);
        Double err;
        if (rs.next()) {
            Long cnt = rs.getLong(1);
            err = rs.getDouble(2);
            System.out.println(err);
        }
        sql = "drop samples of orders";
        vc.executeJdbcQuery(sql);
        vc.destroy();
    }
}
