package lang.eris.analysis.binding;

import lang.eris.analysis.syntax.*;

import java.util.ArrayList;
import java.util.List;

public class Binder {

	private final List<String> diagnostics = new ArrayList<>();

	public List<String> getDiagnostics(){
		return diagnostics;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax){

		if (syntax instanceof LiteralExpressionSyntax literal){
			return bindLiteralExpression(literal);
		} else if (syntax instanceof UnaryExpressionSyntax unary){
			return bindUnaryExpression(unary);
		} else if (syntax instanceof BinaryExpressionSyntax binary){
			return bindBinaryExpression(binary);
		} else throw new IllegalStateException("Unknown syntax " + syntax.kind());
	}

	private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax){
		var value = (Integer) syntax.literalToken().value();
		if (value == null) value = 0;
		return new BoundLiteralExpression(value);
	}

	private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax){
		var boundOperand = bindExpression(syntax.operand());
		var boundOperatorKind = bindUnaryOperatorKind(syntax.operator().kind(), boundOperand.type());
		if (boundOperatorKind == null){
			diagnostics.add("Unary operator " + syntax.operator().text() + " is not defined for type " + boundOperand.type());
			return boundOperand;
		}
		return new BoundUnaryExpression(boundOperatorKind, boundOperand);
	}

	private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax){
		var boundLeft = bindExpression(syntax.left());
		var boundRight = bindExpression(syntax.right());
		var boundOperatorKind = bindBinaryOperatorKind(syntax.operator().kind(), boundLeft.type(), boundRight.type());

		if (boundOperatorKind == null){
			diagnostics.add("Binary operator " + syntax.operator().text() + " is not defined for types " + boundLeft.type() + " and " + boundRight.type());
			return boundLeft;
		}

		return new BoundBinaryExpression(boundLeft, boundOperatorKind, boundRight);
	}

	private BoundUnaryOperatorKind bindUnaryOperatorKind(SyntaxKind kind, Class<?> operandType){

		if (operandType != Integer.class) return null;

		return switch (kind){
			case PlusToken -> BoundUnaryOperatorKind.IDENTITY;
			case MinusToken -> BoundUnaryOperatorKind.NEGATION;
			default -> throw new IllegalStateException("Unexpected unary operator " + kind);
		};
	}

	private BoundBinaryOperatorKind bindBinaryOperatorKind(SyntaxKind kind, Class<?> leftType, Class<?> rightType){

		if (leftType != Integer.class || rightType != Integer.class) return null;

		return switch (kind){
			case PlusToken -> BoundBinaryOperatorKind.Addition;
			case MinusToken -> BoundBinaryOperatorKind.Subtraction;
			case StarToken -> BoundBinaryOperatorKind.Multiplication;
			case SlashToken -> BoundBinaryOperatorKind.Division;
			default -> throw new IllegalStateException("Unexpected binary operator " + kind);
		};
	}


}
