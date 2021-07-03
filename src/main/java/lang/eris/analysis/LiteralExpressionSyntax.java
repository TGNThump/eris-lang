package lang.eris.analysis;

import java.util.Collections;
import java.util.List;

public record LiteralExpressionSyntax(SyntaxToken literalToken) implements ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.LiteralExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Collections.singletonList(literalToken());
	}
}
