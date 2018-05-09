package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.expr.Expr;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LimitedRelationTest extends VerdictTestBase {

    @Test
    public void getSelectElemsTest() throws VerdictException {
        String sql = "select c_nationkey from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        LimitedRelation r = (LimitedRelation) subr.limit(100);
        assertEquals(1, r.getSelectElemList().size());
    }

    @Test
    public void getSourceTest() throws VerdictException {
        String sql = "select c_nationkey from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        LimitedRelation r = (LimitedRelation) subr.limit(100);
        assertEquals(true, r.getSource() instanceof ProjectedRelation);
    }

    @Test
    public void toSqlTest() throws VerdictException {
        String sql = "select c_nationkey from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        LimitedRelation r = (LimitedRelation) subr.limit(100);
        assertEquals(String.format("%s LIMIT 100", r.getSource().toSql()),
                r.toSql());
    }

    @Test
    public void approxTest() throws VerdictException{
        String sql = "select c_nationkey from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        LimitedRelation r = (LimitedRelation) subr.limit(100);
        ApproxRelation approxr = r.approx();
        assertEquals(true, approxr instanceof ApproxLimitedRelation);
    }
}
