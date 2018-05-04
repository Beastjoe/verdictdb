package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.relation.expr.Expr;
import org.junit.Test;

public class ExistsCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        ExistsCond ex = new ExistsCond(Expr.from(dummyContext, "A"));
        assertEquals(" EXISTS (`A`)", ex.toString());
    }
}
