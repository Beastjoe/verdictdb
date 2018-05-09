package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.expr.Expr;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroupedRelationTest extends VerdictTestBase {

    @Test
    public void getSelectElemsTest() throws VerdictException {
        String sql = "select c_nationkey, count(*) from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        GroupedRelation r = subr.groupby("c_nationkey");
        assertEquals(2, r.getSelectElemList().size());
    }

    @Test
    public void getSourceTest() throws VerdictException {
        String sql = "select c_nationkey, count(*) from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        GroupedRelation r = subr.groupby("c_nationkey");
        assertEquals(true, r.getSource() instanceof AggregatedRelation);
    }

    @Test
    public void getGroupbyTest() throws VerdictException {
        String sql = "select c_nationkey, count(*) from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        GroupedRelation r = subr.groupby("c_nationkey");
        List<Expr> l = r.getGroupby();
        assertEquals(1, l.size());
        assertEquals("`c_nationkey`",l.get(0).toString());
    }
    @Test
    public void toSqlTest() throws VerdictException {
        String sql = "select c_nationkey, count(*) from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        GroupedRelation r = subr.groupby("c_nationkey");
        assertEquals(String.format("SELECT * FROM (%s) AS %s GROUP BY %s", r.getSource().toSql(), r.getAlias(), r.getGroupby().get(0).toString()),
                r.toSql());
    }

    @Test
    public void approxTest() throws VerdictException{
        String sql = "select c_nationkey, count(*) from customer";
        ExactRelation subr = ExactRelation.from(vc, sql);
        GroupedRelation r = subr.groupby("c_nationkey");
        ApproxRelation approxr = r.approx();
        assertEquals(true, approxr instanceof ApproxProjectedRelation);
    }
}
