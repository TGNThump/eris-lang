package lang.eris;

import lang.eris.analysis.Compilation;
import lang.eris.analysis.Diagnostic;
import lang.eris.analysis.VariableSymbol;
import lang.eris.analysis.syntax.SyntaxTree;
import lang.eris.util.ConsoleColor;
import lang.eris.util.TreePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ErisApplication{

	public static void main(String[] args) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		boolean showTree = false;
		var variables = new HashMap<VariableSymbol, Object>();

		while (true){
			System.out.print("> ");
			var line = reader.readLine();
			if (line.isBlank()){
				continue;
			}

			if (line.equals("#showTrees")){
				showTree = !showTree;
				System.out.println(showTree ? "Showing parse trees." : "Not showing parse trees.");
				continue;
			}

			var syntaxTree = SyntaxTree.parse(line);

			if (showTree){
				TreePrinter.prettyPrint(syntaxTree.root());
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
}
