package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ColNameExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void fromTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("schema.tab.`col`", a.toString());
    }

    @Test
    public void getColTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("col", a.getCol());
    }

    @Test
    public void getTabTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("tab", a.getTab());
    }

    @Test
    public void getSchemaTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("schema", a.getSchema());
    }

    @Test
    public void setTabTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        a.setTab("newTab");
        assertEquals("newTab", a.getTab());
    }

    @Test
    public void setSchemaTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        a.setSchema("newSchema");
        assertEquals("newSchema", a.getSchema());
    }

    @Test
    public void toStringTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("schema.tab.`col`", a.toString());
    }

    @Test
    public void toStringWithoutQuote(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("schema.tab.`col`", a.toString());
    }

    @Test
    public void getTextTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("schema.tab.`col`", a.toString());
    }

    @Test
    public void withTableSubstitutedTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        Expr b = a.withTableSubstituted("newTab");
        assertEquals("newtab.col", b.getText());
    }

    @Test
    public void toSqlTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        assertEquals("schema.tab.`col`", a.toSql());
    }

    @Test
    public void equalsTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        ColNameExpr b = ColNameExpr.from(dummyContext, "schema.tab.col");
        ColNameExpr c = ColNameExpr.from(dummyContext, "schema.newtab.col");
        assertEquals(true, a.equals(b)&&!a.equals(c));
    }

    @Test
    public void compareToTest(){
        ColNameExpr a = ColNameExpr.from(dummyContext, "schema.tab.col");
        ColNameExpr b = ColNameExpr.from(dummyContext, "schema.newtab.col");
        assertEquals(6, a.compareTo(b));
    }
}
