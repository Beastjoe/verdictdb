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
    public void removeTest1(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        Cond c = Cond.from(dummyContext,"o_orderkey>62340");
        assertEquals("t.`o_orderkey` = t.`vpart`", a.remove(c).toString());
    }

    @Test
    public void removeTest2(){
        Cond c1 = Cond.from(dummyContext, "t1.o_orderkey=t1.vpart");
        OrCond a1 = OrCond.from(c1, Cond.from(dummyContext, "o_orderkey>62340"));
        AndCond a2 = AndCond.from(Cond.from(dummyContext, "t2.o_orderkey<>t1.vpart"), a1);
        assertEquals("(t2.`o_orderkey` <> t1.`vpart`) AND ((t1.`o_orderkey` = t1.`vpart`) OR (`o_orderkey` > 62340))",
                a2.remove(c1).toString());
    }

    @Test
    public void toStringTest(){
        Cond c1 = Cond.from(dummyContext, "t1.o_orderkey=t1.vpart");
        OrCond a1 = OrCond.from(c1, Cond.from(dummyContext, "o_orderkey>62340"));
        AndCond a2 = AndCond.from(Cond.from(dummyContext, "t2.o_orderkey<>t1.vpart"), a1);
        assertEquals("(t2.`o_orderkey` <> t1.`vpart`) AND ((t1.`o_orderkey` = t1.`vpart`) OR (`o_orderkey` > 62340))",
                a2.toString());
    }

    @Test
    public void equalsTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        AndCond b = AndCond.from(Cond.from(dummyContext, "t.vpart=t.o_orderkey"), Cond.from(dummyContext, "o_orderkey>62340"));
        assertEquals(false, a.equals(b));
    }

}
