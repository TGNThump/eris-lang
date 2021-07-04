package lang.eris.analysis.syntax;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface SyntaxNode {
	SyntaxKind kind();
	default List<SyntaxNode> children(){
		return Collections.emptyList();
	}
}
