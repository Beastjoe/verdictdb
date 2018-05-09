package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.condition.Cond;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JoinedRelationTest extends VerdictTestBase {

    @Test
    public void getSelectElemsTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer");
        ExactRelation r2 = ExactRelation.from(vc, "select n_nationkey from nation");
        Cond c = Cond.from(vc, "customer.`c_nationkey` = nation.`n_nationkey`");
        JoinedRelation r = JoinedRelation.from(vc, r1, r2, c);
        r.setJoinType(JoinedRelation.JoinType.LEFT_OUTER);
        assertEquals(2, r.getSelectElemList().size());
        assertEquals(2, r.getSelectElemList().size()); //cannot repeat?
    }

    @Test
    public void getSourceTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer");
        ExactRelation r2 = ExactRelation.from(vc, "select n_nationkey from nation");
        Cond c = Cond.from(vc, "customer.`c_nationkey` = nation.`n_nationkey`");
        JoinedRelation r = JoinedRelation.from(vc, r1, r2, c);
        r.setJoinType(JoinedRelation.JoinType.LEFT_OUTER);
        assertEquals(true, r.getLeftSource() instanceof ProjectedRelation);
        assertEquals(true, r.getRightSource() instanceof ProjectedRelation);
    }

    @Test
    public void toSqlTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer");
        ExactRelation r2 = ExactRelation.from(vc, "select n_nationkey from nation");
        Cond c = Cond.from(vc, "customer.`c_nationkey` = nation.`n_nationkey`");
        JoinedRelation r = JoinedRelation.from(vc, r1, r2, c);
        r.setJoinType(JoinedRelation.JoinType.LEFT_OUTER);
        assertEquals(String.format("SELECT * FROM (%s) AS %s LEFT OUTER JOIN (%s) AS %s ON %s", r.getLeftSource().toSql(),
                r.getLeftSource().getSourceName(), r.getRightSource().toSql(), r.getRightSource().getSourceName(), c.toString()),
                r.toSql());
    }

    @Test
    public void getTypeTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer");
        ExactRelation r2 = ExactRelation.from(vc, "select n_nationkey from nation");
        Cond c = Cond.from(vc, "customer.`c_nationkey` = nation.`n_nationkey`");
        JoinedRelation r = JoinedRelation.from(vc, r1, r2, c);
        r.setJoinType(JoinedRelation.JoinType.LEFT_OUTER);
        assertEquals(JoinedRelation.JoinType.LEFT_OUTER, r.getJoinType());
    }

    @Test
    public void getCondTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer");
        ExactRelation r2 = ExactRelation.from(vc, "select n_nationkey from nation");
        Cond c = Cond.from(vc, "customer.`c_nationkey` = nation.`n_nationkey`");
        JoinedRelation r = JoinedRelation.from(vc, r1, r2, c);
        r.setJoinType(JoinedRelation.JoinType.LEFT_OUTER);
        assertEquals(c, r.getJoinCond().get(0));
    }

    @Test
    public void approxTest() throws VerdictException{
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer");
        ExactRelation r2 = ExactRelation.from(vc, "select n_nationkey from nation");
        Cond c = Cond.from(vc, "customer.`c_nationkey` = nation.`n_nationkey`");
        JoinedRelation r = JoinedRelation.from(vc, r1, r2, c);
        r.setJoinType(JoinedRelation.JoinType.LEFT_OUTER);
        ApproxRelation approxr = r.approx();
        assertEquals(true, approxr instanceof ApproxJoinedRelation);
    }
}
