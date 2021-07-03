package lang.eris.analysis.syntax;

import java.util.Collections;
import java.util.List;

public record LiteralExpressionSyntax(
		SyntaxToken literalToken
) implements lang.eris.analysis.syntax.ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return lang.eris.analysis.syntax.SyntaxKind.LiteralExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Collections.singletonList(literalToken);
	}
}
