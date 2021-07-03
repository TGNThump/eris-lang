package lang.eris;

import lang.eris.analysis.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ErisApplication{

	public static void main(String[] args) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		boolean showTree = false;
		while (true){
			System.out.print("> ");
			var line = reader.readLine();
			if (line.isBlank()){
				return;
			}

			if (line.equals("#showTree")){
				showTree = !showTree;
				System.out.println(showTree ? "Showing parse trees." : "Not showing parse trees.");
				continue;
			}

			var syntaxTree = SyntaxTree.parse(line);

			if (showTree){
				prettyPrint(syntaxTree.root());
			}

			if (syntaxTree.diagnostics().isEmpty()){
				var e = new Evaluator(syntaxTree.root());
				var result = e.evaluate();
				System.out.println(result);
			} else {
				for (String diagnostic : syntaxTree.diagnostics()){
					System.out.println(diagnostic);
				}
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

		indent += isLast ? "   " : "│   ";

		for (int i = 0; i < node.children().size(); i++){
			prettyPrint(node.children().get(i), indent, i == node.children().size() - 1);
		}
	}
}
