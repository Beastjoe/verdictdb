package edu.umich.verdict.ApproximateQueryTest;

import edu.umich.verdict.VerdictTestBase;
import edu.umich.verdict.exceptions.VerdictException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApproximateQueryTestBase extends VerdictTestBase {
    private double tolerance = 0.1;

    public void SetTolerance(double t) {
        tolerance = t;
    }

    protected void Compare(ResultSet sampleRes, ResultSet originRes, String type) throws VerdictException, SQLException {
        if (type == "long") {
            long sample = 0, origin = 0;
            while (sampleRes.next()) {
                sample = sampleRes.getLong(1);
            }
            while (originRes.next()) {
                origin = originRes.getLong(1);
            }
            assertEquals(true, origin * (1 - tolerance) <= sample && sample <= origin * (1 + tolerance));
        }
        else if (type == "double") {
            double sample = 0, origin = 0;
            while (sampleRes.next()) {
                sample = sampleRes.getDouble(1);
            }
            while (originRes.next()) {
                origin = originRes.getDouble(1);
            }
            assertEquals(true, origin * (1 - tolerance) <= sample && sample <= origin * (1 + tolerance));
        }
    }

    protected void Compare(List sampleRes, List originRes, List sampleGroupby, List originGroupby){
        // First compare the size of two resultSet
        assertEquals(true, originRes.size()*(1-tolerance)<=sampleRes.size()
                && sampleRes.size() <= originRes.size()*(1+tolerance));
        int idx = 0;
        //Then Compare the value
        for (int i=0;i<originRes.size();i++){
            if (originGroupby.get(i).equals(sampleGroupby.get(idx))){
                if (double.class.isInstance(originRes.get(i))) {
                    assertEquals(true, (double) originRes.get(i) * (1 - tolerance) <= (double) sampleRes.get(idx)
                            && (double) sampleRes.get(idx) <= (double) originRes.get(i) * (1 + tolerance));
                } else {
                    assertEquals(true, (long) originRes.get(i) * (1 - tolerance) <= (long) sampleRes.get(idx)
                            && (long) sampleRes.get(idx) <= (long) originRes.get(i) * (1 + tolerance));

                }
                idx++;
            }
        }
        // If group by in samples does not occur in origin result, assert false.
        if (idx!=sampleRes.size()) {
            assertEquals(false, true);
        }
    }
}
