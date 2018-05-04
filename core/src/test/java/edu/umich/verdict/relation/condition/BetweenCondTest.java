package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.relation.expr.ColNameExpr;
import edu.umich.verdict.relation.expr.ConstantExpr;
import org.junit.Test;

public class BetweenCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest() {
        ColNameExpr col = ColNameExpr.from(dummyContext, "table.id");
        ConstantExpr c1 = ConstantExpr.from(dummyContext, 1);
        ConstantExpr c2 = ConstantExpr.from(dummyContext, 2);
        BetweenCond b = new BetweenCond(col, c1, c2);
        assertEquals("table.`id` BETWEEN 1 AND 2", b.toString());
    }

    @Test
    public void equalsTest(){
        ColNameExpr col = ColNameExpr.from(dummyContext, "table.id");
        ConstantExpr c1 = ConstantExpr.from(dummyContext, 1);
        ConstantExpr c2 = ConstantExpr.from(dummyContext, 2);
        BetweenCond b1 = new BetweenCond(col, c1, c2);
        BetweenCond b2 = new BetweenCond(col,c2,c1);
        assertEquals(false, b1.equals(b2));
    }
}
