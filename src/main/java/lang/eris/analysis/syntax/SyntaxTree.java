package lang.eris.analysis.syntax;

import lang.eris.analysis.DiagnosticBag;

public final record SyntaxTree(
		DiagnosticBag diagnostics,
		ExpressionSyntax root,
		SyntaxToken endOfFileToken
){

	public static SyntaxTree parse(String text){
		var parser = new Parser(text);
		return parser.parse();
	}
}
