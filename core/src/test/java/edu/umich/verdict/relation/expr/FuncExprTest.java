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
    public void toStringTest1(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals("count(*)", f.toString());
    }

    @Test
    public void toStringTest2(){
        FuncExpr f1 = new FuncExpr(FuncExpr.FuncName.COUNT, ColNameExpr.from(dummyContext, "table.id"));
        FuncExpr f2 = new FuncExpr(FuncExpr.FuncName.ENCODE, f1, ConstantExpr.from(dummyContext, 16));
        assertEquals("encode(count(table.`id`), 16)", f2.toString());
    }

    @Test
    public void isaggTest1(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals(true, f.isagg());
    }

    @Test
    public  void isaggTest2(){
        FuncExpr f1 = new FuncExpr(FuncExpr.FuncName.COUNT, ColNameExpr.from(dummyContext, "table.id"));
        FuncExpr f2 = new FuncExpr(FuncExpr.FuncName.ENCODE, f1, ConstantExpr.from(dummyContext, 16));
        assertEquals(true, f2.isagg());
    }

    @Test
    public void isCountTest1(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        assertEquals(true, f.isCount());
    }

    @Test
    public void isCountTest2(){
        FuncExpr f1 = new FuncExpr(FuncExpr.FuncName.COUNT, ColNameExpr.from(dummyContext, "table.id"));
        FuncExpr f2 = new FuncExpr(FuncExpr.FuncName.ENCODE, f1, ConstantExpr.from(dummyContext, 16));
        assertEquals(false, f2.isCount());
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
