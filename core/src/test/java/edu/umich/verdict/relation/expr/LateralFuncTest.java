package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LateralFuncTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        LateralFunc l = new LateralFunc(LateralFunc.LateralFuncName.EXPLODE, Expr.from(null, "t.a"));
        assertEquals("explode(t.`a`)", l.toString());
    }

    @Test
    public void equalsTest(){
        LateralFunc l1 = new LateralFunc(LateralFunc.LateralFuncName.UNKNOWN, Expr.from(null, "t.a"));
        LateralFunc l2 = new LateralFunc(LateralFunc.LateralFuncName.EXPLODE, Expr.from(null, "t.a"));
        assertEquals(false, l1.equals(l2));
    }
}
