package lang.eris.analysis.syntax;

import lang.eris.analysis.DiagnosticBag;
import lang.eris.analysis.TextSpan;

public final class Lexer{
	private final String text;
	private final DiagnosticBag diagnostics = new DiagnosticBag();
	private int position;

	public Lexer(String text){
		this.text = text;
	}

	public DiagnosticBag diagnostics(){
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

		int start = position;
		char current = current();

		if (Character.isDigit(current)){

			while (Character.isDigit(current())){
				next();
			}

			String text = this.text.substring(start, position);

			int value = 0;
			try{
				value = Integer.parseInt(text);
			} catch (NumberFormatException nfe){
				diagnostics.reportInvalidNumber(new TextSpan(start, position-start), text, Integer.class);
			}

			return new SyntaxToken(SyntaxKind.LiteralToken, start, text, value);
		}

		if (Character.isWhitespace(current)){

			while (Character.isWhitespace(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			return new SyntaxToken(SyntaxKind.WhitespaceToken, start, stringValue, null);
		}

		if (Character.isLetter(current())){

			while (Character.isLetter(current())){
				next();
			}

			String stringValue = text.substring(start, position);
			var kind = SyntaxFacts.getKeywordKind(stringValue);
			return new SyntaxToken(kind, start, stringValue, null);
		}

		next();
		char lookahead = current();

		return switch (current){
			case '+' -> new SyntaxToken(SyntaxKind.PlusToken, start, "+", null);
			case '-' -> new SyntaxToken(SyntaxKind.MinusToken, start, "-", null);
			case '*' -> new SyntaxToken(SyntaxKind.StarToken, start, "*", null);
			case '/' -> new SyntaxToken(SyntaxKind.SlashToken, start, "/", null);
			case '(' -> new SyntaxToken(SyntaxKind.OpenParenthesisToken, start, "(", null);
			case ')' -> new SyntaxToken(SyntaxKind.CloseParenthesisToken, start, ")", null);
			case '!' -> {
				if (lookahead == '='){
					next();
					yield new SyntaxToken(SyntaxKind.BangEqualsToken, start, "!=", null);
				} else {
					yield new SyntaxToken(SyntaxKind.BangToken, start, "!", null);
				}
			}
			case '&' -> {
				if (lookahead == '&'){
					next();
					yield new SyntaxToken(SyntaxKind.AmpersandAmpersandToken, start, "&&", null);
				} else {
					yield new SyntaxToken(SyntaxKind.AmpersandToken, start, "&", null);
				}
			}
			case '|' -> {
				if (lookahead == '|'){
					next();
					yield new SyntaxToken(SyntaxKind.PipePipeToken, start, "||", null);
				} else {
					yield new SyntaxToken(SyntaxKind.PipeToken, start, "|", null);
				}
			}
			case '=' -> {
				if (lookahead == '='){
					next();
					yield new SyntaxToken(SyntaxKind.EqualsEqualsToken, start, "==", null);
				} else {
					yield new SyntaxToken(SyntaxKind.EqualsToken, start, "=", null);
				}
			}
			case '<' -> {
				if (lookahead == '='){
					next();
					yield new SyntaxToken(SyntaxKind.LessThanEqualsToken, start, "==", null);
				} else {
					yield new SyntaxToken(SyntaxKind.LessThanToken, start, "=", null);
				}
			}
			case '>' -> {
				if (lookahead == '='){
					next();
					yield new SyntaxToken(SyntaxKind.GreaterThanEqualsToken, start, "==", null);
				} else {
					yield new SyntaxToken(SyntaxKind.GreaterThanToken, start, "=", null);
				}
			}
			default -> {
				diagnostics.reportBadCharacter(start, current());
				yield new SyntaxToken(SyntaxKind.BadToken, start, text.substring(position-1, position), null);
			}
		};
	}
}
