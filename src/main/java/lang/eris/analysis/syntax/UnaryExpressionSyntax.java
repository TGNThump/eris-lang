package lang.eris.analysis.syntax;

import java.util.Arrays;
import java.util.List;

public final record UnaryExpressionSyntax(
	SyntaxToken operator,
	ExpressionSyntax operand
) implements ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.UnaryExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(operator, operand);
	}
}
