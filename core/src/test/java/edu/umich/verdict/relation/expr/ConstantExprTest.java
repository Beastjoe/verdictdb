package edu.umich.verdict.relation.expr;

import com.sun.tools.internal.jxc.ap.Const;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ConstantExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void setValueTest1(){
        ConstantExpr c = new ConstantExpr(dummyContext, "123");
        c.setValue("111");
        assertEquals("111", c.getValue());
    }

    @Test
    public void setValueTest2(){
        ConstantExpr c = new ConstantExpr(dummyContext, "123");
        BinaryOpExpr b1;
        BinaryOpExpr b2 = BinaryOpExpr.from(dummyContext, ColNameExpr.from(dummyContext, "table.col"),
                ConstantExpr.from(dummyContext, 2), "*");
        b1 = BinaryOpExpr.from(dummyContext, ConstantExpr.from(dummyContext, 1), b2, "/");
        c.setValue(b1);
        assertEquals(b1, c.getValue());
    }

    @Test
    public void toStringTest(){
        ConstantExpr c = new ConstantExpr(dummyContext, "123");
        assertEquals("123", c.toString());
    }

    @Test
    public void equalsTest(){
        ConstantExpr a = new ConstantExpr(dummyContext, "123");
        ConstantExpr b = new ConstantExpr(dummyContext, "123");
        ConstantExpr c = new ConstantExpr(dummyContext, "124");
        assertEquals(true, a.equals(b)&&!a.equals(c));
    }
}
