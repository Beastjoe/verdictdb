package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.expr.Expr;
import edu.umich.verdict.relation.expr.OrderByExpr;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderedRelationTest extends VerdictTestBase {

    @Test
    public void getSelectElemsTest() throws VerdictException {
        String sql = "select c_nationkey, c_address from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        OrderedRelation r = (OrderedRelation) subr.orderby("c_nationkey ASC");
        assertEquals(2, r.getSelectElemList().size());
    }

    @Test
    public void getSourceTest() throws VerdictException {
        String sql = "select c_nationkey, c_address from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        OrderedRelation r = (OrderedRelation) subr.orderby("c_nationkey ASC");
        assertEquals(true, r.getSource() instanceof ProjectedRelation);
    }

    @Test
    public void toSqlTest() throws VerdictException {
        String sql = "select c_nationkey, c_address from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        OrderedRelation r = (OrderedRelation) subr.orderby("c_nationkey ASC");
        assertEquals(String.format("%s ORDER BY `c_nationkey` ASC", r.getSource().toSql()),
                r.toSql());
    }

    @Test
    public void getOrderbyTest() throws VerdictException {
        String sql = "select c_nationkey, c_address from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        OrderedRelation r = (OrderedRelation) subr.orderby("c_nationkey ASC");
        List<OrderByExpr> l = r.getOrderby();
        assertEquals("`c_nationkey` ASC", l.get(0).toString());
    }

    @Test
    public void approxTest() throws VerdictException{
        String sql = "select c_nationkey, c_address from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        OrderedRelation r = (OrderedRelation) subr.orderby("c_nationkey ASC");
        ApproxRelation approxr = r.approx();
        assertEquals(true, approxr instanceof ApproxOrderedRelation);
    }
}
