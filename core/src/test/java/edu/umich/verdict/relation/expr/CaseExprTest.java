package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.relation.condition.AndCond;
import edu.umich.verdict.relation.condition.Cond;
import edu.umich.verdict.relation.condition.IsCond;
import edu.umich.verdict.relation.condition.NullCond;
import org.junit.Test;

import java.util.Arrays;

public class CaseExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void getConditionsTest(){
        Cond c1 = Cond.from(dummyContext, "t.a=t.b");
        Expr a = Expr.from(dummyContext, "1");
        CaseExpr c = new CaseExpr(dummyContext, Arrays.asList(c1), Arrays.asList(a));
        assertEquals("t.`a` = t.`b`", c.getConditions().get(0).toString());
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
        CaseExpr c = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 1 END");
        assertEquals("1", c.getExpressions().get(0).toString());
    }

    @Test
    public void toStringTest1(){
        CaseExpr c = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 1 END");
        assertEquals( "(CASE WHEN t.`a` = t.`b` THEN 1 END)", c.toString());
    }

    @Test
    public void toStringTest2(){
        AndCond c1 = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"),
                Cond.from(dummyContext, "o_orderkey>62340"));
        IsCond c2 = new IsCond(ColNameExpr.from(dummyContext, "table.id"), new NullCond());
        Expr a1 = Expr.from(dummyContext, "1");
        Expr a2 = Expr.from(dummyContext, "2");
        Expr a3 = Expr.from(dummyContext, "3");
        CaseExpr c = new CaseExpr(dummyContext, Arrays.asList(c1,c2), Arrays.asList(a1, a2, a3));
        assertEquals("(CASE WHEN (t.`o_orderkey` = t.`vpart`) AND (`o_orderkey` > 62340) THEN 1 WHEN (table.`id` IS NULL) THEN 2 ELSE 3 END)",
                c.toString());
    }

    @Test
    public void toSqlTest(){
        CaseExpr c = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 1 END");
        assertEquals("(CASE WHEN t.`a` = t.`b` THEN 1 END)", c.toSql());
    }

    @Test
    public void equalsTest(){
        CaseExpr c1 = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 1 END");
        CaseExpr c2 = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 1 END");
        CaseExpr c3 = CaseExpr.from(dummyContext, "CASE WHEN t.b = t.a THEN 1 END");
        CaseExpr c4 = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 2 END");
        CaseExpr c5 = CaseExpr.from(dummyContext, "CASE WHEN t.a = t.b THEN 1 ELSE 2 END");
        assertEquals(true, c1.equals(c2));
        assertEquals(false, c1.equals(c3));
        assertEquals(false, c1.equals(c4));
        assertEquals(false, c1.equals(c5));
    }

}
