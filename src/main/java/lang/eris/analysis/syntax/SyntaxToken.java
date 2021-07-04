package lang.eris.analysis.syntax;

import lang.eris.analysis.TextSpan;

public record SyntaxToken(
		SyntaxKind kind,
		int position,
		String text,
		Object value,
		TextSpan span
) implements SyntaxNode{
	public SyntaxToken(SyntaxKind kind, int position, String text, Object value){
		this(kind, position, text, value, new TextSpan(position, text.length()));
	}
}
