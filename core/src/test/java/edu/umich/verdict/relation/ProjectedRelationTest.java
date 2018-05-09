package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectedRelationTest extends VerdictTestBase {

    @Test
    public void getSelectElemsTest() throws VerdictException{
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ProjectedRelation r = (ProjectedRelation) ExactRelation.from(vc, sql);
        assertEquals(4, r.getSelectElems().size());
    }

    @Test
    public void getSourceTest() throws VerdictException {
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ProjectedRelation r = (ProjectedRelation) ExactRelation.from(vc, sql);
        assertEquals("test.customer", ((SingleRelation)r.getSource()).getTableName().toString());
    }

    @Test
    public void toSqlTest() throws VerdictException {
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ProjectedRelation r = (ProjectedRelation) ExactRelation.from(vc, sql);
        assertEquals(String.format("SELECT %s, %s, %s, %s FROM %s AS %s", r.getSelectElems().get(0).toString(),
                r.getSelectElems().get(1).toString(), r.getSelectElems().get(2).toString(), r.getSelectElems().get(3).toString(),
                ((SingleRelation)r.getSource()).getTableName(), ((SingleRelation)r.getSource()).getAlias()), r.toSql());
    }

    @Test
    public void approxTest() throws VerdictException{
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ProjectedRelation r = (ProjectedRelation) ExactRelation.from(vc, sql);
        ApproxRelation subr = r.approx();
        assertEquals(true, subr instanceof ApproxProjectedRelation);
    }
}
