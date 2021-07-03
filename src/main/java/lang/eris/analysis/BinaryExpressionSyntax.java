package lang.eris.analysis;

import java.util.Arrays;
import java.util.List;

public record BinaryExpressionSyntax(
	ExpressionSyntax left,
	SyntaxToken operator,
	ExpressionSyntax right
) implements ExpressionSyntax {

	@Override
	public SyntaxKind kind(){
		return SyntaxKind.BinaryExpression;
	}

	@Override
	public List<SyntaxNode> children(){
		return Arrays.asList(left, operator, right);
	}
}
