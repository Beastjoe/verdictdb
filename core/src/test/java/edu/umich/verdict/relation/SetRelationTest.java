package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SetRelationTest extends VerdictTestBase {

    @Test
    public void getSelectElemsTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer1");
        ExactRelation r2 = ExactRelation.from(vc, "select c_nationkey from customer2");
        SetRelation r = new SetRelation(vc, r1, r2, SetRelation.SetType.UNION);
        assertEquals(2, r.getSelectElemList().size());
        assertEquals(2, r.getSelectElemList().size()); //cannot repeat?
    }

    @Test
    public void getSourceTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer1");
        ExactRelation r2 = ExactRelation.from(vc, "select c_nationkey from customer2");
        SetRelation r = new SetRelation(vc, r1, r2, SetRelation.SetType.UNION);
        assertEquals(true, r.getLeftSource() instanceof ProjectedRelation);
        assertEquals(true, r.getRightSource() instanceof ProjectedRelation);
    }

    @Test
    public void toSqlTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer1");
        ExactRelation r2 = ExactRelation.from(vc, "select c_nationkey from customer2");
        SetRelation r = new SetRelation(vc, r1, r2, SetRelation.SetType.UNION);
        assertEquals(String.format("%s UNION %s", r.getLeftSource().toSql(), r.getRightSource().toSql()),
                r.toSql());
    }

    @Test
    public void getTypeTest() throws VerdictException {
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer1");
        ExactRelation r2 = ExactRelation.from(vc, "select c_nationkey from customer2");
        SetRelation r = new SetRelation(vc, r1, r2, SetRelation.SetType.UNION);
        assertEquals(SetRelation.SetType.UNION, r.getSetType());
    }

    @Test
    public void approxTest() throws VerdictException{
        ExactRelation r1 = ExactRelation.from(vc, "select c_nationkey from customer1");
        ExactRelation r2 = ExactRelation.from(vc, "select c_nationkey from customer2");
        SetRelation r = new SetRelation(vc, r1, r2, SetRelation.SetType.UNION);
        ApproxRelation approxr = r.approx();
        assertEquals(true, approxr instanceof ApproxSetRelation);
    }
}
