package edu.umich.verdict.relation;

import edu.umich.verdict.VerdictTestBase;
import static org.junit.Assert.assertEquals;

import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.expr.ColNameExpr;
import org.junit.Test;

import java.util.List;

public class SingleRelationTest extends VerdictTestBase {

    @Test
    public void getTableNameTest(){
        TableUniqueName tablename = new TableUniqueName("schema", "table");
        SingleRelation singleRelation = SingleRelation.from(vc, tablename);
        assertEquals("schema.table", singleRelation.getTableName().toString());
    }

    @Test
    public void toSqlTest(){
        TableUniqueName tablename = new TableUniqueName("schema", "table");
        SingleRelation singleRelation = SingleRelation.from(vc, tablename);
        assertEquals(String.format("SELECT * FROM %s AS %s", singleRelation.getTableName(), singleRelation.alias), singleRelation.toSql());
    }

    @Test
    public void approx() throws VerdictException{
        TableUniqueName tablename = new TableUniqueName("schema", "table");
        SingleRelation singleRelation = SingleRelation.from(vc, tablename);
        ApproxSingleRelation r  = (ApproxSingleRelation) singleRelation.approx();
        assertEquals(true, r.isApproximate());
    }

    @Test
    public void accumulateSamplingProbColumnsTest() throws VerdictException{
        String sql = "select c_nationkey, c_name, c_phone, c_address from customer";
        ExactRelation r = ExactRelation.from(vc, sql);
        SingleRelation singleRelation = (SingleRelation) ((ProjectedRelation)r).getSource();
        List<ColNameExpr> col = singleRelation.accumulateSamplingProbColumns();
        assertEquals(0, col.size());
    }
}
