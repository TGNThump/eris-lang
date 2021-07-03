package lang.eris.analysis.syntax;

import java.util.List;

public final record SyntaxTree(
		List<String> diagnostics,
		lang.eris.analysis.syntax.ExpressionSyntax root,
		lang.eris.analysis.syntax.SyntaxToken endOfFileToken
){

	public static SyntaxTree parse(String text){
		var parser = new lang.eris.analysis.syntax.Parser(text);
		return parser.parse();
	}
}
