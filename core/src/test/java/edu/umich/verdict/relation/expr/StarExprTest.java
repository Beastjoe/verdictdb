package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StarExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void setTabTest(){
        TableNameExpr t = new TableNameExpr(dummyContext, "schema", "tab");
        StarExpr s = new StarExpr(t);
        assertEquals("schema.tab.*", s.toString());
    }

}
