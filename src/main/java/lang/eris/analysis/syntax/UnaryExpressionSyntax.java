package lang.eris.analysis.syntax;

import java.util.Arrays;
import java.util.List;

public final record UnaryExpressionSyntax(
	lang.eris.analysis.syntax.SyntaxToken operator,
	lang.eris.analysis.syntax.ExpressionSyntax operand
) implements lang.eris.analysis.syntax.ExpressionSyntax{

	@Override
	public lang.eris.analysis.syntax.SyntaxKind kind(){
		return lang.eris.analysis.syntax.SyntaxKind.UnaryExpression;
	}

	@Override
	public List<lang.eris.analysis.syntax.SyntaxNode> children(){
		return Arrays.asList(operator, operand);
	}
}
