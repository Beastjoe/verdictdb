package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OverClauseTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        OverClause o = OverClause.from(null, "OVER(PARTITION BY vt155.orderkey, vt155.price order by vt155.price DESC)");
        assertEquals(o.toString(), "OVER (partition by vt155.`orderkey`, vt155.`price` order by vt155.`price`)");
    }
}
