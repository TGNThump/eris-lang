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
			var operand = (Integer) evaluateExpression(u.operand());
			return switch (u.operatorKind()){
				case NEGATION -> -operand;
				case IDENTITY -> operand;
			};
		} else if (node instanceof BoundBinaryExpression b){
			var left = (Integer) evaluateExpression(b.left());
			var right = (Integer) evaluateExpression(b.right());

			return switch (b.operatorKind()){
				case Addition -> left + right;
				case Subtraction -> left - right;
				case Multiplication -> left * right;
				case Division -> left / right;
			};
		} else throw new IllegalStateException("Unexpected node " + node.boundNodeKind());
	}
}
