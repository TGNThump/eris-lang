package lang.eris.util;

import lang.eris.analysis.syntax.SyntaxNode;
import lang.eris.analysis.syntax.SyntaxToken;

public class TreePrinter {
    private TreePrinter(){}

    public static void prettyPrint(SyntaxNode node){
        prettyPrint(node, "", true);
    }

    public static void prettyPrint(SyntaxNode node, String indent, boolean isLast){
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
