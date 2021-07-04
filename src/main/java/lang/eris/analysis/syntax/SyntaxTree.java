package lang.eris.analysis.syntax;

import lang.eris.analysis.DiagnosticBag;

import java.util.ArrayList;
import java.util.List;

public final record SyntaxTree(
		DiagnosticBag diagnostics,
		ExpressionSyntax root,
		SyntaxToken endOfFileToken
){

	public static SyntaxTree parse(String text){
		var parser = new Parser(text);
		return parser.parse();
	}

	public static List<SyntaxToken> parseTokens(String text){
		var lexer = new Lexer(text);
		var tokens = new ArrayList<SyntaxToken>();
		while(true){
			var token = lexer.lex();
			if (token.kind() == SyntaxKind.EndOfFileToken) break;
			tokens.add(token);
		}
		return tokens;
	}
}
