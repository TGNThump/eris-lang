package lang.eris.analysis.syntax;

public record SyntaxToken(
		SyntaxKind kind,
		int position,
		String text,
		Object value
) implements SyntaxNode{
}
