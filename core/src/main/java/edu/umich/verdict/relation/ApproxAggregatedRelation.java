package edu.umich.verdict.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.relation.condition.Cond;
import edu.umich.verdict.relation.condition.IsCond;
import edu.umich.verdict.relation.condition.NullCond;
import edu.umich.verdict.relation.expr.BinaryOpExpr;
import edu.umich.verdict.relation.expr.CaseExpr;
import edu.umich.verdict.relation.expr.ColNameExpr;
import edu.umich.verdict.relation.expr.ConstantExpr;
import edu.umich.verdict.relation.expr.Expr;
import edu.umich.verdict.relation.expr.ExprModifier;
import edu.umich.verdict.relation.expr.FuncExpr;
import edu.umich.verdict.relation.expr.OverClause;
import edu.umich.verdict.relation.expr.SelectElem;
import edu.umich.verdict.util.VerdictLogger;

public class ApproxAggregatedRelation extends ApproxRelation {

    private ApproxRelation source;

    private List<Expr> aggs;

    private boolean includeGroupsInToSql = true;

    public ApproxAggregatedRelation(VerdictContext vc, ApproxRelation source, List<Expr> aggs) {
        super(vc);
        this.source = source;
        this.aggs = aggs;
    }

    public ApproxRelation getSource() {
        return source;
    }

    public List<Expr> getAggList() {
        return aggs;
    }

    public void setIncludeGroupsInToSql(boolean o) {
        includeGroupsInToSql = o;
    }

    @Override
    public ExactRelation rewriteForPointEstimate() {
        ExactRelation newSource = source.rewriteForPointEstimate();

        List<Expr> scaled = new ArrayList<Expr>();
        //		List<ColNameExpr> samplingProbColumns = newSource.accumulateSamplingProbColumns();
        for (Expr e : aggs) {
            scaled.add(transformForSingleFunction(e));
        }
        ExactRelation r = new AggregatedRelation(vc, newSource, scaled);
        r.setAlias(getAlias());
        ((AggregatedRelation) r).setIncludeGroupsInToSql(includeGroupsInToSql);
        return r;
    }

    @Override
    public ExactRelation rewriteWithSubsampledErrorBounds() {
        // direct call to this class is not expected; however, we still support it by creating another
        // ProjectedRelation on this and by calling the method.
        List<SelectElem> elems = new ArrayList<SelectElem>();
        if (source instanceof ApproxGroupedRelation) {
            List<Expr> groupby = ((ApproxGroupedRelation) source).getGroupby();
            for (Expr group : groupby) {
                elems.add(new SelectElem(vc, group));
            }
        }
        for (Expr agg : aggs) {
            elems.add(new SelectElem(vc, agg));
        }
        ApproxRelation a = new ApproxProjectedRelation(vc, this, elems);
        ExactRelation r = a.rewriteWithSubsampledErrorBounds();
        return r;
    }

    /**
     * This relation must include partition numbers, and the answers must be scaled properly. Note that {@link ApproxRelation#rewriteWithSubsampledErrorBounds()}
     * is used only for the statement including final error bounds; all internal manipulations must be performed by
     * this method.
     * 
     * The rewritten relation transforms original aggregate elements as follows. Every aggregate element is replaced with
     * two aggregate elements. One is for mean estimate and the other is for error estimate.
     * 
     * The rewritten relation includes an extra aggregate element: count(*). This is to compute the partition sizes. These
     * partition sizes can be used by an upstream (or parent) relation for computing the final mean estimate. (note that
     * computing weighted average provides higher accuracy compared to unweighted average.)
     * @return
     */
    @Override
    protected ExactRelation rewriteWithPartition() {
        ExactRelation newSource = partitionedSource();

        // select list elements are scaled considering both sampling probabilities and partitioning for subsampling.
        List<Expr> scaledExpr = new ArrayList<Expr>();
        //		List<ColNameExpr> samplingProbCols = newSource.accumulateSamplingProbColumns();
        List<Expr> groupby = new ArrayList<Expr>();
        if (source instanceof ApproxGroupedRelation) {
            groupby.addAll(((ApproxGroupedRelation) source).groupbyWithTablesSubstituted());
        }

        final Map<TableUniqueName, String> sub = source.tableSubstitution();
        for (Expr e : aggs) {
            // for mean estimation
            Expr scaled = transformForSingleFunctionWithPartitionSize(e, groupby, newSource.partitionColumn(), sub, false);
            scaledExpr.add(scaled);

            //			// for error estimation
            //			Expr scaledErr = transformForSingleFunctionWithPartitionSize(e, samplingProbCols, groupby, newSource.partitionColumn(), sub, true);
            //			scaledExpr.add(scaledErr);
        }
        // to compute the partition size
        scaledExpr.add(FuncExpr.count());
        ExactRelation r = new AggregatedRelation(vc, newSource, scaledExpr);

        return r;
    }

    private ExactRelation partitionedSource() {
        if (source instanceof ApproxGroupedRelation) {
            return source.rewriteWithPartition();
        } else {
            return (new ApproxGroupedRelation(vc, source, Arrays.<Expr>asList())).rewriteWithPartition();
        }
    }

    @Override
    protected Map<TableUniqueName, String> tableSubstitution() {
        return source.tableSubstitution();
    }

    @Override
    protected List<Expr> samplingProbabilityExprsFor(FuncExpr f) {
        if (f.getFuncName().equals(FuncExpr.FuncName.COUNT_DISTINCT)) {
            if (sampleType().equals("universe")) {
                return Arrays.<Expr>asList(ConstantExpr.from(vc, samplingProbability()));
            } else if (sampleType().equals("stratified")) {
                return Arrays.<Expr>asList();
            } else if (sampleType().equals("nosample")) {
                return Arrays.<Expr>asList();
            } else {
                VerdictLogger.warn(this, String.format("%s sample should not be used for count-distinct.", sampleType()));
                return Arrays.<Expr>asList(ConstantExpr.from(vc, 1.0));
            }
        } else {	// SUM, COUNT, AVG
            if (!sampleType().equals("nosample")) {
                String samplingProbCol = samplingProbabilityColumnName();
                return Arrays.<Expr>asList(new ColNameExpr(vc, samplingProbCol, alias));
            } else {
                return Arrays.<Expr>asList();
            }
        }
    }

    private Expr transformForSingleFunctionWithPartitionSize(
            Expr f,
            final List<Expr> groupby,
            final ColNameExpr partitionCol,
            final Map<TableUniqueName, String> tablesNamesSub,
            final boolean forErrorEst) {

        //		final List<Expr> groupbyExpr = new ArrayList<Expr>();
        //		for (Expr c : groupby) {
        //			groupbyExpr.add((Expr) c);
        //		}

        ExprModifier v = new ExprModifier(vc) {
            public Expr call(Expr expr) {
                if (expr instanceof FuncExpr) {
                    // Take two different approaches:
                    // 1. stratified samples: use the verdict_sampling_prob column (in each tuple).
                    // 2. other samples: use either the uniform sampling probability or the ratio between the sample
                    //    size and the original table size.

                    FuncExpr f = (FuncExpr) expr;
                    FuncExpr s = (FuncExpr) exprWithTableNamesSubstituted(expr, tablesNamesSub);
                    List<Expr> samplingProbExprs = source.samplingProbabilityExprsFor(f);

                    if (f.getFuncName().equals(FuncExpr.FuncName.COUNT)) {
                        Expr est = FuncExpr.sum(scaleForSampling(samplingProbExprs));
                        est = scaleWithPartitionSize(est, groupby, partitionCol, forErrorEst);
                        //						est = FuncExpr.round(est);
                        return est;
                    }
                    else if (f.getFuncName().equals(FuncExpr.FuncName.COUNT_DISTINCT)) {
                        String dbname = vc.getDbms().getName();
                        Expr scale = scaleForSampling(samplingProbExprs);
                        Expr est = null;
                        est = new FuncExpr(FuncExpr.FuncName.COUNT_DISTINCT, s.getUnaryExpr());

                        //						if (dbname.equals("impala")) {
                        //							est = new FuncExpr(FuncExpr.FuncName.IMPALA_APPROX_COUNT_DISTINCT, s.getUnaryExpr());
                        //						} else {
                        //							
                        //						}

                        est = BinaryOpExpr.from(vc, est, scale, "*");
                        if (source.sampleType().equals("universe")) {
                            est = scaleWithPartitionSize(est, groupby, partitionCol, forErrorEst);
                        }
                        //						est = FuncExpr.round(est);
                        return est;
                    }
                    else if (f.getFuncName().equals(FuncExpr.FuncName.SUM)) {
                        Expr est = scaleForSampling(samplingProbExprs);
                        est = FuncExpr.sum(BinaryOpExpr.from(vc, s.getUnaryExpr(), est, "*"));
                        est = scaleWithPartitionSize(est, groupby, partitionCol, forErrorEst);
                        return est;
                    }
                    else if (f.getFuncName().equals(FuncExpr.FuncName.AVG)) {
                        Expr scale = scaleForSampling(samplingProbExprs);
                        Expr sumEst = FuncExpr.sum(BinaryOpExpr.from(vc, s.getUnaryExpr(), scale, "*"));
                        // this count-est filters out the null expressions.
                        Expr countEst = countNotNull(s.getUnaryExpr(), scale);
                        return BinaryOpExpr.from(vc, sumEst, countEst, "/");
                    }
                    else {		// expected not to be visited
                        return s;
                    }
                } else {
                    return expr;
                }
            }
        };

        return v.visit(f);
    }

    private Expr scaleForSampling(List<Expr> samplingProbCols) {
        Expr scale = ConstantExpr.from(vc, 1.0);
        for (Expr c : samplingProbCols) {
            scale = BinaryOpExpr.from(vc, scale, c, "/");
        }
        return scale;
    }

    private Expr scaleWithPartitionSize(Expr expr, List<Expr> groupby, ColNameExpr partitionCol, boolean forErrorEst) {
        //		Expr scaled = BinaryOpExpr.from(expr, FuncExpr.count(), "/");

        Expr scaled = null;

        if (!forErrorEst) {
            if (expr.isCountDistinct()) {
                // scale by the partition count. the ratio between average partition size and the sum of them should be
                // almost same as the inverse of the partition count.
                scaled = BinaryOpExpr.from(vc, expr, new FuncExpr(FuncExpr.FuncName.AVG, FuncExpr.count(), new OverClause(groupby)), "/");
                scaled = BinaryOpExpr.from(vc, scaled, new FuncExpr(FuncExpr.FuncName.SUM, FuncExpr.count(), new OverClause(groupby)), "*");	
            } else {
                scaled = BinaryOpExpr.from(vc, expr, FuncExpr.count(), "/");
                scaled = BinaryOpExpr.from(vc, scaled, new FuncExpr(FuncExpr.FuncName.SUM, FuncExpr.count(), new OverClause(groupby)), "*");
            }
        } else {
            // for error estimations, we do not exactly scale with the partition size ratios.
            scaled = BinaryOpExpr.from(vc, expr, new FuncExpr(FuncExpr.FuncName.AVG, FuncExpr.count(), new OverClause(groupby)), "/");
            scaled = BinaryOpExpr.from(vc, scaled, new FuncExpr(FuncExpr.FuncName.SUM, FuncExpr.count(), new OverClause(groupby)), "*");
        }

        return scaled;
    }

    private Expr transformForSingleFunction(Expr f) {
        final Map<TableUniqueName, String> sub = source.tableSubstitution();

        ExprModifier v = new ExprModifier(vc) {
            public Expr call(Expr expr) {
                if (expr instanceof FuncExpr) {
                    // Take two different approaches:
                    // 1. stratified samples: use the verdict_sampling_prob column (in each tuple).
                    // 2. other samples: use either the uniform sampling probability or the ratio between the sample
                    //    size and the original table size.

                    FuncExpr f = (FuncExpr) expr;
                    FuncExpr s = (FuncExpr) exprWithTableNamesSubstituted(expr, sub);
                    List<Expr> samplingProbExprs = source.samplingProbabilityExprsFor(f);

                    if (f.getFuncName().equals(FuncExpr.FuncName.COUNT)) {
                        Expr scale = scaleForSampling(samplingProbExprs);
                        Expr est = FuncExpr.sum(scale);
                        return FuncExpr.round(est);
                    }
                    else if (f.getFuncName().equals(FuncExpr.FuncName.COUNT_DISTINCT)) {
                        String dbname = vc.getDbms().getName();
                        Expr scale = scaleForSampling(samplingProbExprs);
                        if (dbname.equals("impala")) {
                            return FuncExpr.round(
                                    BinaryOpExpr.from(vc, new FuncExpr(
                                            FuncExpr.FuncName.IMPALA_APPROX_COUNT_DISTINCT, s.getUnaryExpr()),
                                            scale, "*"));
                        } else {
                            return FuncExpr.round(BinaryOpExpr.from(vc, s, scale, "*"));
                        }
                    }
                    else if (f.getFuncName().equals(FuncExpr.FuncName.SUM)) {
                        Expr scale = scaleForSampling(samplingProbExprs);
                        Expr est = FuncExpr.sum(BinaryOpExpr.from(vc, s.getUnaryExpr(), scale, "*"));
                        return est;
                    }
                    else if (f.getFuncName().equals(FuncExpr.FuncName.AVG)) {
                        Expr scale = scaleForSampling(samplingProbExprs);
                        Expr sumEst = FuncExpr.sum(BinaryOpExpr.from(vc, s.getUnaryExpr(), scale, "*"));
                        // this count-est filters out the null expressions.
                        Expr countEst = countNotNull(s.getUnaryExpr(), scale);
                        return BinaryOpExpr.from(vc, sumEst, countEst, "/");
                    }
                    else {		// expected not to be visited
                        return s;
                    }
                } else {
                    return expr;
                }
            }
        };

        return v.visit(f);
    }

    private FuncExpr countNotNull(Expr nullcheck, Expr expr) {
        return FuncExpr.sum(
                new CaseExpr(vc, Arrays.<Cond>asList(new IsCond(nullcheck, new NullCond())),
                        Arrays.<Expr>asList(ConstantExpr.from(vc, "0"), expr)));
    }

    @Override
    public String sampleType() {
        String sampleType = source.sampleType();

        if (source instanceof ApproxGroupedRelation) {
            List<Expr> groupby = ((ApproxGroupedRelation) source).getGroupby();
            List<String> strGroupby = new ArrayList<String>();
            for (Expr expr : groupby) {
                if (expr instanceof ColNameExpr) {
                    strGroupby.add(((ColNameExpr) expr).getCol());
                }
            }

            List<String> sampleColumns = source.sampleColumns();
            if (sampleType.equals("universe") && strGroupby.equals(sampleColumns)) {
                return "universe";
            } else if (sampleType.equals("stratified") && strGroupby.equals(sampleColumns)) {
                return "stratified";
            }

            return "grouped-" + sampleType;
        } else {
            return "nosample";
        }
    }

    @Override
    public double cost() {
        return source.cost();
    }

    @Override
    protected List<String> sampleColumns() {
        return source.sampleColumns();
    }

    @Override
    protected String toStringWithIndent(String indent) {
        StringBuilder s = new StringBuilder(1000);
        s.append(indent);
        s.append(String.format("%s(%s) [%s] type: %s, cost: %f\n",
                this.getClass().getSimpleName(),
                getAlias(),
                Joiner.on(", ").join(aggs),
                sampleType(),
                cost()));
        s.append(source.toStringWithIndent(indent + "  "));
        return s.toString();
    }

    @Override
    public boolean equals(ApproxRelation o) {
        if (o instanceof ApproxAggregatedRelation) {
            if (source.equals(((ApproxAggregatedRelation) o).source)) {
                if (aggs.equals(((ApproxAggregatedRelation) o).aggs)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sampling probability defers based on the sample type and the preceding groupby.
     * For many cases, sampling probabilities are not easily computable, in which case, we set the return value
     * to zero, so that the sample is not used for computing another aggregation.
     */
    @Override
    public double samplingProbability() {
        if (source.sampleType().equals("nosample")) {
            return 1.0;
        }

        if (source instanceof ApproxGroupedRelation) {
            List<Expr> groupby = ((ApproxGroupedRelation) source).getGroupby();
            List<String> groupbyInString = new ArrayList<String>();
            for (Expr expr : groupby) {
                if (expr instanceof ColNameExpr) {
                    groupbyInString.add(((ColNameExpr) expr).getCol());
                }
            }

            if (source.sampleType().equals("stratified")) {
                if (groupbyInString.equals(sampleColumns())) {
                    return 1.0;
                } else {
                    return 0;
                }
            } else if (source.sampleType().equals("universe")) {
                if (groupbyInString.equals(sampleColumns())) {
                    return source.samplingProbability();
                } else {
                    return 0;
                }
            }
        }

        return 0;
    }

    @Override
    protected boolean doesIncludeSample() {
        return source.doesIncludeSample();
    }

}
