package lang.eris.analysis.syntax;

public record SyntaxToken(
		lang.eris.analysis.syntax.SyntaxKind kind,
		int position,
		String text,
		Object value
) implements lang.eris.analysis.syntax.SyntaxNode{
}
