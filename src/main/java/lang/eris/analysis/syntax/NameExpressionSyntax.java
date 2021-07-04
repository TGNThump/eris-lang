package lang.eris.analysis.syntax;

import java.util.Collections;
import java.util.List;

public record NameExpressionSyntax(
		SyntaxToken identifierToken
) implements ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.NameExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Collections.singletonList(identifierToken);
	}
}
