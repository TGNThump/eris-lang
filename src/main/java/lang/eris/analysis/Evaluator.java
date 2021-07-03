package lang.eris.analysis;

public class Evaluator{
	private final ExpressionSyntax root;

	public Evaluator(ExpressionSyntax root){
		this.root = root;
	}

	public Object evaluate(){
		return evaluateExpression(root);
	}

	private Object evaluateExpression(ExpressionSyntax node){
		if (node instanceof LiteralExpressionSyntax l){
			return l.literalToken().value();
		} else if (node instanceof UnaryExpressionSyntax u){
			var operand = evaluateExpression(u.operand());
			return switch (u.operator().kind()){
				case MinusToken -> -(int) operand;
				case PlusToken -> operand;
				default -> throw new IllegalStateException("Unexpected unary operator " + u.operator().kind());
			};
		} else if (node instanceof BinaryExpressionSyntax b){
			var left = evaluateExpression(b.left());
			var right = evaluateExpression(b.right());

			return switch (b.operator().kind()){
				case PlusToken -> (int) left + (int) right;
				case MinusToken -> (int) left - (int) right;
				case StarToken -> (int) left * (int) right;
				case SlashToken -> (int) left / (int) right;
				default -> throw new IllegalStateException("Unexpected binary operator " + b.operator().kind());
			};
		} else if (node instanceof ParenthesizedExpressionSyntax p){
			return evaluateExpression(p.expression());
		} else throw new IllegalStateException("Unexpected node " + node.kind());
	}
}
