package edu.umich.verdict.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictSQLParser;
import edu.umich.verdict.datatypes.Alias;
import edu.umich.verdict.datatypes.TableUniqueName;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.util.VerdictLogger;

public class BootstrapSelectStatementRewriter extends AnalyticSelectStatementRewriter {
	
	final String RAND_COLNAME = vc.getConf().get("bootstrap_random_value_colname");

	final String MULTIPLICITY = vc.getConf().get("bootstrap_multiplicity_colname");

	protected String resampleMethod;
	
	// poissonDist[k] is the probability that a tuple appears k times when n is very large.
	final private static double[] poissonDist = {
			0.367879441171442,
			0.367879441171442,
			0.183939720585721,
			0.061313240195240,
			0.015328310048810,
			0.003065662009762,
			0.000510943668294,
			0.000072991952613,
			0.000009123994077,
			0.000001013777120,
			0.000000101377712
	};
	
	// cumulProb[k] is the probability that a tuple appears k times or less.
	final private static double[] cumulProb;
	static {
		cumulProb = new double[poissonDist.length];
		cumulProb[0] = poissonDist[0];
		for (int i = 1; i < poissonDist.length; i++) {
			cumulProb[i] = cumulProb[i-1] + poissonDist[i];
		}
		cumulProb[poissonDist.length-1] = 1.0;
	}
	
	// conditionalProb[k] is the probability that a tuple appears k times conditioned that the tuple does not appear
	// more than k times.
	final private static double[] conditionalProb;
	
	static {
		conditionalProb = new double[poissonDist.length];
		double cdf = 0;
		for (int i = 0; i < poissonDist.length; i++) {
			cdf += poissonDist[i];
			conditionalProb[i] = poissonDist[i] / cdf;
		}
	}
	
	protected Map<TableUniqueName, String> sampleTableAlias = new HashMap<TableUniqueName, String>();

	public BootstrapSelectStatementRewriter(VerdictContext vc, String queryString) {
		super(vc, queryString);
		resampleMethod = "con";
	}
	
	public void setResampleMethod(String m) throws VerdictException {
		if (m.equals("con") || m.equals("1")) {
			throw new VerdictException("Unexpected resample method: " + m);
		}
		resampleMethod = m;
	}
	
	
	/**
	 * Rewrites a query for combining mean estimate and the variance estimate.
	 * 
	 * This function is pretty convoluted to make use of the overloaded functions in this class definition and 
	 * {@link AnalyticSelectStatementRewriter#visitQuery_specification(edu.umich.verdict.VerdictSQLParser.Query_specificationContext) 
	 * visitQuery_specification} of the base class for every bootstrap trial.
	 * For calling the visitQuery_specification function, this class defines another function {@link
	 * BootstrapSelectStatementRewriter#visitQuery_specificationForSingleTrial(VerdictSQLParser.Query_specificationContext ctx)
	 * visitQuery_specificationForSingleTrial}.
	 */
	@Override
	public String visitQuery_specification(VerdictSQLParser.Query_specificationContext ctx) {
		StringBuilder sql = new StringBuilder(2000);
		
		// this statement computes the mean value
		AnalyticSelectStatementRewriter meanRewriter = new AnalyticSelectStatementRewriter(vc, queryString);
		meanRewriter.setDepth(depth+1);
		meanRewriter.setIndentLevel(defaultIndent + 6);
		String mainSql = meanRewriter.visit(ctx);
		cumulativeReplacedTableSources.putAll(meanRewriter.getCumulativeSampleTables());
		
		// this statement computes the standard deviation
		BootstrapSelectStatementRewriter varianceRewriter = new BootstrapSelectStatementRewriter(vc, queryString);
		varianceRewriter.setDepth(depth+1);
		varianceRewriter.setIndentLevel(defaultIndent + 6);
		String subSql = varianceRewriter.varianceComputationStatement(ctx);
		
		Alias leftAlias = Alias.genDerivedTableAlias(depth);		// table alias name for mean statement
		Alias rightAlias = Alias.genDerivedTableAlias(depth);		// table alias name for variance statement
		
		// we combine those two statements using join.
		List<Pair<String, Alias>> thisColumnName2Aliases = new ArrayList<Pair<String, Alias>>();
		
		List<Pair<String, Alias>> leftColName2Aliases = meanRewriter.getColName2Aliases();
//		List<Boolean> leftAggColIndicator = meanRewriter.getAggregateColumnIndicator();
		
		List<Pair<String, Alias>> rightColName2Aliases = varianceRewriter.getColName2Aliases();
//		List<Boolean> rightAggColIndicator = varianceRewriter.getAggregateColumnIndicator();
		
		sql.append(String.format("%sSELECT", indentString));
		int totalSelectElemIndex = 0;
		List<Integer> groupbyIndex = new ArrayList<Integer>();
		for (int leftSelectElemIndex = 1; leftSelectElemIndex <= leftColName2Aliases.size(); leftSelectElemIndex++) {
			Pair<String, Alias> leftColName2Alias = leftColName2Aliases.get(leftSelectElemIndex-1);
			Pair<String, Alias> rightColName2Alias = rightColName2Aliases.get(leftSelectElemIndex-1);
			
			if (leftSelectElemIndex == 1) sql.append(" ");
			else sql.append(", ");
			
			if (meanRewriter.isAggregateColumn(leftSelectElemIndex)) {
				// mean
				totalSelectElemIndex++;
				Alias alias = leftColName2Alias.getValue();	// we propagate the alias names
				sql.append(String.format("%s.%s AS %s", leftAlias, alias, alias));
				thisColumnName2Aliases.add(Pair.of(leftColName2Alias.getKey(), alias));
				
				// error (standard deviation * 1.96 (for 95% confidence interval))
				totalSelectElemIndex++;
				alias = rightColName2Alias.getValue();
				sql.append(String.format(", %s.%s AS %s", rightAlias, alias, alias));
				thisColumnName2Aliases.add(Pair.of(rightColName2Alias.getKey(), alias));
				
				meanColIndex2ErrColIndex.put(totalSelectElemIndex-1, totalSelectElemIndex);
			} else {
				totalSelectElemIndex++;
				Alias alias = leftColName2Alias.getValue();
				sql.append(String.format("%s.%s AS %s", leftAlias, alias, alias));
				groupbyIndex.add(leftSelectElemIndex);
				thisColumnName2Aliases.add(Pair.of(leftColName2Alias.getKey(), alias));
			}
		}
		colName2Aliases = thisColumnName2Aliases;
		
		sql.append(String.format("\n%sFROM (\n", indentString));
		sql.append(mainSql);
		sql.append(String.format("\n%s     ) AS %s", indentString, leftAlias));
		sql.append(" LEFT JOIN (\n");
		sql.append(subSql);
		sql.append(String.format("%s) AS %s", indentString, rightAlias));
		for (Integer gid : groupbyIndex) {
			sql.append(String.format(" ON %s.%s = %s.%s", leftAlias, leftColName2Aliases.get(gid-1).getValue(),
														  rightAlias, rightColName2Aliases.get(gid-1).getValue()));
		}
		
		return sql.toString();
	}
	
	/**
	 * Writes a query for a single bootstrap trial. This function makes uses of several overloaded methods by this class.
	 * @param ctx
	 * @return
	 */
	protected String visitQuery_specificationForSingleTrial(VerdictSQLParser.Query_specificationContext ctx) {
		return super.visitQuery_specification(ctx);
	}
	
	protected String varianceFunction() {
		return vc.getDbms().varianceFunction();
	}
	
	protected String stddevFunction() {
		return vc.getDbms().stddevFunction();
	}
	
	private String errorIndicator() {
		return "_";
	}
	
	protected String errColumnName(String regularName) {
		return String.format(vc.getConf().get("error_column_pattern"), regularName);
	}
	
	protected String varianceComputationStatement(VerdictSQLParser.Query_specificationContext ctx) {
		List<Pair<String, Alias>> subqueryColName2Aliases = null;
		BootstrapSelectStatementRewriter singleRewriter = null;
		
		StringBuilder unionedFrom = new StringBuilder(2000);
		int trialNum = vc.getConf().getInt("bootstrap_trial_num");
		for (int i = 0; i < trialNum; i++) {
			singleRewriter = new BootstrapSelectStatementRewriter(vc, queryString);
			singleRewriter.setIndentLevel(defaultIndent + 6);
			singleRewriter.setDepth(depth+1);
			String singleTrialQuery = singleRewriter.visitQuery_specificationForSingleTrial(ctx);
			if (i == 0) {
				subqueryColName2Aliases = singleRewriter.getColName2Aliases();
			}
			if (i > 0) unionedFrom.append(String.format("\n%s    UNION\n", indentString));
			unionedFrom.append(singleTrialQuery);
		}
		
		StringBuilder sql = new StringBuilder(2000);
		sql.append(String.format("%sSELECT", indentString));
		int selectElemIndex = 0;
		for (Pair<String, Alias> e : subqueryColName2Aliases) {
			selectElemIndex++;
			sql.append((selectElemIndex > 1)? ", " : " ");
			if (singleRewriter.isAggregateColumn(selectElemIndex)) {
				String stddevColname = String.format("%s(%s)", stddevFunction(), e.getValue());
				Alias alias = Alias.genAlias(depth, errColumnName(e.getValue().getProperName()));
				sql.append(String.format("%s AS %s", stddevColname, alias));
				colName2Aliases.add(Pair.of(e.getKey() + errorIndicator(), alias));
			} else {
				Alias colname = e.getValue();
				Alias alias = Alias.genAlias(depth, colname.originalName());
				sql.append(String.format("%s AS %s", colname, alias));
				colName2Aliases.add(Pair.of(e.getKey(), alias));
			}
		}
		sql.append(String.format("\n%sFROM (\n", indentString));
		sql.append(unionedFrom.toString());
		sql.append(String.format("\n%s) AS %s", indentString, Alias.genDerivedTableAlias(depth)));
		sql.append(String.format("\n%sGROUP BY", indentString));
		
		for (int colIndex = 1; colIndex <= subqueryColName2Aliases.size(); colIndex++) {
			if (!singleRewriter.isAggregateColumn(colIndex)) {
				if (colIndex > 1) {
					sql.append(String.format(", %s", colName2Aliases.get(colIndex-1).getValue()));
				} else {
					sql.append(String.format(" %s", colName2Aliases.get(colIndex-1).getValue()));
				}
			}
		}
			
		return sql.toString();
	}
	
	
	@Override
	public String visitAggregate_function_within_query_specification(VerdictSQLParser.Aggregate_windowed_functionContext ctx) {
		while (aggColumnIndicator.size() < select_list_elem_num-1) {
			aggColumnIndicator.add(false);		// pad zero
		}
		aggColumnIndicator.add(true);			// TODO this must be adapted according to the sample size.
		
		if (ctx.AVG() != null) {
			return String.format("AVG((%s) * %s)",
					visit(ctx.all_distinct_expression()),
					MULTIPLICITY,
					getSampleSizeToOriginalTableSizeRatio());
		} else if (ctx.SUM() != null) {
			return String.format("(SUM((%s) * %s) * %f)",
					visit(ctx.all_distinct_expression()),
					MULTIPLICITY,
					getSampleSizeToOriginalTableSizeRatio());
		} else if (ctx.COUNT() != null) {
			return String.format("ROUND((SUM(%s) * %f))", MULTIPLICITY, getSampleSizeToOriginalTableSizeRatio());
		}
		VerdictLogger.error(this, String.format("Unexpected aggregate function expression: %s", ctx.getText()));
		return null;	// we don't handle other aggregate functions for now.
	}
	
	protected String multiplicityExpression() {
		return multiplicityExpression(resampleMethod);
	}
	
	protected String multiplicityExpression(String param) {
		if (param != null && param.equals("1")) {
			return String.format("1 AS %s", MULTIPLICITY);
		} else if (param != null && param.equals("con")) {
			StringBuilder elem = new StringBuilder();
			elem.append("(case");
			for (int k = conditionalProb.length - 1; k > 0 ; k--) {
				elem.append(String.format(" WHEN rand() <= %.10f THEN %d", conditionalProb[k], k));
			}
			elem.append(String.format(" ELSE 0 END) AS %s", MULTIPLICITY));
			return elem.toString();
		} else {
			StringBuilder elem = new StringBuilder();
			elem.append("(case");
			for (int k = 0; k < cumulProb.length-1; k++) {
				elem.append(String.format(" WHEN %s <= %.10f THEN %d", RAND_COLNAME, cumulProb[k], k));
			}
			elem.append(String.format(" ELSE %d END) AS %s", cumulProb.length-1, MULTIPLICITY));
			return elem.toString();
		}
	}
	
	@Override
	protected String extraIndentBeforeTableSourceName(int sourceIndex) {
		return (sourceIndex == 1) ? "" : "\n" + indentString + "      ";
	}
	
	protected TableUniqueName aliasedTableNameItem = null;
	
	@Override
	public String visitHinted_table_name_item(VerdictSQLParser.Hinted_table_name_itemContext ctx) {
		String tableNameItem = visit(ctx.table_name_with_hint());
		Alias alias = null;
		if (ctx.as_table_alias() == null) {
			alias = Alias.genAlias(depth, tableNameItem);
		} else {
			alias = new Alias(tableNameItem, ctx.as_table_alias().getText());
		}
		tableAliases.put(aliasedTableNameItem, alias);
		return tableNameItem + " " + alias;
	}
	
	@Override
	protected String tableSourceReplacer(String originalTableName) {
		String bootstrap_sampling_method = vc.getConf().get("bootstrap_sampling_method");
		
		if (bootstrap_sampling_method == null) {
			return tableSourceReplacerSingleNested(originalTableName);	// by default
		} else if (bootstrap_sampling_method.equals("single_nested")) {
			return tableSourceReplacerSingleNested(originalTableName);
		} else if (bootstrap_sampling_method.equals("double_nested")) {
			VerdictLogger.warn(this, "Double-nested bootstrap sampling is erroroneous; thus, it should be avoided.");
			return tableSourceReplacerDoubleNested(originalTableName);
		} else {
			return tableSourceReplacerSingleNested(originalTableName);
		}
	}
	
	protected String tableSourceReplacerSingleNested(String originalTableName) {
		TableUniqueName uTableName = TableUniqueName.uname(vc, originalTableName);
		TableUniqueName newTableSource = vc.getMeta().getSampleTableNameIfExistsElseOriginal(uTableName);
		aliasedTableNameItem = newTableSource;
		if (!uTableName.equals(newTableSource)) {
			replacedTableSources.add(uTableName);
			cumulativeReplacedTableSources.put(uTableName, newTableSource);
			return String.format("(SELECT *, %s\n%sFROM %s)",
					multiplicityExpression(),
					indentString + "      ",
					newTableSource);
		} else {
			return newTableSource.toString();
		}
	}
	
	/**
	 * This must not be used. The rand() AS verdict_rand is not materialized; thus, every call of verdict_rand produces
	 * a different value.
	 * @param originalTableName
	 * @return
	 */
	protected String tableSourceReplacerDoubleNested(String originalTableName) {
		TableUniqueName uTableName = TableUniqueName.uname(vc, originalTableName);
		TableUniqueName newTableSource = vc.getMeta().getSampleTableNameIfExistsElseOriginal(uTableName);
		aliasedTableNameItem = newTableSource;
		if (!uTableName.equals(newTableSource)) {
			replacedTableSources.add(uTableName);
			cumulativeReplacedTableSources.put(uTableName, newTableSource);
			return String.format("(SELECT *, %s\n%sFROM %s",
					multiplicityExpression("cumul"),
					indentString + "      ",
					String.format("(SELECT *, rand() AS %s\n%sFROM %s) %s)",
							RAND_COLNAME,
							indentString + "            ",
							newTableSource,
							Alias.genDerivedTableAlias(depth)));
		} else {
			return newTableSource.toString();
		}
	}
	
	@Override
	public String visitOrder_by_clause(VerdictSQLParser.Order_by_clauseContext ctx) {
		StringBuilder orderby = new StringBuilder();
		orderby.append("ORDER BY");
		boolean isFirst = true;
		for (VerdictSQLParser.Order_by_expressionContext octx : ctx.order_by_expression()) {
			Alias alias = findAliasedExpression(visit(octx));
			
			if (isFirst) orderby.append(" ");
			else 	     orderby.append(", ");
			
			orderby.append(alias);
			if (octx.DESC() != null) {
				orderby.append(" DESC");
			}
			if (octx.ASC() != null) {
				orderby.append(" ASC");
			}
			isFirst = false;
		}
		return orderby.toString();
	}
	
	/**
	 * @param colExpr The original text
	 * @return An alias we declared
	 */
	private Alias findAliasedExpression(String colExpr) {
		for (Pair<String, Alias> e : colName2Aliases) {
			if (colExpr.equalsIgnoreCase(e.getLeft())) {
				return e.getRight();
			}
		}
		return new Alias(colExpr, colExpr);
	}
	
}

