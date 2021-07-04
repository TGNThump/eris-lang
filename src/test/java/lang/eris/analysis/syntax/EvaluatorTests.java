package lang.eris.analysis.syntax;

import lang.eris.analysis.Compilation;
import lang.eris.analysis.VariableSymbol;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class EvaluatorTests {

    @SuppressWarnings("unused")
    private static Stream<Arguments> testExpressions() {
        return Stream.of(
                of("1", 1),
                of("+1", 1),
                of("-1", -1),
                of("1 + 2", 3),
                of("1 - 2", -1),
                of("2 * 3", 6),
                of("6 / 2", 3),
                of("(5)", 5),
                of("1 + 2 * 3", 7),
                of("(1 + 2) * 3", 9),
                of("true", true),
                of("false", false),
                of("!true", false),
                of("!false", true),
                of("true || true", true),
                of("true || false", true),
                of("false || true", true),
                of("false || false", false),
                of("true && true", true),
                of("true && false", false),
                of("false && true", false),
                of ("false && false", false),
                of ("true == true", true),
                of ("true == false", false),
                of ("true != true", false),
                of ("true != false", true),
                of ("false != true", true),
                of ("1 == 1", true),
                of ("1 == 3", false),
                of("(a = 10) * a", 100)
        );
    }

    @ParameterizedTest
    @MethodSource("testExpressions")
    public void testExpression(String text, Object expectedValue){
        var syntaxTree = SyntaxTree.parse(text);
        var compilation = new Compilation(syntaxTree);
        var variables = new HashMap<VariableSymbol, Object>();
        var actualResult = compilation.evaluate(variables);

        assertThat(actualResult.diagnostics()).isEmpty();
        assertThat(actualResult.value()).isEqualTo(expectedValue);
    }
}
