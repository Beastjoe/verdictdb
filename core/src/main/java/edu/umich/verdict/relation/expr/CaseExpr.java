package edu.umich.verdict.relation.expr;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import edu.umich.verdict.VerdictSQLLexer;
import edu.umich.verdict.VerdictSQLParser;
import edu.umich.verdict.VerdictSQLParser.Case_exprContext;
import edu.umich.verdict.VerdictSQLParser.ExpressionContext;
import edu.umich.verdict.VerdictSQLParser.Search_conditionContext;
import edu.umich.verdict.relation.condition.Cond;
import edu.umich.verdict.util.VerdictLogger;

/**
 * For (CASE (WHEN condition THEN expression)+ ELSE expression END)
 * @author Yongjoo Park
 *
 */
public class CaseExpr extends Expr {
	
	private List<Cond> conditions;
	
	private List<Expr> expressions;
	
	public CaseExpr(List<Cond> conditions, List<Expr> expressions) {
		if (conditions.size() != expressions.size() && conditions.size() +1 != expressions.size()) {
			VerdictLogger.warn(this,
					String.format("Incorrect number of conditions (%d) for the number of expressions (%d) in a case expression.",
							conditions.size(), expressions.size()));
		}
		
		this.conditions = conditions;
		this.expressions = expressions;
	}
	
	public static CaseExpr from(String expr) {
		VerdictSQLLexer l = new VerdictSQLLexer(CharStreams.fromString(expr));
		VerdictSQLParser p = new VerdictSQLParser(new CommonTokenStream(l));
		return from(p.case_expr());
	}
	
	public static CaseExpr from(Case_exprContext ctx) {
		List<Cond> conds = new ArrayList<Cond>();
		for (Search_conditionContext c : ctx.search_condition()) {
			conds.add(Cond.from(c));
		}
		List<Expr> exprs = new ArrayList<Expr>();
		for (ExpressionContext e : ctx.expression()) {
			exprs.add(Expr.from(e));
		}
		return new CaseExpr(conds, exprs);
	}
	
	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder(100);
		sql.append("(CASE");
		for (int i = 0; i < conditions.size(); i++) {
			sql.append(String.format(" WHEN %s THEN %s", conditions.get(i), expressions.get(i)));
		}
		if (expressions.size() > conditions.size()) {
			sql.append(String.format(" ELSE %s", expressions.get(expressions.size()-1)));
		}
		sql.append(" END)");
		return sql.toString();
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.call(this);
	}

}
