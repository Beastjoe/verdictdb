package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TableNameExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        TableNameExpr t = new TableNameExpr(dummyContext, "schema", "table");
        assertEquals("schema.table", t.toString());
    }
}
