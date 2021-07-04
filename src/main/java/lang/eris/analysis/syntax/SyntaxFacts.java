package lang.eris.analysis.syntax;

public final class SyntaxFacts{
	private SyntaxFacts(){}

	public static int getUnaryOperatorPrecedence(SyntaxKind kind){
		return switch (kind){
			case PlusToken, MinusToken -> 3;
			default -> 0;
		};
	}

	public static int getBinaryOperatorPrecedence(lang.eris.analysis.syntax.SyntaxKind kind){
		return switch (kind){
			case StarToken, SlashToken -> 2;
			case PlusToken, MinusToken -> 1;
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
