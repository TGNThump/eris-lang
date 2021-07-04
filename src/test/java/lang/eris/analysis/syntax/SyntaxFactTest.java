package lang.eris.analysis.syntax;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SyntaxFactTest {

    @ParameterizedTest
    @EnumSource(SyntaxKind.class)
    public void testGetText(SyntaxKind kind){
        var text = SyntaxFacts.getText(kind);
        if (text == null) return;

        var token = SyntaxTree.parseTokens(text);
        assertThat(token).singleElement().extracting(SyntaxToken::text).isEqualTo(text);
    }
}
