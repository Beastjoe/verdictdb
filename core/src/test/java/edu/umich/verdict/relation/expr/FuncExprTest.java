package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FuncExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void fromTest(){
        FuncExpr f = FuncExpr.from(dummyContext, "COUNT(*)");
        assertEquals(FuncExpr.FuncName.COUNT, f.getFuncName());
    }

    @Test
    public void getUnaryExprTest(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals("*", f.getUnaryExpr().toString());
    }

    @Test
    public void getUnaryExprInStringTest(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals("*", f.getUnaryExprInString());
    }

    @Test
    public void countTest(){
        FuncExpr f = FuncExpr.count();
        assertEquals("count(*)", f.toString());
    }

    @Test
    public void toStringTest(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals("count(*)", f.toString());
    }

    @Test
    public void isaggTest(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals(true, f.isagg());
    }

    @Test
    public void isCountTest(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals(true, f.isCount());
    }

    @Test
    public void isMaxTest(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals(false, f.isMax());
    }

    @Test
    public void equalsTest(){
        FuncExpr f1 = FuncExpr.count();
        FuncExpr f2 = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals(false, f1.equals(f2));
    }
}
