package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.expr.SelectElem;
import edu.umich.verdict.relation.expr.SubqueryExpr;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExactRelationTest extends VerdictTestBase {

    @Test
    public void getElemList(){
        String sql = "select c_nationkey, (select count(*) from customer) from customer";
        ExactRelation r = ExactRelation.from(vc, sql);
        assertEquals(2, r.getSelectElemList().size());
        assertEquals(true, r.getSelectElemList().get(1).getExpr() instanceof SubqueryExpr);
    }

    @Test
    public void selectTest(){
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r = ExactRelation.from(vc, sql);
        List<String> sublist = new ArrayList<String>();
        sublist.add("c_nationkey");
        sublist.add("c_name");
        ExactRelation subr = r.select(sublist);
        assertEquals(2, subr.getSelectElemList().size());
        assertEquals(((ProjectedRelation)subr).getSource(), (ProjectedRelation)r);
    }

    @Test
    public void filterTest() throws VerdictException {
        String cond = "`c_nationkey` > 10";
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r = ExactRelation.from(vc, sql);
        ExactRelation subr = r.filter(cond);
        assertEquals(((FilteredRelation)subr).getSource(), (ProjectedRelation)r);
        assertEquals(cond, ((FilteredRelation)subr).getFilter().toString());
        assertEquals(true, subr instanceof FilteredRelation);
    }

    @Test
    public void countTest() throws VerdictException {
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r = ExactRelation.from(vc, sql);
        ExactRelation subr = r.count();
        assertEquals(((AggregatedRelation)subr).getSource(), r);
        assertEquals(true, ((AggregatedRelation)subr).getElemList().get(0).isagg());
    }

    @Test
    public void groupbyTest() throws VerdictException {
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r = ExactRelation.from(vc, sql);
        List<String> grouplist = new ArrayList<String>();
        grouplist.add("`c_nationkey`");
        grouplist.add("`c_name`");
        GroupedRelation subr = r.groupby(grouplist);
        assertEquals(subr.getSource(), r);
        assertEquals(grouplist.get(0), subr.getGroupby().get(0).toString());
        assertEquals(grouplist.get(1), subr.getGroupby().get(1).toString());
    }

    @Test
    public void joinTest() throws VerdictException {
        String sql1 = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r1 = ExactRelation.from(vc, sql1);
        String sql2 = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r2 = ExactRelation.from(vc, sql2);
        String cond = "`c_nationkey` > 5";
        JoinedRelation r = r1.join(r2, cond);
        assertEquals(r.getLeftSource(), r1);
        assertEquals(r.getRightSource(), r2);
        assertEquals(cond, r.getJoinCond().get(0).toString());
    }

    @Test
    public void toSqlTest(){
        ExactRelation r = ExactRelation.from(vc, "select c_nationkey, count(*) from customer group by c_nationkey");
        GroupedRelation s = (GroupedRelation) ((AggregatedRelation)r).getSource();
        SingleRelation ss  = (SingleRelation) s.getSource();
        List<SelectElem> l = ((AggregatedRelation) r).getElemList();
        assertEquals(String.format("SELECT %s, %s FROM %s AS %s GROUP BY %s", l.get(0).toString(), l.get(1).toString(), ss.getTableName(), ss.getAlias(), s.groupby.get(0).toString()),
                r.toSql());
    }

}
