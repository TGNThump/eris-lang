package lang.eris.analysis.syntax;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.params.provider.Arguments.of;

public class LexerTests{

	@SuppressWarnings("unused")
	private static Stream<Arguments> tokens(){
		return Stream.of(
				of(SyntaxKind.PlusToken, "+"),
				of(SyntaxKind.MinusToken, "-"),
				of(SyntaxKind.StarToken, "*"),
				of(SyntaxKind.SlashToken, "/"),
				of(SyntaxKind.BangToken, "!"),
				of(SyntaxKind.BangEqualsToken, "!="),
				of(SyntaxKind.AmpersandAmpersandToken, "&&"),
				of(SyntaxKind.AmpersandToken, "&"),
				of(SyntaxKind.PipePipeToken, "||"),
				of(SyntaxKind.PipeToken, "|"),
				of(SyntaxKind.EqualsEqualsToken, "=="),
				of(SyntaxKind.EqualsToken, "="),
				of(SyntaxKind.LessThanEqualsToken, "<="),
				of(SyntaxKind.LessThanToken, "<"),
				of(SyntaxKind.GreaterThanEqualsToken, ">="),
				of(SyntaxKind.GreaterThanToken, ">"),
				of(SyntaxKind.OpenParenthesisToken, "("),
				of(SyntaxKind.CloseParenthesisToken, ")"),
				of(SyntaxKind.FalseKeyword, "false"),
				of(SyntaxKind.TrueKeyword, "true"),
				of(SyntaxKind.WhitespaceToken, " "),
				of(SyntaxKind.WhitespaceToken, "  "),
				of(SyntaxKind.WhitespaceToken, "\r"),
				of(SyntaxKind.WhitespaceToken, "\n"),
				of(SyntaxKind.WhitespaceToken, "\r\n"),
				of(SyntaxKind.NumberToken, "1"),
				of(SyntaxKind.NumberToken, "123"),
				of(SyntaxKind.IdentifierToken, "a"),
				of(SyntaxKind.IdentifierToken, "abc")
		);
	}

	private static boolean requiresSeperator(SyntaxKind t1Kind, SyntaxKind t2Kind){

		if (t1Kind == SyntaxKind.IdentifierToken && t2Kind == SyntaxKind.IdentifierToken) return true;
		if (t1Kind == SyntaxKind.NumberToken && t2Kind == SyntaxKind.NumberToken) return true;
		if (t1Kind == SyntaxKind.BangToken && t2Kind == SyntaxKind.EqualsToken) return true;
		if (t1Kind == SyntaxKind.EqualsToken && t2Kind == SyntaxKind.EqualsToken) return true;
		if (t1Kind == SyntaxKind.EqualsToken && t2Kind == SyntaxKind.EqualsEqualsToken) return true;
		if (t1Kind == SyntaxKind.BangToken && t2Kind == SyntaxKind.EqualsEqualsToken) return true;
		if (t1Kind == SyntaxKind.AmpersandToken && t2Kind == SyntaxKind.AmpersandAmpersandToken) return true;
		if (t1Kind == SyntaxKind.AmpersandToken && t2Kind == SyntaxKind.AmpersandToken) return true;
		if (t1Kind == SyntaxKind.PipeToken && t2Kind == SyntaxKind.PipePipeToken) return true;
		if (t1Kind == SyntaxKind.PipeToken && t2Kind == SyntaxKind.PipeToken) return true;
		if (t1Kind == SyntaxKind.LessThanToken && t2Kind == SyntaxKind.EqualsEqualsToken) return true;
		if (t1Kind == SyntaxKind.LessThanToken && t2Kind == SyntaxKind.EqualsToken) return true;
		if (t1Kind == SyntaxKind.GreaterThanToken && t2Kind == SyntaxKind.EqualsEqualsToken) return true;
		if (t1Kind == SyntaxKind.GreaterThanToken && t2Kind == SyntaxKind.EqualsToken) return true;
		if (t1Kind == SyntaxKind.WhitespaceToken && t2Kind == SyntaxKind.WhitespaceToken) return true;

		var t1IsKeyword = t1Kind.toString().endsWith("Keyword");
		var t2IsKeyword = t2Kind.toString().endsWith("Keyword");

		if (t1IsKeyword && t2IsKeyword) return true;
		if (t1IsKeyword && t2Kind == SyntaxKind.IdentifierToken) return true;
		if (t1Kind == SyntaxKind.IdentifierToken && t2IsKeyword) return true;

		return false;
	}

	@SuppressWarnings("unused")
	private static Stream<Arguments> tokenPairs(){
		return tokens()
				.flatMap(a -> tokens().map(b -> of(a.get()[0], a.get()[1], b.get()[0], b.get()[1])))
				.filter(args -> {
					SyntaxKind t1Kind = (SyntaxKind) args.get()[0];
					SyntaxKind t2Kind = (SyntaxKind) args.get()[2];

					return !requiresSeperator(t1Kind, t2Kind);
				});
	}

	@ParameterizedTest
	@MethodSource("tokens")
	public void lexerLexesToken(SyntaxKind kind, String text){
		var tokens = SyntaxTree.parseTokens(text);

		assertThat(tokens)
				.singleElement()
				.extracting(SyntaxToken::kind, SyntaxToken::text)
				.containsExactly(kind, text);
	}

	@ParameterizedTest
	@MethodSource("tokenPairs")
	public void lexerLexesTokenPairs(SyntaxKind aKind, String aText, SyntaxKind bKind, String bText){
		var tokens = SyntaxTree.parseTokens(aText + bText);

		assertThat(tokens)
				.elements(0, 1)
				.extracting(SyntaxToken::kind, SyntaxToken::text)
				.containsExactly(tuple(aKind, aText), tuple(bKind, bText));
	}

}
