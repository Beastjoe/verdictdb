package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictConf;
import edu.umich.verdict.VerdictJDBCContext;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

public class TimeStampParsingTest {

  @Test
  public void timeStampTest() throws VerdictException {

    VerdictConf conf = new VerdictConf();
    conf.setDbms("dummy");
    VerdictJDBCContext vc = VerdictJDBCContext.from(conf);

    String sql = "select * from mytable t where t.d > TIMESTAMP '1992-01-01 00:00:00'";
    ExactRelation r = ExactRelation.from(vc, sql);
    System.out.println(r.toSql());
  }

  @Test
  public void timeTest() throws VerdictException {

    VerdictConf conf = new VerdictConf();
    conf.setDbms("dummy");
    VerdictJDBCContext vc = VerdictJDBCContext.from(conf);

    String sql = "select * from mytable t where t.d > TIME '00:00:00'";
    ExactRelation r = ExactRelation.from(vc, sql);
    System.out.println(r.toSql());
  }

  @Test
  public void dateTest() throws VerdictException {

    VerdictConf conf = new VerdictConf();
    conf.setDbms("dummy");
    VerdictJDBCContext vc = VerdictJDBCContext.from(conf);

    String sql = "select * from mytable t where t.d > DATE '1992-01-01'";
    ExactRelation r = ExactRelation.from(vc, sql);
    System.out.println(r.toSql());
  }

  @Test
  public void dateTimeTest() throws VerdictException {

    VerdictConf conf = new VerdictConf();
    conf.setDbms("dummy");
    VerdictJDBCContext vc = VerdictJDBCContext.from(conf);

    String sql = "select * from mytable t where t.d > DATETIME '1992-01-01 00:00:00'";
    ExactRelation r = ExactRelation.from(vc, sql);
    System.out.println(r.toSql());
  }

}
