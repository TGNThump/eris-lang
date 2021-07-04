package lang.eris.analysis.syntax;

import lang.eris.util.TreePrinter;
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

    private static Stream<SyntaxKind> unaryOperators(){
        return Arrays.stream(SyntaxKind.values()).filter(kind -> SyntaxFacts.getUnaryOperatorPrecedence(kind) > 0);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> getBinaryOperatorPairs() {
        return binaryOperators().flatMap(a -> binaryOperators().map(b -> Arguments.of(a, b)));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> getUnaryBinaryOperatorPairs() {
        return unaryOperators().flatMap(a -> binaryOperators().map(b -> Arguments.of(a, b)));
    }

    @ParameterizedTest
    @MethodSource("getUnaryBinaryOperatorPairs")
    public void parserUnaryExpressionHonorsPrecedences(SyntaxKind unaryKind, SyntaxKind binaryKind) throws IOException {
        var unaryPrecedence = SyntaxFacts.getUnaryOperatorPrecedence(unaryKind);
        var binaryPrecedence = SyntaxFacts.getBinaryOperatorPrecedence(binaryKind);
        var unaryText = SyntaxFacts.getText(unaryKind);
        var binaryText = SyntaxFacts.getText(binaryKind);

        var text = unaryText + " a " + binaryText + " b";
        var expression = SyntaxTree.parse(text).root();
        TreePrinter.prettyPrint(expression);

        if (unaryPrecedence >= binaryPrecedence){
            //  binary
            //  /    \
            // unary  b
            //  |
            //  a


            try(var e = new AssertingEnumerator(expression)){
                e.assertNode(SyntaxKind.BinaryExpression);
                e.assertNode(SyntaxKind.UnaryExpression);
                e.assertToken(unaryKind, unaryText);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "a");
                e.assertToken(binaryKind, binaryText);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "b");
            }
        } else {
            //  unary
            //    |
            //  binary
            //  /   \
            // a     b

            try(var e = new AssertingEnumerator(expression)){
                e.assertNode(SyntaxKind.UnaryExpression);
                e.assertToken(unaryKind, unaryText);
                e.assertNode(SyntaxKind.BinaryExpression);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "a");
                e.assertToken(binaryKind, binaryText);
                e.assertNode(SyntaxKind.NameExpression);
                e.assertToken(SyntaxKind.IdentifierToken, "b");
            }
        }
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
