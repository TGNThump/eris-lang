package lang.eris.analysis.binding;

import lang.eris.analysis.DiagnosticBag;
import lang.eris.analysis.syntax.*;

public class Binder {

	private final DiagnosticBag diagnostics = new DiagnosticBag();

	public DiagnosticBag diagnostics(){
		return diagnostics;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax){

		if (syntax instanceof LiteralExpressionSyntax literal){
			return bindLiteralExpression(literal);
		} else if (syntax instanceof UnaryExpressionSyntax unary){
			return bindUnaryExpression(unary);
		} else if (syntax instanceof BinaryExpressionSyntax binary){
			return bindBinaryExpression(binary);
		} else if (syntax instanceof ParenthesizedExpressionSyntax paren){
			return bindExpression(paren.expression());
		}
		throw new IllegalStateException("Unknown syntax " + syntax.kind());
	}

	private BoundExpression bindLiteralExpression(LiteralExpressionSyntax syntax){
		var value = syntax.value();
		if (value == null) value = 0;
		return new BoundLiteralExpression(value);
	}

	private BoundExpression bindUnaryExpression(UnaryExpressionSyntax syntax){
		var boundOperand = bindExpression(syntax.operand());
		var boundOperator = BoundUnaryOperator.bind(syntax.operator().kind(), boundOperand.type());
		if (boundOperator == null){
			diagnostics.reportUndefinedUnaryOperator(syntax.operator().span(), syntax.operator().text(), boundOperand.type());
			return boundOperand;
		}
		return new BoundUnaryExpression(boundOperator, boundOperand);
	}

	private BoundExpression bindBinaryExpression(BinaryExpressionSyntax syntax){
		var boundLeft = bindExpression(syntax.left());
		var boundRight = bindExpression(syntax.right());
		var boundOperator = BoundBinaryOperator.bind(syntax.operator().kind(), boundLeft.type(), boundRight.type());

		if (boundOperator == null){
			diagnostics.reportUndefinedBinaryOperator(syntax.operator().span(), syntax.operator().text(), boundLeft.type(), boundRight.type());
			return boundLeft;
		}

		return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
	}
}
