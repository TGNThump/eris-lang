package lang.eris.analysis.syntax;

import lang.eris.analysis.TextSpan;

import java.util.Collections;
import java.util.List;

public interface SyntaxNode {

	default TextSpan span(){
		var children = children();
		var first = children.get(0).span();
		var last = children.get(children.size()-1).span();
		return TextSpan.fromBounds(first.start(), last.end());
	}
	SyntaxKind kind();
	default List<SyntaxNode> children(){
		return Collections.emptyList();
	}
}
