package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void fromTest() {
        OrCond a = OrCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        assertEquals("t.`o_orderkey` = t.`vpart`", a.getLeft().toString());
        assertEquals("`o_orderkey` > 62340", a.getRight().toString());
    }

    @Test
    public void withTableSubstituteTest() {
        OrCond a = OrCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        a.withTableSubstituted("new_t");
        assertEquals("new_t.`o_orderkey` = new_t.`vpart`", a.getLeft().toString());
    }

    @Test
    public void removeTest() {
        OrCond a = OrCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        Cond c = Cond.from(dummyContext, "o_orderkey>62340");
        assertEquals("t.`o_orderkey` = t.`vpart`", a.remove(c));
    }

    @Test
    public void equalsTest() {
        OrCond a = OrCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        OrCond b = OrCond.from(Cond.from(dummyContext, "t.vpart=t.o_orderkey"), Cond.from(dummyContext, "o_orderkey>62340"));
        assertEquals(false, a.equals(b));
    }

}