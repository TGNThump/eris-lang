package lang.eris.analysis.syntax;

import java.util.ArrayList;
import java.util.List;

public final class Parser{
	private final List<SyntaxToken> tokens;
	private final List<String> diagnostics = new ArrayList<>();
	private int position;

	public Parser(String text){
		tokens = new ArrayList<>();

		var lexer = new lang.eris.analysis.syntax.Lexer(text);
		lang.eris.analysis.syntax.SyntaxToken token;
		do {
			token = lexer.lex();

			if (token.kind() != SyntaxKind.WhitespaceToken && token.kind() != lang.eris.analysis.syntax.SyntaxKind.BadToken){
				tokens.add(token);
			}
		} while (token.kind() != lang.eris.analysis.syntax.SyntaxKind.EndOfFileToken);

		diagnostics.addAll(lexer.getDiagnostics());
	}

	private lang.eris.analysis.syntax.SyntaxToken peek(int offset){
		var index = position + offset;
		if (index >= tokens.size())
			return tokens.get(tokens.size()-1);

		return tokens.get(index);
	}

	private lang.eris.analysis.syntax.SyntaxToken current(){
		return peek(0);
	}

	private lang.eris.analysis.syntax.SyntaxToken nextToken(){
		var current = current();
		position++;
		return current;
	}

	public SyntaxTree parse(){
		var expression = parseExpression();
		var endOfFileToken = matchToken(lang.eris.analysis.syntax.SyntaxKind.EndOfFileToken);
		return new lang.eris.analysis.syntax.SyntaxTree(diagnostics, expression, endOfFileToken);
	}

	private lang.eris.analysis.syntax.SyntaxToken matchToken(lang.eris.analysis.syntax.SyntaxKind kind){
		if (current().kind() == kind){
			return nextToken();
		}

		diagnostics.add("Unexpected token <" + current().kind() +">, expected <" + kind + ">");
		return new lang.eris.analysis.syntax.SyntaxToken(kind, current().position(), null, null);
	}

	private lang.eris.analysis.syntax.ExpressionSyntax parseExpression(){
		return parseExpression(0);
	}

	@SuppressWarnings("InfiniteRecursion")
	private lang.eris.analysis.syntax.ExpressionSyntax parseExpression(int parentPrecedence){
		lang.eris.analysis.syntax.ExpressionSyntax left;
		var unaryOperatorPrecedence = SyntaxFacts.getUnaryOperatorPrecedence(current().kind());
		if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence){
			var operatorToken = nextToken();
			var operand = parseExpression(unaryOperatorPrecedence);
			left = new UnaryExpressionSyntax(operatorToken, operand);
		} else {
			left = parsePrimaryExpression();
		}

		while (true){
			var precedence = lang.eris.analysis.syntax.SyntaxFacts.getBinaryOperatorPrecedence(current().kind());
			if (precedence == 0 || precedence <= parentPrecedence) break;

			var operatorToken = nextToken();
			var right = parseExpression(precedence);
			left = new lang.eris.analysis.syntax.BinaryExpressionSyntax(left, operatorToken, right);
		}

		return left;
	}

	private lang.eris.analysis.syntax.ExpressionSyntax parsePrimaryExpression(){
		if (current().kind() == lang.eris.analysis.syntax.SyntaxKind.OpenParenthesisToken){
			var left = nextToken();
			var expression = parseExpression();
			var right = matchToken(lang.eris.analysis.syntax.SyntaxKind.CloseParenthesisToken);
			return new lang.eris.analysis.syntax.ParenthesizedExpressionSyntax(left, expression, right);
		}

		var literalToken = matchToken(lang.eris.analysis.syntax.SyntaxKind.LiteralToken);
		return new lang.eris.analysis.syntax.LiteralExpressionSyntax(literalToken);
	}
}
