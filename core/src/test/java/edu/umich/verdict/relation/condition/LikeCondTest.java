package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.relation.expr.Expr;
import org.junit.Test;

public class LikeCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        LikeCond l = new LikeCond(Expr.from(dummyContext, "CustomerName"), Expr.from(dummyContext, "'a%'"), true);
        assertEquals("`customername` NOT LIKE 'a%'", l.toString());
    }

    @Test
    public void equalsTest(){
        LikeCond l1 = new LikeCond(Expr.from(dummyContext, "CustomerName"), Expr.from(dummyContext, "'a%'"), true);
        LikeCond l2 = new LikeCond(Expr.from(dummyContext, "CustomerName"), Expr.from(dummyContext, "'a%'"), false);
        assertEquals(false, l1.equals(l2));
    }

}
