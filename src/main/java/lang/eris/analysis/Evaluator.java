package lang.eris.analysis;

import lang.eris.analysis.binding.BoundBinaryExpression;
import lang.eris.analysis.binding.BoundExpression;
import lang.eris.analysis.binding.BoundLiteralExpression;
import lang.eris.analysis.binding.BoundUnaryExpression;

public record Evaluator(BoundExpression root){

	public Object evaluate(){
		return evaluateExpression(root);
	}

	private Object evaluateExpression(BoundExpression node){
		if (node instanceof BoundLiteralExpression l){
			return l.value();
		} else if (node instanceof BoundUnaryExpression u){
			var operand = evaluateExpression(u.operand());
			return switch (u.operator().kind()){
				case Identity -> operand;
				case Negation -> -(Integer) operand;
				case LogicalNegation -> !(Boolean) operand;
			};
		} else if (node instanceof BoundBinaryExpression b){
			var left = evaluateExpression(b.left());
			var right = evaluateExpression(b.right());

			return switch (b.operator().kind()){
				case Addition -> (Integer) left + (Integer) right;
				case Subtraction -> (Integer) left - (Integer) right;
				case Multiplication -> (Integer) left * (Integer) right;
				case Division -> (Integer) left / (Integer) right;
				case Equals -> left.equals(right);
				case NotEquals -> !left.equals(right);
				case LessThan -> (Integer) left < (Integer) right;
				case LessThanEquals -> (Integer) left <= (Integer) right;
				case GreaterThan -> (Integer) left > (Integer) right;
				case GreaterThanEquals -> (Integer) left >= (Integer) right;
				case LogicalAnd -> (Boolean) left && (Boolean) right;
				case LogicalOr -> (Boolean) left || (Boolean) right;
			};
		} else throw new IllegalStateException("Unexpected node " + node.boundNodeKind());
	}
}
