package edu.umich.verdict.relation.condition;

import com.sun.tools.corba.se.idl.constExpr.Not;
import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NotCondTest extends VerdictTestBase {

    private static VerdictContext dummyContext = null;

    @Test
    public void toStringTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        NotCond n = NotCond.from(a);
        assertEquals("NOT ((t.`o_orderkey` = t.`vpart`) AND (`o_orderkey` > 62340))", n.toString());
    }

    @Test
    public void equalsTest(){
        AndCond a = AndCond.from(Cond.from(dummyContext, "t.o_orderkey=t.vpart"), Cond.from(dummyContext, "o_orderkey>62340"));
        NotCond n1 = NotCond.from(a);
        NotCond n2 = NotCond.from(n1);
        assertEquals(false, n1.equals(n2));
    }

}
