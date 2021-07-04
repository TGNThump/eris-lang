package lang.eris;

import lang.eris.analysis.Compilation;
import lang.eris.analysis.Diagnostic;
import lang.eris.analysis.syntax.SyntaxNode;
import lang.eris.analysis.syntax.SyntaxToken;
import lang.eris.analysis.syntax.SyntaxTree;
import lang.eris.util.ConsoleColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ErisApplication{

	public static void main(String[] args) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		boolean showTree = false;
		var variables = new HashMap<String, Object>();

		while (true){
			System.out.print("> ");
			var line = reader.readLine();
			if (line.isBlank()){
				return;
			}

			if (line.equals("#showTrees")){
				showTree = !showTree;
				System.out.println(showTree ? "Showing parse trees." : "Not showing parse trees.");
				continue;
			}

			var syntaxTree = SyntaxTree.parse(line);

			if (showTree){
				prettyPrint(syntaxTree.root());
			}

			var compilation = new Compilation(syntaxTree);
			var result = compilation.evaluate(variables);

			if (result.diagnostics().isEmpty()){
				System.out.println(result.value());
			} else {
				for (Diagnostic diagnostic : result.diagnostics()){
					var prefix = line.substring(0, diagnostic.span().start());
					var error = line.substring(diagnostic.span().start(), diagnostic.span().end());
					var suffix = line.substring(diagnostic.span().end());

					System.out.println();
					System.out.println("" + ConsoleColor.RED + diagnostic + ConsoleColor.RESET);
					System.out.println("    " + prefix + ConsoleColor.RED + error + ConsoleColor.RESET + suffix);
				}
				System.out.println();
			}
		}
	}

	private static void prettyPrint(SyntaxNode node){
		prettyPrint(node, "", true);
	}

	private static void prettyPrint(SyntaxNode node, String indent, boolean isLast){
		var marker = isLast ? "└──" : "├──";
		System.out.print(indent);
		System.out.print(marker);
		System.out.print(node.kind());

		if (node instanceof SyntaxToken t && t.value() != null){
			System.out.print(" " + t.value());
		}

		System.out.println();

		indent += isLast ? "   " : "│  ";

		for (int i = 0; i < node.children().size(); i++){
			prettyPrint(node.children().get(i), indent, i == node.children().size() - 1);
		}
	}
}
