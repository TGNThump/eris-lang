package lang.eris.analysis;

import lang.eris.analysis.binding.*;

import java.util.Map;
import java.util.Objects;

public final class Evaluator{
	private final BoundExpression root;
	private final Map<VariableSymbol, Object> variables;

	public Evaluator(BoundExpression root, Map<VariableSymbol, Object> variables){
		this.root = root;
		this.variables = variables;
	}

	public Object evaluate(){
		return evaluateExpression(root);
	}

	private Object evaluateExpression(BoundExpression node){
		return switch (node.boundNodeKind()){
			case LiteralExpression -> evaluateLiteralExpression((BoundLiteralExpression) node);
			case BinaryExpression -> evaluateBinaryExpression((BoundBinaryExpression) node);
			case UnaryExpression -> evaluateUnaryExpression((BoundUnaryExpression) node);
			case VariableExpression -> evaluateVariableExpression((BoundVariableExpression) node);
			case AssignmentExpression -> evaluateAssignmentExpression((BoundAssignmentExpression) node);
		};
	}

	private Object evaluateLiteralExpression(BoundLiteralExpression l) {
		return l.value();
	}

	private Object evaluateVariableExpression(BoundVariableExpression v) {
		return variables.get(v.variableSymbol());
	}

	private Object evaluateAssignmentExpression(BoundAssignmentExpression a) {
		var value = evaluateExpression(a.boundExpression());
		variables.put(a.variableSymbol(), value);
		return value;
	}

	private Object evaluateUnaryExpression(BoundUnaryExpression u) {
		var operand = evaluateExpression(u.operand());
		return switch (u.operator().kind()){
			case Identity -> operand;
			case Negation -> -(Integer) operand;
			case LogicalNegation -> !(Boolean) operand;
		};
	}

	private Object evaluateBinaryExpression(BoundBinaryExpression b) {
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
	}

	@Override
	public boolean equals(Object obj){
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Evaluator) obj;
		return Objects.equals(this.root, that.root) && Objects.equals(this.variables, that.variables);
	}

	@Override
	public int hashCode(){
		return Objects.hash(root, variables);
	}

	@Override
	public String toString(){
		return "Evaluator[" + "root=" + root + ", " + "variables=" + variables + ']';
	}

}
