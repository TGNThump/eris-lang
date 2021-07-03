package lang.eris.analysis;

public record SyntaxToken(SyntaxKind kind, int position, String text, Object value) implements SyntaxNode{
}
