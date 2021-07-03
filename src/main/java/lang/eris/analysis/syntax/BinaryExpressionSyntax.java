package lang.eris.analysis.syntax;

import java.util.Arrays;
import java.util.List;

public record BinaryExpressionSyntax(
	ExpressionSyntax left,
	SyntaxToken operator,
	lang.eris.analysis.syntax.ExpressionSyntax right
) implements lang.eris.analysis.syntax.ExpressionSyntax{

	@Override
	public SyntaxKind kind(){
		return lang.eris.analysis.syntax.SyntaxKind.BinaryExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(left, operator, right);
	}
}
