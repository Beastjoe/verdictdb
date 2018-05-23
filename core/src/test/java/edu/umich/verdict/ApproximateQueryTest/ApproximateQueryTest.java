package edu.umich.verdict.ApproximateQueryTest;

import org.junit.Test;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class ApproximateQueryTest extends ApproximateQueryTestBase {

    //@Test //Feature 1
    public void FlatQuery() {
        Result result = JUnitCore.runClasses(FlatQueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 2
    public void FilterQuery() {
        Result result = JUnitCore.runClasses(FilterQueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 3
    public void Join2Query() {
        Result result = JUnitCore.runClasses(Join2QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 4
    public void Join3Query() {
        Result result = JUnitCore.runClasses(Join3QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    @Test //Feature 5
    public void GroupbyQuery() {
        Result result = JUnitCore.runClasses(GroupbyQueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 6
    public void Filter_Join2_Query() {
        Result result = JUnitCore.runClasses(Filter_Join2_Groupby_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 7
    public void Filter_Groupby_Query() {
        Result result = JUnitCore.runClasses(Filter_Join2_Groupby_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 8
    public void Join2_Groupby_Query() {
        Result result = JUnitCore.runClasses(Join2_Groupby_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 9
    public void Filter_Join2_Groupby_Query() {
        Result result = JUnitCore.runClasses(Filter_Join2_Groupby_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 10
    public void Select_Subquery_Query() {
        Result result = JUnitCore.runClasses(Select_Subquey_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 11
    public void From_Subquery_Query() {
        Result result = JUnitCore.runClasses(From_Subquery_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 12
    public void Where_Subquery_Query() {
        Result result = JUnitCore.runClasses(Where_Subquery_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

    //@Test //Feature 13
    public void Union_Query() {
        Result result = JUnitCore.runClasses(Union_QueryTest.class);
        for (Failure failure:result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

}
