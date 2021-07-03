package lang.eris.analysis.syntax;

import java.util.Arrays;
import java.util.List;

public record ParenthesizedExpressionSyntax(
		SyntaxToken openParenthesisToken,
		lang.eris.analysis.syntax.ExpressionSyntax expression,
		lang.eris.analysis.syntax.SyntaxToken closeParenthesisToken
) implements lang.eris.analysis.syntax.ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return lang.eris.analysis.syntax.SyntaxKind.ParenthesizedExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(openParenthesisToken, expression, closeParenthesisToken);
	}
}
