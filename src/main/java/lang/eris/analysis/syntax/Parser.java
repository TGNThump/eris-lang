package lang.eris.analysis.syntax;

import lang.eris.analysis.DiagnosticBag;

import java.util.ArrayList;
import java.util.List;

public final class Parser{
	private final List<SyntaxToken> tokens;
	private final DiagnosticBag diagnostics = new DiagnosticBag();
	private int position;

	public Parser(String text){
		tokens = new ArrayList<>();

		var lexer = new Lexer(text);
		SyntaxToken token;
		do {
			token = lexer.lex();

			if (token.kind() != SyntaxKind.WhitespaceToken && token.kind() != SyntaxKind.BadToken){
				tokens.add(token);
			}
		} while (token.kind() != SyntaxKind.EndOfFileToken);

		diagnostics.addAll(lexer.diagnostics());
	}

	public DiagnosticBag diagnostics(){
		return diagnostics;
	}

	private SyntaxToken peek(int offset){
		var index = position + offset;
		if (index >= tokens.size())
			return tokens.get(tokens.size()-1);

		return tokens.get(index);
	}

	private SyntaxToken current(){
		return peek(0);
	}

	private SyntaxToken nextToken(){
		var current = current();
		position++;
		return current;
	}

	public SyntaxTree parse(){
		var expression = parseExpression();
		var endOfFileToken = matchToken(SyntaxKind.EndOfFileToken);
		return new SyntaxTree(diagnostics, expression, endOfFileToken);
	}

	private SyntaxToken matchToken(SyntaxKind kind){
		if (current().kind() == kind){
			return nextToken();
		}

		diagnostics.reportUnexpectedToken(current().span(), current().kind(), kind);
		return new SyntaxToken(kind, current().position(), current().text(), null);
	}

	private ExpressionSyntax parseExpression(){
		return parseAssignmentExpression();
	}

	private ExpressionSyntax parseAssignmentExpression(){
		if (peek(0).kind() == SyntaxKind.IdentifierToken && peek(1).kind() == SyntaxKind.EqualsToken){
			var identifierToken = nextToken();
			var operatorToken = nextToken();
			var right = parseAssignmentExpression();
			return new AssignmentExpressionSyntax(identifierToken, operatorToken, right);
		}

		return parseBinaryExpression();
	}

	private ExpressionSyntax parseBinaryExpression(){
		return parseBinaryExpression(0);
	}

	@SuppressWarnings("InfiniteRecursion")
	private ExpressionSyntax parseBinaryExpression(int parentPrecedence){
		ExpressionSyntax left;
		var unaryOperatorPrecedence = SyntaxFacts.getUnaryOperatorPrecedence(current().kind());
		if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence){
			var operatorToken = nextToken();
			var operand = parseBinaryExpression(unaryOperatorPrecedence);
			left = new UnaryExpressionSyntax(operatorToken, operand);
		} else {
			left = parsePrimaryExpression();
		}

		while (true){
			var precedence = SyntaxFacts.getBinaryOperatorPrecedence(current().kind());
			if (precedence == 0 || precedence <= parentPrecedence) break;

			var operatorToken = nextToken();
			var right = parseBinaryExpression(precedence);
			left = new BinaryExpressionSyntax(left, operatorToken, right);
		}

		return left;
	}

	private ExpressionSyntax parsePrimaryExpression(){
		return switch (current().kind()){
			case OpenParenthesisToken -> {
				var left = nextToken();
				var expression = parseExpression();
				var right = matchToken(SyntaxKind.CloseParenthesisToken);
				yield new ParenthesizedExpressionSyntax(left, expression, right);
			}
			case FalseKeyword, TrueKeyword -> {
				var keywordToken = nextToken();
				var value = keywordToken.kind() == SyntaxKind.TrueKeyword;
				yield new LiteralExpressionSyntax(keywordToken, value);
			}
			case IdentifierToken -> {
				var identifierToken = nextToken();
				yield new NameExpressionSyntax(identifierToken);
			}
			default -> {
				var literalToken = matchToken(SyntaxKind.NumberToken);
				yield new LiteralExpressionSyntax(literalToken);
			}
		};
	}
}
