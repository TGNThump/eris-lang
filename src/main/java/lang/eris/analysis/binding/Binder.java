package lang.eris.analysis.binding;

import lang.eris.analysis.DiagnosticBag;
import lang.eris.analysis.VariableSymbol;
import lang.eris.analysis.syntax.*;

import java.util.Map;

public class Binder {

	private final DiagnosticBag diagnostics = new DiagnosticBag();
	private final Map<VariableSymbol, Object> variables;

	public Binder(Map<VariableSymbol, Object> variables){
		this.variables = variables;
	}

	public DiagnosticBag diagnostics(){
		return diagnostics;
	}

	public BoundExpression bindExpression(ExpressionSyntax syntax){

		if (syntax instanceof ParenthesizedExpressionSyntax paren){
			return bindParenthesizedExpression(paren);
		} else if (syntax instanceof LiteralExpressionSyntax literal){
			return bindLiteralExpression(literal);
		} else if (syntax instanceof UnaryExpressionSyntax unary){
			return bindUnaryExpression(unary);
		} else if (syntax instanceof BinaryExpressionSyntax binary){
			return bindBinaryExpression(binary);
		} else if (syntax instanceof NameExpressionSyntax name){
			return bindNameExpression(name);
		} else if (syntax instanceof AssignmentExpressionSyntax assign){
			return bindAssignmentExpression(assign);
		}
		throw new IllegalStateException("Unknown syntax " + syntax.kind());
	}

	private BoundExpression bindParenthesizedExpression(ParenthesizedExpressionSyntax syntax){
		return bindExpression(syntax.expression());
	}

	private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax){
		var name = syntax.identifierToken().text();
		var boundExpression = bindExpression(syntax.expression());

		var existingVariable = variables.keySet().stream().filter(v -> v.name().equals(name)).findFirst();
		existingVariable.ifPresent(variables::remove);

		var variable = new VariableSymbol(name, boundExpression.type());

		variables.put(variable, null);
		return new BoundAssignmentExpression(variable, boundExpression);
	}

	private BoundExpression bindNameExpression(NameExpressionSyntax syntax){
		var name = syntax.identifierToken().text();

		var variable = variables.keySet().stream().filter(v -> v.name().equals(name)).findFirst();

		if (variable.isEmpty()){
			diagnostics.reportUndefinedName(syntax.identifierToken().span(), name);
			return new BoundLiteralExpression(0);
		}

		return new BoundVariableExpression(variable.get());
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
