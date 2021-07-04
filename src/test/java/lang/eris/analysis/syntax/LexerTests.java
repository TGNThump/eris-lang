package lang.eris.analysis.syntax;

import org.junit.jupiter.params.ParameterizedTest;
 import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class LexerTests{

	private record SyntaxExample(SyntaxKind kind, String text){}

	@SuppressWarnings("unused")
	private static Stream<SyntaxExample> tokens(){
		return Stream.concat(
//				Static tokens
				Arrays.stream(SyntaxKind.values()).map(k -> new SyntaxExample(k, SyntaxFacts.getText(k))).filter(ex -> ex.text != null),
//				Dynamic tokens
				Stream.of(
						new SyntaxExample(SyntaxKind.NumberToken, "1"),
						new SyntaxExample(SyntaxKind.NumberToken, "123"),
						new SyntaxExample(SyntaxKind.IdentifierToken, "a"),
						new SyntaxExample(SyntaxKind.IdentifierToken, "abc")
				)
		);
	}

	@SuppressWarnings("unused")
	private static Stream<SyntaxExample> separators(){
		return Stream.of(
				new SyntaxExample(SyntaxKind.WhitespaceToken, " "),
				new SyntaxExample(SyntaxKind.WhitespaceToken, "  "),
				new SyntaxExample(SyntaxKind.WhitespaceToken, "\r"),
				new SyntaxExample(SyntaxKind.WhitespaceToken, "\n"),
				new SyntaxExample(SyntaxKind.WhitespaceToken, "\r\n")
		);
	}

	private static boolean requiresSeparator(SyntaxKind k1, SyntaxKind k2){

		if (k1 == SyntaxKind.IdentifierToken && k2 == SyntaxKind.IdentifierToken) return true;
		if (k1 == SyntaxKind.NumberToken && k2 == SyntaxKind.NumberToken) return true;
		if (k1 == SyntaxKind.BangToken && k2 == SyntaxKind.EqualsToken) return true;
		if (k1 == SyntaxKind.EqualsToken && k2 == SyntaxKind.EqualsToken) return true;
		if (k1 == SyntaxKind.EqualsToken && k2 == SyntaxKind.EqualsEqualsToken) return true;
		if (k1 == SyntaxKind.BangToken && k2 == SyntaxKind.EqualsEqualsToken) return true;
		if (k1 == SyntaxKind.AmpersandToken && k2 == SyntaxKind.AmpersandAmpersandToken) return true;
		if (k1 == SyntaxKind.AmpersandToken && k2 == SyntaxKind.AmpersandToken) return true;
		if (k1 == SyntaxKind.PipeToken && k2 == SyntaxKind.PipePipeToken) return true;
		if (k1 == SyntaxKind.PipeToken && k2 == SyntaxKind.PipeToken) return true;
		if (k1 == SyntaxKind.LessThanToken && k2 == SyntaxKind.EqualsEqualsToken) return true;
		if (k1 == SyntaxKind.LessThanToken && k2 == SyntaxKind.EqualsToken) return true;
		if (k1 == SyntaxKind.GreaterThanToken && k2 == SyntaxKind.EqualsEqualsToken) return true;
		if (k1 == SyntaxKind.GreaterThanToken && k2 == SyntaxKind.EqualsToken) return true;

		var k1IsKeyword = k1.toString().endsWith("Keyword");
		var k2IsKeyword = k2.toString().endsWith("Keyword");

		if (k1IsKeyword && k2IsKeyword) return true;
		if (k1IsKeyword && k2 == SyntaxKind.IdentifierToken) return true;
		if (k1 == SyntaxKind.IdentifierToken && k2IsKeyword) return true;

		return false;
	}

	@SuppressWarnings("unused")
	private static Stream<List<SyntaxExample>> tokenSequences(){
		return tokens()
				.flatMap(a -> tokens().flatMap(b -> {
					if (requiresSeparator(a.kind, b.kind)){
						return separators().map(sep -> Arrays.asList(a, sep, b));
					} else {
						return Stream.of(Arrays.asList(a, b));
					}
				}));
	}

	@ParameterizedTest
	@MethodSource({"tokens", "separators"})
	public void lexerLexesToken(SyntaxExample syntaxExample){
		var tokens = SyntaxTree.parseTokens(syntaxExample.text);

		assertThat(tokens)
				.singleElement()
				.extracting(SyntaxToken::kind, SyntaxToken::text)
				.containsExactly(syntaxExample.kind, syntaxExample.text);
	}

	@ParameterizedTest
	@MethodSource("tokenSequences")
	public void lexerLexesTokenSequences(List<SyntaxExample> sequence){
		var tokens = SyntaxTree.parseTokens(sequence.stream().map(SyntaxExample::text).collect(Collectors.joining()));

		assertThat(tokens)
				.hasSize(sequence.size())
				.extracting(SyntaxToken::kind, SyntaxToken::text)
				.containsSequence(sequence.stream().map(ex -> tuple(ex.kind, ex.text)).toList());
	}

}
