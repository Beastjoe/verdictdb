package edu.umich.verdict.relation.condition;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AndCondTest extends VerdictTestBase{

    private static VerdictContext dummyContext = null;

    @Test
    public void fromTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        assertEquals("t.`o_orderkey` = t.`vpart`", a.getLeft().toString());
        assertEquals("`o_orderkey` > 62340", a.getRight().toString());
    }

    @Test
    public void withTableSubstituteTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        a.withTableSubstituted("new_t");
        assertEquals("new_t.`o_orderkey` = new_t.`vpart`", a.getLeft().toString());
    }

    @Test
    public void removeTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        Cond c = Cond.from(dummyContext,"o_orderkey>62340");
        assertEquals("t.`o_orderkey` = t.`vpart`", a.remove(c));
    }

    @Test
    public void equalsTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        AndCond b = AndCond.from(Cond.from(dummyContext, "t.vpart=t.o_orderkey"), Cond.from(dummyContext, "o_orderkey>62340"));
        assertEquals(false, a.equals(b));
    }

}
