package lang.eris.analysis.syntax;

import lang.eris.analysis.DiagnosticBag;
import lang.eris.analysis.TextSpan;

import static java.lang.Character.*;

public final class Lexer{
	private final String text;
	private final DiagnosticBag diagnostics = new DiagnosticBag();
	private int position;

	private int start;
	private Object value;

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
		start = position;
		value = null;

		var kind = switch (current()){
			case '\0' -> SyntaxKind.EndOfFileToken;
			case '+' -> {
				next();
				yield SyntaxKind.PlusToken;
			}
			case '-' -> {
				next();
				yield SyntaxKind.MinusToken;
			}
			case '*' -> {
				next();
				yield SyntaxKind.StarToken;
			}
			case '/' -> {
				next();
				yield SyntaxKind.SlashToken;
			}
			case '(' -> {
				next();
				yield SyntaxKind.OpenParenthesisToken;
			}
			case ')' -> {
				next();
				yield SyntaxKind.CloseParenthesisToken;
			}
			case '!' -> {
				next();
				if (current() == '='){
					next();
					yield SyntaxKind.BangEqualsToken;
				} else yield SyntaxKind.BangToken;
			}
			case '&' -> {
				next();
				if (current() == '&'){
					next();
					yield SyntaxKind.AmpersandAmpersandToken;
				} else yield SyntaxKind.AmpersandToken;
			}
			case '|' -> {
				next();
				if (current() == '|'){
					next();
					yield SyntaxKind.PipePipeToken;
				} else yield SyntaxKind.PipeToken;
			}
			case '=' -> {
				next();
				if (current() == '='){
					next();
					yield SyntaxKind.EqualsEqualsToken;
				} else yield SyntaxKind.EqualsToken;
			}
			case '<' -> {
				next();
				if (current() == '='){
					next();
					yield SyntaxKind.LessThanEqualsToken;
				} else yield SyntaxKind.LessThanToken;
			}
			case '>' -> {
				next();
				if (current() == '='){
					next();
					yield SyntaxKind.GreaterThanEqualsToken;
				} else yield SyntaxKind.GreaterThanToken;
			}
			case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber();
			case ' ', '\t', '\n', '\r' -> readWhitespace();
			default -> {
				if (isLetter(current())) {
					yield readIdentifierOrKeyword();
				} else if (isWhitespace(current())){
					yield readWhitespace();
				}  else {
					diagnostics.reportBadCharacter(start, current());
					next();
					yield SyntaxKind.BadToken;
				}
			}
		};

		var text = SyntaxFacts.getText(kind);
		if (text == null){
			text = this.text.substring(start, position);
		}
		return new SyntaxToken(kind, start, text, value);
	}

	private SyntaxKind readIdentifierOrKeyword() {
		while (isLetter(current())) next();
		String text = this.text.substring(start, position);
		return SyntaxFacts.getKeywordKind(text);
	}

	private SyntaxKind readWhitespace() {
		while (isWhitespace(current())) next();
		return SyntaxKind.WhitespaceToken;
	}

	private SyntaxKind readNumber() {
		while (isDigit(current())) next();
		String text = this.text.substring(start, position);

		try{
			value = Integer.parseInt(text);
		} catch (NumberFormatException nfe){
			diagnostics.reportInvalidNumber(new TextSpan(start, position-start), text, Integer.class);
		}

		return SyntaxKind.NumberToken;
	}
}
