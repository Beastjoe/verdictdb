package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.relation.condition.AndCond;
import edu.umich.verdict.relation.condition.Cond;
import org.junit.Test;


public class FilteredRelationTest extends VerdictTestBase {

    @Test
    public void getFilterTest() throws VerdictException{
        String sql = "select c_nationkey from customer";
        AndCond a = AndCond.from(Cond.from(vc, "t.o_orderkey=t.vpart"), Cond.from(vc, "o_orderkey>62340"));
        FilteredRelation r = (FilteredRelation) ExactRelation.from(vc, sql).filter(a);
        assertEquals(a, r.getFilter());
    }

    @Test
    public void getSourceTest() throws VerdictException {
        String sql = "select c_nationkey from customer";
        AndCond a = AndCond.from(Cond.from(vc, "t.o_orderkey=t.vpart"), Cond.from(vc, "o_orderkey>62340"));
        FilteredRelation r = (FilteredRelation) ExactRelation.from(vc, sql).filter(a);
        assertEquals(true, r.getSource() instanceof ProjectedRelation);
    }

    @Test
    public void toSqlTest() throws VerdictException {
        String sql = "select c_nationkey from customer";
        ExactRelation exactRelation = ExactRelation.from(vc, sql);
        AndCond a = AndCond.from(Cond.from(vc, "t.`o_orderkey`=t.`vpart`"), Cond.from(vc, "`o_orderkey`>62340"));
        FilteredRelation r = (FilteredRelation) exactRelation.filter(a);
        assertEquals(String.format("SELECT * FROM (%s) AS %s WHERE %s", exactRelation.toSql(), r.getAlias(), a.toSql()), r.toSql());
    }

    @Test
    public void approxTest() throws VerdictException{
        String sql = "select c_nationkey from customer";
        AndCond a = AndCond.from(Cond.from(vc, "t.`o_orderkey`=t.`vpart`"), Cond.from(vc, "`o_orderkey`>62340"));
        FilteredRelation r = (FilteredRelation) ExactRelation.from(vc, sql).filter(a);
        ApproxRelation subr = r.approx();
        assertEquals(true, subr instanceof ApproxFilteredRelation);
    }

}
