package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.relation.expr.ColNameExpr;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IsCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        IsCond i = new IsCond(ColNameExpr.from(dummyContext, "table.id"), Cond.from(dummyContext, "NULL"));
        assertEquals("(table.`id` IS null)", i.toString());
    }

}
