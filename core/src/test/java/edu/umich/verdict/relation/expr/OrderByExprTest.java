package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OrderByExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void fromTest(){
        OrderByExpr o = OrderByExpr.from(null, " count(*) AS cnt order by cnt ASC");
        assertEquals("ASC", o.getDirection().toString());
        assertEquals("count(*)", o.getExpression().toString());
    }

    @Test
    public void toStringTest(){
        OrderByExpr o = OrderByExpr.from(null, " count(*) AS cnt order by cnt ASC");
        assertEquals("count(*) ASC", o.toString());
    }

    @Test
    public void equalsTest(){
        OrderByExpr o1 = OrderByExpr.from(null, " count(*) AS cnt order by cnt ASC");
        OrderByExpr o2 = OrderByExpr.from(null, " count(*) order by cnt ASC");
        assertEquals(true, o1.equals(o2));
    }
}
