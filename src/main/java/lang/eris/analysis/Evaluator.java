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
			return switch (u.operatorKind()){
				case NEGATION -> -(int) operand;
				case IDENTITY -> operand;
			};
		} else if (node instanceof BoundBinaryExpression b){
			var left = evaluateExpression(b.left());
			var right = evaluateExpression(b.right());

			return switch (b.operatorKind()){
				case Addition -> (int) left + (int) right;
				case Subtraction -> (int) left - (int) right;
				case Multiplication -> (int) left * (int) right;
				case Division -> (int) left / (int) right;
			};
		} else throw new IllegalStateException("Unexpected node " + node.boundNodeKind());
	}
}
