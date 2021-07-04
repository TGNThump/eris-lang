package lang.eris.analysis.syntax;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class ParserTests {

    private static Stream<SyntaxKind> binaryOperators(){
        return Arrays.stream(SyntaxKind.values()).filter(kind -> SyntaxFacts.getBinaryOperatorPrecedence(kind) > 0);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> getBinaryOperatorPairs() {
        return binaryOperators().flatMap(a -> binaryOperators().map(b -> Arguments.of(a, b)));
    }


    @ParameterizedTest
    @MethodSource("getBinaryOperatorPairs")
    public void parserBinaryExpressionHonorsPrecedences(SyntaxKind op1, SyntaxKind op2) throws IOException {
        var op1Precedence = SyntaxFacts.getBinaryOperatorPrecedence(op1);
        var op2Precedence = SyntaxFacts.getBinaryOperatorPrecedence(op2);

        var op1Text = SyntaxFacts.getText(op1);
        var op2Text = SyntaxFacts.getText(op2);

        var text = "a " + op1Text + " b " + op2Text + " c";
        var expression = SyntaxTree.parse(text).root();

        if (op1Precedence >= op2Precedence){
            //     op2
            //    /   \
            //   op1   c
            //  /  \
            // a    b

            try(var e = new AssertingEnumerator(expression)){
                e.assertNode(SyntaxKind.BinaryExpression);
                e.assertNode(SyntaxKind.BinaryExpression);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "a");
                e.assertToken(op1, op1Text);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "b");
                e.assertToken(op2, op2Text);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "c");
            }
        } else {
            //     op2
            //    /   \
            //   a     op1
            //        /  \
            //       b    c

            try(var e = new AssertingEnumerator(expression)){
                e.assertNode(SyntaxKind.BinaryExpression);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "a");
                e.assertToken(op1, op1Text);
                e.assertNode(SyntaxKind.BinaryExpression);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "b");
                e.assertToken(op2, op2Text);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "c");
            }
        }
    }
}
