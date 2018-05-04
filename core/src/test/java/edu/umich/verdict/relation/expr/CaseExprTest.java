package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import edu.umich.verdict.relation.condition.Cond;
import org.junit.Test;

import java.util.Arrays;

public class CaseExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void getConditionsTest(){
        Cond c1 = Cond.from(dummyContext, "t.a=t.b");
        Expr a = Expr.from(dummyContext, "1");
        CaseExpr c = new CaseExpr(dummyContext, Arrays.asList(c1), Arrays.asList(a));
        assertEquals("t.'a'=t.'b'", c.getConditions().get(0).toString());
    }

    @Test
    public void getExpressionsTest(){
        Cond c1 = Cond.from(dummyContext, "t.a=t.b");
        Expr a = Expr.from(dummyContext, "1");
        CaseExpr c = new CaseExpr(dummyContext, Arrays.asList(c1), Arrays.asList(a));
        assertEquals("1", c.getExpressions().get(0).toString());
    }

    @Test
    public void fromTest(){
        CaseExpr c = CaseExpr.from(dummyContext, "(CASE WHEN t.a = t.b THEN 1 END)");
        assertEquals("1", c.getExpressions().get(0).toString());
    }

    @Test
    public void toStringTest(){
        CaseExpr c = CaseExpr.from(dummyContext, "(CASE WHEN t.a = t.b THEN 1 END)");
        assertEquals( "(CASE WHEN t.`a` = t.`b` THEN 1 END)", c.toString());
    }

    @Test
    public void toSqlTest(){
        CaseExpr c = CaseExpr.from(dummyContext, "(CASE WHEN t.a = t.b THEN 1 END)");
        assertEquals("(CASE WHEN t.`a` = t.`b` THEN 1 END)", c.toSql());
    }

    @Test
    public void equalsTest(){
        CaseExpr c1 = CaseExpr.from(dummyContext, "(CASE WHEN t.a = t.b THEN 1 END)");
        CaseExpr c2 = CaseExpr.from(dummyContext, "(CASE WHEN t.a = t.b THEN 1 END)");
        assertEquals(true, c1.equals(c2));
    }

}
