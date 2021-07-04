package lang.eris.analysis.syntax;

import java.util.List;

public final record SyntaxTree(
		List<String> diagnostics,
		ExpressionSyntax root,
		SyntaxToken endOfFileToken
){

	public static SyntaxTree parse(String text){
		var parser = new Parser(text);
		return parser.parse();
	}
}
