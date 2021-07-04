package lang.eris.analysis.syntax;

import java.util.Collections;
import java.util.List;

public record LiteralExpressionSyntax(
		SyntaxToken literalToken,
		Object value
) implements ExpressionSyntax{

	public LiteralExpressionSyntax(SyntaxToken literalToken){
		this(literalToken, literalToken.value());
	}

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.LiteralExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Collections.singletonList(literalToken);
	}
}
