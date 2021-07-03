package lang.eris.analysis;

public class SyntaxFacts{
	public static int getBinaryOperatorPrecedence(SyntaxKind kind){
		return switch (kind){
			case StarToken, SlashToken -> 2;
			case PlusToken, MinusToken -> 1;
			default -> 0;
		};
	}
}
