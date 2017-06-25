package edu.umich.verdict.relation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.datatypes.SampleParam;
import edu.umich.verdict.datatypes.SampleSizeInfo;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.relation.expr.Expr;
import edu.umich.verdict.relation.expr.FuncExpr;
import edu.umich.verdict.util.TypeCasting;
import edu.umich.verdict.util.VerdictLogger;

/**
 * Represents a sample table before any projection (including aggregation) or filtering is performed.
 * Aggregation functions issued on this table return approximate answers.
 * @author Yongjoo Park
 *
 */
public class ApproxSingleRelation extends ApproxRelation {
	
	protected TableUniqueName sampleTableName;
	
	protected SampleParam param;
	
	protected SampleSizeInfo info;
	
	protected boolean derived;
	
	protected ApproxSingleRelation(VerdictContext vc, TableUniqueName sampleTableName, SampleParam param, SampleSizeInfo info) {
		super(vc);
		this.sampleTableName = sampleTableName;
		this.param = param;
		this.info = info;
		derived = false;
	}
	
	protected ApproxSingleRelation(VerdictContext vc, SampleParam param) {
		super(vc);
		this.param = param;
		
		// set this.info
		TableUniqueName originalTable = param.originalTable;
		List<Pair<SampleParam, TableUniqueName>> sampleInfo = vc.getMeta().getSampleInfoFor(originalTable);
		for (Pair<SampleParam, TableUniqueName> e : sampleInfo) {
			SampleParam p = e.getLeft();
			
			if (param.samplingRatio == null) {
				if (p.originalTable.equals(param.originalTable)
						&& p.sampleType.equals(param.sampleType)
						&& p.columnNames.equals(param.columnNames)) {
					sampleTableName = e.getRight();
				}
			} else {
				if (p.equals(param)) {
					sampleTableName = e.getRight();
				}
			}
		}
		
		if (sampleTableName != null) {
			info = vc.getMeta().getSampleSizeOf(sampleTableName);
		}
		
		if (sampleTableName == null) {
			if (param.sampleType.equals("universe") || param.sampleType.equals("stratified")) {
				VerdictLogger.error(this, String.format("No %.2f%% %s sample table on %s found for the table %s.",
						param.samplingRatio*100, param.sampleType, param.columnNames.toString(), originalTable));
			} else if (param.sampleType.equals("uniform")) {
				VerdictLogger.error(this, String.format("No %.2f%% %s sample table found for the table %s.",
						param.samplingRatio*100, param.sampleType, originalTable));
			}
		}
	}
	
	/**
	 * Uses this method when creating a sample table that points to the materialized table. Using this method sets
	 * the derived field to false.
	 * @param vc
	 * @param param
	 * @return
	 */
	public static ApproxSingleRelation from(VerdictContext vc, SampleParam param) {
		if (param.sampleType.equals("nosample")) {
			return asis(SingleRelation.from(vc, param.originalTable));
		} else {
			return new ApproxSingleRelation(vc, param);
		}
	}
	
	public static ApproxSingleRelation asis(SingleRelation r) {
		return new ApproxSingleRelation(
				r.vc,
				r.getTableName(), 
				new SampleParam(r.getTableName(), "nosample", 1.0, null),
				new SampleSizeInfo(-1, -1));
	}
	
	public TableUniqueName getSampleName() {
		return sampleTableName;
	}

	public long getSampleSize() {
		return info.sampleSize;
	}

	public long getOriginalTableSize() {
		return info.originalTableSize;
	}

	public double getSamplingRatio() {
		return param.samplingRatio;
	}

	public String getSampleType() {
		return param.sampleType;
	}

	public TableUniqueName getOriginalTableName() {
		return getTableName();
	}
	
	public TableUniqueName getTableName() {
		return param.originalTable;
	}
	
	/*
	 * Approx
	 */

	@Override
	public ExactRelation rewrite() {
		ExactRelation r = SingleRelation.from(vc, getSampleName());
		r.setAliasName(getAliasName());
		return r;
	}
	
	@Override
	protected double samplingProbabilityFor(FuncExpr f) {
		if (f.getFuncName().equals(FuncExpr.FuncName.COUNT_DISTINCT)) {
			if (getSampleType().equals("universe")) {
				return getSamplingRatio();
			} else if (getSampleType().equals("stratified")) {
				return 1.0;
			} else if (getSampleType().equals("nosample")) {
				return 1.0;
			} else {
				VerdictLogger.warn(this, String.format("%s sample should not be used for count-distinct.", getSampleType()));
				return 1.0;
			}
		} else {	// SUM, COUNT
			if (getSampleType().equals("stratified")) {
				return 1.0;		// the sampling probability for stratified samples is handled by accumulateStratifiedSamples().
			} else {
				return getSampleSize() / (double) getOriginalTableSize();
			}
		}
	}
	
	@Override
	protected String sampleType() {
		return getSampleType();
	}
	
	@Override
	protected List<TableUniqueName> accumulateStratifiedSamples() {
		if (param.sampleType.equals("stratified")) {
			return Arrays.asList(param.originalTable);
		} else {
			return Arrays.asList();
		}
	}
	
	@Override
	protected List<String> sampleColumns() {
		return param.columnNames;
	}

	@Override
	protected Map<String, String> tableSubstitution() {
		Map<String, String> s = ImmutableMap.of(param.originalTable.tableName, sampleTableName.tableName);
		return s;
	}
	
	/*
	 * Aggregations
	 */
	
	public AggregatedRelation aggOnSample(List<Expr> functions) {
		return rewrite().agg(functions);
	}
	
	public AggregatedRelation aggOnSample(Expr... functions) {
		return aggOnSample(Arrays.asList(functions));
	}
	
	public long countOnSample() throws VerdictException {
		ExactRelation r = aggOnSample(FuncExpr.count());
		List<List<Object>> rs = r.collect();
		return (Long) rs.get(0).get(0);
	}
	
	public long countDistinctOnSample(Expr expression) throws VerdictException {
		ExactRelation r = aggOnSample(FuncExpr.countDistinct(expression));
		List<List<Object>> rs = r.collect();
		return (Long) rs.get(0).get(0);
	}
	
	@Override
	public String toString() {
		return "ApproxSingleRelation(" + param.toString() + ")";
	}

	@Override
	public int hashCode() {
		return sampleTableName.hashCode();
	}

	@Override
	public boolean equals(Object a) {
		if (a instanceof ApproxSingleRelation) {
			return sampleTableName.equals(((ApproxSingleRelation) a).getSampleName());
		} else {
			return false;
		}
	}
	
}
