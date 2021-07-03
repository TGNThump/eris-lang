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

	private char current(){
		if (position >= text.length()){
			return '\0';
		} else return text.charAt(position);
	}

	private void next(){
		position++;
	}

	public SyntaxToken lex(){
		if (position >= text.length()){
			return new lang.eris.analysis.syntax.SyntaxToken(SyntaxKind.EndOfFileToken, position, "", null);
		}

		if (Character.isDigit(current())){
			var start = position;

			while (Character.isDigit(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			int value = Integer.parseInt(stringValue);

			return new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.LiteralToken, start, stringValue, value);
		}

		if (Character.isWhitespace(current())){
			var start = position;

			while (Character.isWhitespace(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			return new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.WhitespaceToken, start, stringValue, null);
		}

		return switch (current()){
			case '+' -> new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.PlusToken, position++, "+", null);
			case '-' -> new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.MinusToken, position++, "-", null);
			case '*' -> new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.StarToken, position++, "*", null);
			case '/' -> new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.SlashToken, position++, "/", null);
			case '(' -> new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.OpenParenthesisToken, position++, "(", null);
			case ')' -> new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.CloseParenthesisToken, position++, ")", null);
			default -> {
				diagnostics.add("ERROR: bad character input " + current());
				yield new lang.eris.analysis.syntax.SyntaxToken(lang.eris.analysis.syntax.SyntaxKind.BadToken, position++, text.substring(position-1, position), null);
			}
		};
	}
}
