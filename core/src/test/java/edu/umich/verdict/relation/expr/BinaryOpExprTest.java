package edu.umich.verdict.relation.expr;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BinaryOpExprTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void getLeftTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals("1", a.getLeft().toString());
    }

    @Test
    public void getRightTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals("2", a.getRight().toString());
    }

    @Test
    public void getOpTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals("*", a.getOp());
    }

    @Test
    public void toStringTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals("(1 * 2)", a.toString());
    }

    @Test
    public void isaggTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals(false, a.isagg());
    }

    @Test
    public void toSqlTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals("(1 * 2)", a.toSql());
    }

    @Test
    public void equalsTest(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        Expr dummyExprLeft1 = Expr.from(dummyContext, "2");
        Expr dummyExprRight1 = Expr.from(dummyContext, "1");
        BinaryOpExpr a1 = BinaryOpExpr.from(dummyContext, dummyExprLeft1, dummyExprRight1, "*");
        Expr dummyExprLeft2 = Expr.from(dummyContext, "1");
        Expr dummyExprRight2 = Expr.from(dummyContext, "2");
        BinaryOpExpr a2 = BinaryOpExpr.from(dummyContext, dummyExprLeft2, dummyExprRight2, "*");
        assertEquals(true, a.equals(a2)&&!a.equals(a1));
    }
}
