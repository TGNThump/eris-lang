package lang.eris.analysis.syntax;

import java.util.ArrayList;
import java.util.List;

public final class Lexer{
	private final String text;
	private final List<String> diagnostics = new ArrayList<>();
	private int position;

	public Lexer(String text){
		this.text = text;
	}

	public List<String> getDiagnostics(){
		return diagnostics;
	}

	private char peek(int offset){
		var index = position + offset;
		if (index >= text.length()){
			return '\0';
		} else return text.charAt(index);
	}

	private char current(){
		return peek(0);
	}

	private void next(){
		position++;
	}

	public SyntaxToken lex(){
		if (position >= text.length()){
			return new SyntaxToken(SyntaxKind.EndOfFileToken, position, "", null);
		}

		if (Character.isDigit(current())){
			var start = position;

			while (Character.isDigit(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			int value = Integer.parseInt(stringValue);

			return new SyntaxToken(SyntaxKind.LiteralToken, start, stringValue, value);
		}

		if (Character.isWhitespace(current())){
			var start = position;

			while (Character.isWhitespace(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			return new SyntaxToken(SyntaxKind.WhitespaceToken, start, stringValue, null);
		}

		if (Character.isLetter(current())){
			var start = position;

			while (Character.isLetter(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			var kind = SyntaxFacts.getKeywordKind(stringValue);
			return new SyntaxToken(kind, start, stringValue, null);
		}

		return switch (current()){
			case '+' -> new SyntaxToken(SyntaxKind.PlusToken, position++, "+", null);
			case '-' -> new SyntaxToken(SyntaxKind.MinusToken, position++, "-", null);
			case '*' -> new SyntaxToken(SyntaxKind.StarToken, position++, "*", null);
			case '/' -> new SyntaxToken(SyntaxKind.SlashToken, position++, "/", null);
			case '(' -> new SyntaxToken(SyntaxKind.OpenParenthesisToken, position++, "(", null);
			case ')' -> new SyntaxToken(SyntaxKind.CloseParenthesisToken, position++, ")", null);
			case '!' -> new SyntaxToken(SyntaxKind.BangToken, position++, "!", null);
			case '&' -> {
				if (peek(1) == '&'){
					yield new SyntaxToken(SyntaxKind.AmpersandAmpersandToken, position += 2, "&&", null);
				} else {
					yield new SyntaxToken(SyntaxKind.AmpersandToken, position++, "&", null);
				}
			}
			case '|' -> {
				if (peek(1) == '|'){
					yield new SyntaxToken(SyntaxKind.PipePipeToken, position += 2, "||", null);
				} else {
					yield new SyntaxToken(SyntaxKind.PipeToken, position++, "|", null);
				}
			}
			default -> {
				diagnostics.add("ERROR: bad character input " + current());
				yield new SyntaxToken(SyntaxKind.BadToken, position++, text.substring(position-1, position), null);
			}
		};
	}
}
