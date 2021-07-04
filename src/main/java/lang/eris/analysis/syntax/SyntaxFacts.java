package lang.eris.analysis.syntax;

public final class SyntaxFacts{
	private SyntaxFacts(){}

	public static int getUnaryOperatorPrecedence(SyntaxKind kind){
		return switch (kind){
			case PlusToken, MinusToken, BangToken -> 6;
			default -> 0;
		};
	}

	public static int getBinaryOperatorPrecedence(SyntaxKind kind){
		return switch (kind){
			case StarToken, SlashToken -> 5;
			case PlusToken, MinusToken -> 4;
			case EqualsEqualsToken, BangEqualsToken, LessThanToken, LessThanEqualsToken, GreaterThanToken, GreaterThanEqualsToken -> 3;
			case AmpersandAmpersandToken -> 2;
			case PipePipeToken ->  1;
			default -> 0;
		};
	}

	public static SyntaxKind getKeywordKind(String keyword){
		return switch (keyword){
			case "true" -> SyntaxKind.TrueKeyword;
			case "false" -> SyntaxKind.FalseKeyword;
			default -> SyntaxKind.IdentifierToken;
		};
	}

	public static String getText(SyntaxKind kind){
		return switch (kind){
			case PlusToken -> "+";
			case MinusToken -> "-";
			case StarToken -> "*";
			case SlashToken -> "/";
			case BangToken -> "!";
			case BangEqualsToken -> "!=";
			case AmpersandAmpersandToken -> "&&";
			case AmpersandToken -> "&";
			case PipePipeToken -> "||";
			case PipeToken -> "|";
			case EqualsEqualsToken -> "==";
			case EqualsToken -> "=";
			case LessThanEqualsToken -> "<=";
			case LessThanToken -> "<";
			case GreaterThanEqualsToken -> ">=";
			case GreaterThanToken -> ">";
			case OpenParenthesisToken -> "(";
			case CloseParenthesisToken -> ")";
			default -> null;
		};
	}
}
