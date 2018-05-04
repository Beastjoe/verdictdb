package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IntervalExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        IntervalExpr i = new IntervalExpr(dummyContext, 12, IntervalExpr.Unit.DAY);
        assertEquals("interval 12 days", i.toString());
    }

    @Test
    public void equalsTest(){
        IntervalExpr i1 = new IntervalExpr(dummyContext, 12, IntervalExpr.Unit.MONTH);
        IntervalExpr i2 = new IntervalExpr(dummyContext, 12, IntervalExpr.Unit.YEAR);
        assertEquals(false, i1.equals(i2));
    }
}
