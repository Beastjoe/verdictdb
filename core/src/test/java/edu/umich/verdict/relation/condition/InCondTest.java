package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.relation.expr.ColNameExpr;
import edu.umich.verdict.relation.expr.ConstantExpr;
import edu.umich.verdict.relation.expr.Expr;
import org.junit.Test;

import java.util.Arrays;

public class InCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        Expr c1 = ConstantExpr.from(dummyContext, "Germany");
        Expr c2 = ConstantExpr.from(dummyContext, "UK");
        Expr c3 = ConstantExpr.from(dummyContext, "France");
        InCond i = new InCond(ColNameExpr.from(dummyContext, "country"), false, Arrays.asList(c1, c2, c3));
        assertEquals("`country` IN (Germany, UK, France)", i.toString());
    }

    @Test
    public void equalsTest(){
        Expr c1 = ConstantExpr.from(dummyContext, "Germany");
        Expr c2 = ConstantExpr.from(dummyContext, "UK");
        Expr c3 = ConstantExpr.from(dummyContext, "France");
        InCond i1 = new InCond(ColNameExpr.from(dummyContext, "country"), false, Arrays.asList(c1, c2, c3));
        InCond i2 = new InCond(ColNameExpr.from(dummyContext, "country"), false, Arrays.asList(c2, c1, c3));
        assertEquals(false, i1.equals(i2));

    }
}
