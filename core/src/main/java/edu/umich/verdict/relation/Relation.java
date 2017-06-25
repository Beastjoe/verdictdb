package edu.umich.verdict.relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.google.common.base.Joiner;

import edu.umich.verdict.VerdictContext;
import edu.umich.verdict.VerdictSQLBaseVisitor;
import edu.umich.verdict.VerdictSQLLexer;
import edu.umich.verdict.VerdictSQLParser;
import edu.umich.verdict.VerdictSQLParser.ExpressionContext;
import edu.umich.verdict.VerdictSQLParser.Join_partContext;
import edu.umich.verdict.VerdictSQLParser.Search_conditionContext;
import edu.umich.verdict.exceptions.VerdictException;
import edu.umich.verdict.exceptions.VerdictUnexpectedMethodCall;
import edu.umich.verdict.query.SelectStatementBaseRewriter;
import edu.umich.verdict.relation.expr.Expr;
import edu.umich.verdict.relation.expr.SelectElem;
import edu.umich.verdict.util.ResultSetConversion;
import edu.umich.verdict.util.StackTraceReader;
import edu.umich.verdict.util.TypeCasting;
import edu.umich.verdict.util.VerdictLogger;

/**
 * Both {@link ExactRelation} and {@link ApproxRelation} must extends this class.
 * @author Yongjoo Park
 *
 */
public abstract class Relation {
	
	protected VerdictContext vc;
	
	protected boolean subquery;
	
	protected boolean approximate;
	
	protected String alias;
	
	public Relation(VerdictContext vc) {
		this.vc = vc;
		this.subquery = false;
		this.approximate = false;
	}
	
	public boolean isSubquery() {
		return subquery;
	}
	
	public void setSubquery(boolean a) {
		this.subquery = a;
	}
	
	public boolean isApproximate() {
		return approximate;
	}
	
	public String getAliasName() {
		return alias;
	}
	
	public void setAliasName(String a) {
		alias = a;
	}
	
	/**
	 * Expression that would appear in sql statement.
	 * SingleSourceRelation: table name
	 * JoinedSourceRelation: join expression
	 * FilteredRelation: select * where condition from sourceExpr()
	 * AggregatedRelation: select groupby, agg where condition from sourceExpr()
	 * @return
	 * @throws VerdictUnexpectedMethodCall 
	 */
	public abstract String toSql();
	
	/*
	 * Aggregation
	 */

	public long countValue() throws VerdictException {
		return TypeCasting.toLong(count().collect().get(0).get(0));
	}

	public double sumValue(String expr) throws VerdictException {
		return TypeCasting.toDouble(sum(expr).collect().get(0).get(0));
	}
	
	public double avgValue(String expr) throws VerdictException {
		return TypeCasting.toDouble(avg(expr).collect().get(0).get(0));
	}
	
	public long countDistinctValue(String expr) throws VerdictException {
		return TypeCasting.toLong(countDistinct(expr).collect().get(0).get(0));
	}
	
	public abstract Relation count() throws VerdictException;
	
	public abstract Relation sum(String expr) throws VerdictException;
	
	public abstract Relation avg(String expr) throws VerdictException;
	
	public abstract Relation countDistinct(String expr) throws VerdictException;
	
	/*
	 * Collect results
	 */
	
	public ResultSet collectResultSet() throws VerdictException {
		String sql = toSql();
//		VerdictLogger.info("The query to db: " + sql);
		VerdictLogger.debug(this, "A query to db:");
		VerdictLogger.debugPretty(this, Relation.prettyfySql(sql), " ");
		return vc.getDbms().executeQuery(sql);
	}
	
	public List<List<Object>> collect() throws VerdictException {
		List<List<Object>> result = new ArrayList<List<Object>>();
		ResultSet rs = collectResultSet();
		try {
			int colCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				List<Object> row = new ArrayList<Object>();	
				for (int i = 1; i <= colCount; i++) {
					row.add(rs.getObject(i));
				}
				result.add(row);
			}
		} catch (SQLException e) {
			throw new VerdictException(e);
		}
		return result;
	}
	
	public String collectAsString() {
		try {
			return ResultSetConversion.resultSetToString(collectResultSet());
		} catch (VerdictException e) {
			return StackTraceReader.stackTrace2String(e);
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/*
	 * Helpers
	 */
	
	protected List<Expr> exprsInSelectElems(List<SelectElem> elems) {
		List<Expr> exprs = new ArrayList<Expr>();
		for (SelectElem e : elems) {
			exprs.add(e.getExpr());
		}
		return exprs;
	}

	public static String prettyfySql(String sql) {
		VerdictSQLLexer l = new VerdictSQLLexer(CharStreams.fromString(sql));
		VerdictSQLParser p = new VerdictSQLParser(new CommonTokenStream(l));
		PrettyPrintVisitor r = new PrettyPrintVisitor(sql);
		return r.visit(p.verdict_statement());
	}
	
	private static int alias_no = 1;
	
	public static String genTableAlias() {
		String n = String.format("vt%d", alias_no);
		alias_no++;
		return n;
	}
	
	public static String genColumnAlias() {
		String n = String.format("vc%d", alias_no);
		alias_no++;
		return n;
	}
	
}


class PrettyPrintVisitor extends VerdictSQLBaseVisitor<String> {
	
	protected String sql;
	
	protected String indent = "";
	
	// pair of original table name and its alias
//	protected Map<String, String> tableAliases = new HashMap<String, String>();
	
	public PrettyPrintVisitor(String sql) {
		this.sql = sql;
		this.indent = "";
	}
	
	public void setIndent(String indent) {
		this.indent = indent;
	}
	
	@Override public String visitCreate_table_as_select(VerdictSQLParser.Create_table_as_selectContext ctx) {
		String create = String.format("CREATE TABLE %s%s AS\n",
				(ctx.IF() != null)? "IF NOT EXISTS " : "",
				ctx.table_name().getText());
		
		PrettyPrintVisitor v = new PrettyPrintVisitor(sql);
		v.setIndent(indent + "    ");
		String select = v.visit(ctx.select_statement());
		
		return create + select;
	}
	
	@Override
	public String visitSelect_statement(VerdictSQLParser.Select_statementContext ctx) {
		StringBuilder query = new StringBuilder(1000);
		query.append(visit(ctx.query_expression()));
		
		if (ctx.order_by_clause() != null) {
			query.append(String.format("\n%s", indent + visit(ctx.order_by_clause())));
		}
		
		if (ctx.limit_clause() != null) {
			query.append(String.format("\n%s", indent + visit(ctx.limit_clause())));
		}
		return query.toString();
	}
	
	@Override
	public String visitQuery_specification(VerdictSQLParser.Query_specificationContext ctx) {
		// Construct a query string after processing all subqueries.
		// The processed subqueries are stored as a view.
		StringBuilder query = new StringBuilder(200);
		query.append(indent + "SELECT ");
		query.append(visit(ctx.select_list()));
		query.append(" ");
		
		query.append("\n" + indent + "FROM ");
		boolean isFirstTableSource = true;
		for (VerdictSQLParser.Table_sourceContext tctx : ctx.table_source()) {
			if (isFirstTableSource) {
				query.append(visit(tctx));
			} else {
				query.append(String.format(", %s", visit(tctx)));
			}
			isFirstTableSource = false;
		}
		query.append(" ");
		
		if (ctx.where != null) {
			query.append("\n" + indent + "WHERE ");
			query.append(visit(ctx.where));
			query.append(" ");
		}
		
		if (ctx.group_by_item() != null && ctx.group_by_item().size() > 0) {
			query.append("\n" + indent + "GROUP BY ");
			for (VerdictSQLParser.Group_by_itemContext gctx : ctx.group_by_item()) {
				query.append(visit(gctx));
			}
			query.append(" ");
		}
		
		String sql = query.toString();
		return sql;
	}
	
	boolean isFirstSelectElem = true;
	
	@Override
	public String visitSelect_list(VerdictSQLParser.Select_listContext ctx) {
		List<String> elems = new ArrayList<String>();
		for (VerdictSQLParser.Select_list_elemContext ectx : ctx.select_list_elem()) {
			elems.add(visit(ectx));
		}
		return Joiner.on(", ").join(elems);
	}
	
	@Override
	public String visitSelect_list_elem(VerdictSQLParser.Select_list_elemContext ctx) {
		if (ctx.getText().equals("*")) {
			return "*";
		} else {
			StringBuilder elem = new StringBuilder();
			elem.append(visit(ctx.expression()));
			if (ctx.column_alias() != null) {
				elem.append(String.format(" AS %s", ctx.column_alias().getText()));
			}
			return elem.toString();
		}
	}
	
	@Override
	public String visitSearch_condition(VerdictSQLParser.Search_conditionContext ctx) {
		List<String> c = new ArrayList<String>();
		for (VerdictSQLParser.Search_condition_orContext octx : ctx.search_condition_or()) {
			c.add(visit(octx));
		}
		return Joiner.on(String.format("\n%s  AND ", indent)).join(c);
	}

	@Override
	public String visitSearch_condition_or(VerdictSQLParser.Search_condition_orContext ctx) {
		List<String> c = new ArrayList<String>();
		for (VerdictSQLParser.Search_condition_notContext nctx : ctx.search_condition_not()) {
			c.add(visit(nctx));
		}
		return Joiner.on(String.format("\n%s  AND ", indent)).join(c);
	}

	@Override
	public String visitSearch_condition_not(VerdictSQLParser.Search_condition_notContext ctx) {
		String predicate = visit(ctx.predicate());
		return ((ctx.NOT() != null) ? "NOT" : "") + predicate;   
	}
	
	@Override
	public String visitSubquery(VerdictSQLParser.SubqueryContext ctx) {
		PrettyPrintVisitor v = new PrettyPrintVisitor(sql);
		v.setIndent(indent + "    ");
		return v.visit(ctx.select_statement());
	}
	
	@Override
	public String visitExists_predicate(VerdictSQLParser.Exists_predicateContext ctx) {
		return String.format("EXISTS (\n%s)", visit(ctx.subquery()));
	}
	
	@Override
	public String visitComp_expr_predicate(VerdictSQLParser.Comp_expr_predicateContext ctx) {
		String exp1 = visit(ctx.expression(0));
		String exp2 = visit(ctx.expression(1));
		return String.format("%s %s %s", exp1, ctx.comparison_operator().getText(), exp2);
	}
	
	@Override
	public String visitSetcomp_expr_predicate(VerdictSQLParser.Setcomp_expr_predicateContext ctx) {
		return String.format("%s %s (\n%s)", ctx.expression().getText(), ctx.comparison_operator().getText(), visit(ctx.subquery()));
	}
	
	@Override
	public String visitComp_between_expr(VerdictSQLParser.Comp_between_exprContext ctx) {
		return String.format("%s %s BETWEEN %s AND %s",
				ctx.expression(0).getText(), (ctx.NOT() == null)? "" : "NOT", ctx.expression(1).getText(), ctx.expression(2).getText() );
	}
	
	@Override
	public String visitIs_predicate(VerdictSQLParser.Is_predicateContext ctx) {
		return String.format("%s IS %s%s", visit(ctx.expression()), (ctx.null_notnull().NOT() != null) ? "NOT " : "", "NULL");
	}
	
	private String getExpressions(VerdictSQLParser.Expression_listContext ctx) {
		List<String> e = new ArrayList<String>();
		for (VerdictSQLParser.ExpressionContext ectx : ctx.expression()) {
			e.add(visit(ectx));
		}
		return Joiner.on(", ").join(e);
	}
	
	@Override
	public String visitIn_predicate(VerdictSQLParser.In_predicateContext ctx) {
		return 	String.format("%s %s IN (%s)",
				ctx.expression().getText(),
				(ctx.NOT() == null)? "" : "NOT",
				(ctx.subquery() == null)? getExpressions(ctx.expression_list()) : "\n" + visit(ctx.subquery()));
	}
	
	@Override
	public String visitLike_predicate(VerdictSQLParser.Like_predicateContext ctx) {
		return String.format("%s %s LIKE %s",
				ctx.expression(0).getText(), (ctx.NOT() == null)? "" : "NOT", ctx.expression(1).getText());
	}
	
	@Override public String visitCase_expr(VerdictSQLParser.Case_exprContext ctx) {
		StringBuilder sql = new StringBuilder();
		sql.append("CASE");
		List<ExpressionContext> exprs = ctx.expression(); 
		
		List<Search_conditionContext> search_conds = ctx.search_condition();
		if (search_conds.size() > 0) {	// second case
			for (int i = 0; i < search_conds.size(); i++) {
				sql.append(" WHEN ");
				sql.append(visit(search_conds.get(i)));
				sql.append(" THEN ");
				sql.append(visit(exprs.get(i)));
			}
			
			if (exprs.size() > search_conds.size()) {
				sql.append(" ELSE ");
				sql.append(visit(exprs.get(exprs.size()-1)));
			}
		} else {	// first case
			sql.append(" ");
			sql.append(exprs.get(0));
			for (int j = 0; j < (exprs.size()-1)/2; j++) {
				int i1 = j*2 + 1;
				int i2 = j*2 + 2;
				sql.append(" WHEN ");
				sql.append(visit(exprs.get(i1)));
				sql.append(" THEN ");
				sql.append(visit(exprs.get(i2)));
			}
			if (ctx.ELSE() != null) {
				sql.append(" ELSE ");
				sql.append(exprs.get(exprs.size()-1));
			}
		}
		
		sql.append(" END");
		return sql.toString();
	}
	
	@Override public String visitBracket_expression(VerdictSQLParser.Bracket_expressionContext ctx) {
		return "(" + visit(ctx.expression()) + ")";
	}
	
	@Override
	public String visitBracket_predicate(VerdictSQLParser.Bracket_predicateContext ctx) {
		return String.format("(%s)", visit(ctx.search_condition()));
	}
	
	@Override
	public String visitPrimitive_expression(VerdictSQLParser.Primitive_expressionContext ctx) {
		return ctx.getText();
	}
	
	@Override
	public String visitColumn_ref_expression(VerdictSQLParser.Column_ref_expressionContext ctx) {
		return visit(ctx.full_column_name());
	}
	
	@Override
	public String visitFull_column_name(VerdictSQLParser.Full_column_nameContext ctx) {
		StringBuilder tabName = new StringBuilder();
		if (ctx.table_name() != null) {
			tabName.append(String.format("%s.", visit(ctx.table_name())));
		}
		tabName.append(ctx.column_name().getText());
		return tabName.toString();
	}
	
	@Override public String visitMathematical_function_expression(VerdictSQLParser.Mathematical_function_expressionContext ctx)
	{
		if (ctx.expression() != null) {
			return String.format("%s(%s)", ctx.unary_mathematical_function().getText(), visit(ctx.expression()));
		} else {
			return String.format("%s()", ctx.noparam_mathematical_function().getText());
		}
		
	}
	
	@Override
	public String visitAggregate_windowed_function(VerdictSQLParser.Aggregate_windowed_functionContext ctx) {
		if (ctx.AVG() != null) {
			return String.format("AVG(%s)", visit(ctx.all_distinct_expression()));
		} else if (ctx.SUM() != null) {
			return String.format("SUM(%s)", visit(ctx.all_distinct_expression()));
		} else if (ctx.COUNT() != null) {
			if (ctx.all_distinct_expression() != null) {
				String colName = ctx.all_distinct_expression().expression().getText();
				if (ctx.all_distinct_expression().DISTINCT() != null) {
					return String.format("COUNT(DISTINCT %s)", colName);
				} else {
					return String.format("COUNT(%s)", colName);
				}
			} else {
				return String.format("COUNT(*)");
			}
		} else if (ctx.NDV() != null) {
			return String.format("NDV(%s)", visit(ctx.all_distinct_expression()));
		}
		VerdictLogger.error(this, String.format("Unexpected aggregate function expression: %s", ctx.getText()));
		return null;	// we don't handle other aggregate functions for now.
		
	}
	
	@Override
	public String visitBinary_operator_expression(VerdictSQLParser.Binary_operator_expressionContext ctx) {
		VerdictSQLParser.ExpressionContext left = ctx.expression(0);
		VerdictSQLParser.ExpressionContext right = ctx.expression(1);
		String op = ctx.op.getText();
		
		String leftCol = visit(left);
		String rightCol = visit(right);
		
		if (leftCol == null || rightCol == null) return null;
		else if (op.equals("*")) return leftCol + " * " + rightCol;
		else if (op.equals("+")) return leftCol + " + " + rightCol;
		else if (op.equals("/")) return leftCol + " / " + rightCol;
		else if (op.equals("-")) return leftCol + " - " + rightCol;
		else return null;
	}
	
	@Override
	public String visitSubquery_expression(VerdictSQLParser.Subquery_expressionContext ctx) {
		return String.format("(\n%s)", visit(ctx.subquery()));
	}

	@Override
	public String visitTable_source(VerdictSQLParser.Table_sourceContext ctx) {
		return visit(ctx.table_source_item_joined());
	}
		
	@Override
	public String visitTable_source_item_joined(VerdictSQLParser.Table_source_item_joinedContext ctx) {
		StringBuilder sql = new StringBuilder();
		sql.append(visit(ctx.table_source_item()));
		for (Join_partContext jctx : ctx.join_part()) {
			sql.append("\n" + indent + "     " + visit(jctx));
		}
		return sql.toString();
	}
	
	@Override
	public String visitJoin_part(VerdictSQLParser.Join_partContext ctx) {
		if (ctx.INNER() != null) {
			return String.format("INNER JOIN %s ON %s", visit(ctx.table_source()), visit(ctx.search_condition()));
		} else if (ctx.OUTER() != null) {
			return String.format("%s OUTER JOIN %s ON %s", ctx.join_type.getText(), visit(ctx.table_source()), visit(ctx.search_condition()));
		} else if (ctx.CROSS() != null) {
			return String.format("CROSS JOIN %s", visit(ctx.table_source()));
		} else {
			return String.format("UNSUPPORTED JOIN (%s)", ctx.getText());
		}
	}
	
	@Override
	public String visitSample_table_name_item(VerdictSQLParser.Sample_table_name_itemContext ctx) {
		assert(false);		// specifying sample table size is not supported now.
		return visitChildren(ctx);
	}
	
	@Override
	public String visitHinted_table_name_item(VerdictSQLParser.Hinted_table_name_itemContext ctx) {
		String tableNameItem = visit(ctx.table_name_with_hint());
		if (ctx.as_table_alias() == null) {
			return tableNameItem;
		} else {
			String alias = ctx.as_table_alias().getText();
			return tableNameItem + " " + alias;
		}
	}
	
	@Override
	public String visitDerived_table_source_item(VerdictSQLParser.Derived_table_source_itemContext ctx) {
		return String.format("(\n%s) AS %s", visit(ctx.derived_table().subquery()), ctx.as_table_alias().table_alias().getText());
	}
	
	@Override
	public String visitTable_name_with_hint(VerdictSQLParser.Table_name_with_hintContext ctx) {
		return visit(ctx.table_name());
	}
	
	@Override
	public String visitTable_name(VerdictSQLParser.Table_nameContext ctx) {
		return ctx.getText();
	}
	
	@Override
	public String visitGroup_by_item(VerdictSQLParser.Group_by_itemContext ctx) {
		return ctx.getText();
	}
	
	@Override
	public String visitOrder_by_clause(VerdictSQLParser.Order_by_clauseContext ctx) {
		return getOriginalText(ctx);
	}
	
	@Override
	public String visitLimit_clause(VerdictSQLParser.Limit_clauseContext ctx) {
		return "\n" + indent + "LIMIT " + ctx.number().getText();
	}
	
	protected String getOriginalText(ParserRuleContext ctx) {
		int a = ctx.start.getStartIndex();
	    int b = ctx.stop.getStopIndex();
	    Interval interval = new Interval(a,b);
	    return CharStreams.fromString(sql).getText(interval);
	}
}
