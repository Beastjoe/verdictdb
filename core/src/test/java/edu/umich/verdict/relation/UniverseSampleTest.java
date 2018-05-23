package edu.umich.verdict.relation;

import edu.umich.verdict.ApproximateQueryTest.ApproximateQueryTest;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.BeforeClass;

import java.sql.SQLException;

public class UniverseSampleTest extends ApproximateQueryTest {

    @BeforeClass
    public static void createSamples() throws VerdictException, SQLException{
        vc.executeJdbcQuery("drop samples of customer");
        vc.executeJdbcQuery("drop samples of supplier");
        vc.executeJdbcQuery("drop samples of orders");
        vc.executeJdbcQuery("create uniform sample of customer");
        vc.executeJdbcQuery("create uniform sample of supplier");
        vc.executeJdbcQuery("create uniform sample of orders");
        vc.executeJdbcQuery("Create universe sample of customer on c_nationkey");
        vc.executeJdbcQuery("Create universe sample of supplier on s_nationkey");
        vc.executeJdbcQuery("Create universe sample of orders on o_orderstatus");
        vc.executeJdbcQuery("Create universe sample of orders on o_custkey");
        vc.executeJdbcQuery("Create universe sample of orders on o_shippriority");
        vc.executeJdbcQuery("Create universe sample of orders on o_orderpriority");
    }
}
