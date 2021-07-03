package lang.eris.analysis;

import java.util.Arrays;
import java.util.List;

public record UnaryExpressionSyntax(
	SyntaxToken operator,
	ExpressionSyntax operand
) implements ExpressionSyntax {

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.UnaryExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(operator(), operand());
	}
}
