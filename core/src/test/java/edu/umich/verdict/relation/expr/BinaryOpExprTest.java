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
    public void toStringTest1(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals("(1 * 2)", a.toString());
    }

    @Test
    public void toStringTest2(){
        BinaryOpExpr b1;
        BinaryOpExpr b2 = BinaryOpExpr.from(dummyContext, ColNameExpr.from(dummyContext, "table.col"),
                ConstantExpr.from(dummyContext, 2), "*");
        b1 = BinaryOpExpr.from(dummyContext, ConstantExpr.from(dummyContext, 1), b2, "/");
        assertEquals("(1 / (table.`col` * 2))", b1.toString());
    }

    @Test
    public void withTableSubstitutedTest(){
        BinaryOpExpr b1;
        BinaryOpExpr b2 = BinaryOpExpr.from(dummyContext, ColNameExpr.from(dummyContext, "table.col"),
                ConstantExpr.from(dummyContext, 2), "*");
        b1 = BinaryOpExpr.from(dummyContext, ConstantExpr.from(dummyContext, 1), b2, "/");
        assertEquals("(1 / (newtab.`col` * 2))", b1.withTableSubstituted("newtab").toString());
    }

    @Test
    public void isaggTest1(){
        Expr dummyExprLeft = Expr.from(dummyContext, "1");
        Expr dummyExprRight = Expr.from(dummyContext, "2");
        BinaryOpExpr a = BinaryOpExpr.from(dummyContext, dummyExprLeft, dummyExprRight, "*");
        assertEquals(false, a.isagg());
    }

    @Test
    public void isaggTest2(){
        FuncExpr f = new FuncExpr(FuncExpr.FuncName.COUNT, ConstantExpr.from(dummyContext, "*"));
        BinaryOpExpr b1;
        BinaryOpExpr b2 = BinaryOpExpr.from(dummyContext, ColNameExpr.from(dummyContext, "table.col"),
                ConstantExpr.from(dummyContext, 2), "*");
        b1 = BinaryOpExpr.from(dummyContext, f, b2, "/");
        assertEquals(true, b1.isagg());
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
