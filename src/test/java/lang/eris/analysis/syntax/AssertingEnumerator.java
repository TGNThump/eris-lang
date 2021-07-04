package lang.eris.analysis.syntax;

import org.assertj.core.api.InstanceOfAssertFactories;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class AssertingEnumerator implements Closeable {
    private final Iterator<SyntaxNode> iterator;

    public AssertingEnumerator(SyntaxNode node){
        iterator = flatten(node);
    }

    private static Iterator<SyntaxNode> flatten(SyntaxNode node){
        List<SyntaxNode> flat = new ArrayList<>();
        var stack = new LinkedList<SyntaxNode>();
        stack.push(node);

        while (stack.size() > 0){
            var n = stack.pop();
            flat.add(n);

            for (int i = n.children().size() - 1; i >= 0; i--) {
                stack.push(n.children().get(i));
            }
        }
        return flat.iterator();
    }

    public void assertToken(SyntaxKind kind, String text){
        assertThat(iterator.hasNext()).isTrue();
        SyntaxNode next = iterator.next();
        System.out.println(next.kind());

        assertThat(next)
                .asInstanceOf(type(SyntaxToken.class))
                .extracting(SyntaxToken::kind, SyntaxToken::text)
                .containsExactly(kind, text);
    }

    public void assertNode(SyntaxKind kind){
        assertThat(iterator.hasNext()).isTrue();
        SyntaxNode next = iterator.next();
        System.out.println(next.kind());

        assertThat(next)
                .isNotInstanceOf(SyntaxToken.class)
                .extracting(SyntaxNode::kind)
                .isEqualTo(kind);
    }

    @Override
    public void close() throws IOException {
        assertThat(iterator.hasNext()).isFalse();
    }
}
