package lang.eris.analysis.syntax;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface SyntaxNode extends Iterable<SyntaxNode>{
	SyntaxKind kind();
	default List<SyntaxNode> children(){
		return Collections.emptyList();
	}

	@Override
	default Iterator<SyntaxNode> iterator(){
		return children().iterator();
	}
}
