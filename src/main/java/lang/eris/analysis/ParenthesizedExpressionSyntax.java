package lang.eris.analysis;

import java.util.Arrays;
import java.util.List;

public record ParenthesizedExpressionSyntax(
		SyntaxToken openParenthesisToken,
		ExpressionSyntax expression,
		SyntaxToken closeParenthesisToken
) implements ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.ParenthesizedExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(openParenthesisToken(), expression(), closeParenthesisToken());
	}
}
