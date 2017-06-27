package edu.umich.verdict.relation.condition;

import edu.umich.verdict.relation.expr.Expr;

public class IsCond extends Cond {

	private Expr left;
	
	private Cond right;
	
	public IsCond(Expr left, Cond right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String toString() {
		return String.format("(%s IS %s)", left, right);
	}
	
}
