package lang.eris.analysis.syntax;

public final class SyntaxFacts{
	private SyntaxFacts(){}

	public static int getUnaryOperatorPrecedence(SyntaxKind kind){
		return switch (kind){
			case PlusToken, MinusToken, BangToken -> 5;
			default -> 0;
		};
	}

	public static int getBinaryOperatorPrecedence(SyntaxKind kind){
		return switch (kind){
			case StarToken, SlashToken -> 4;
			case PlusToken, MinusToken -> 3;
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
}
