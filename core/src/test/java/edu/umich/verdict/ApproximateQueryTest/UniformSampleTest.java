package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.ApproximateQueryTest.ApproximateQueryTest;
import edu.umich.verdict.exceptions.VerdictException;
import org.junit.BeforeClass;

import java.sql.SQLException;

public class UniformSampleTest extends ApproximateQueryTest {

    @BeforeClass
    public static void CreateSamples() throws VerdictException, SQLException{
        vc.executeJdbcQuery("drop samples of customer");
        vc.executeJdbcQuery("drop samples of supplier");
        vc.executeJdbcQuery("drop samples of orders");
        vc.executeJdbcQuery("create uniform sample of customer");
        vc.executeJdbcQuery("create uniform sample of supplier");
        vc.executeJdbcQuery("create uniform sample of orders");
    }

}
