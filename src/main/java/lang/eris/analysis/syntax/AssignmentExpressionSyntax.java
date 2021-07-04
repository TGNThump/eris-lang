package lang.eris.analysis.syntax;

import java.util.Arrays;
import java.util.List;

public record AssignmentExpressionSyntax (
		SyntaxToken identifierToken,
		SyntaxToken equalsToken,
		ExpressionSyntax expression
) implements ExpressionSyntax{
	@Override
	public SyntaxKind kind(){
		return SyntaxKind.AssignmentExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(identifierToken, equalsToken, expression);
	}
}
