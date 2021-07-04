package lang.eris.analysis.binding;

import lang.eris.analysis.syntax.SyntaxKind;

public record BoundBinaryOperator(
		SyntaxKind syntaxKind,
		BoundBinaryOperatorKind kind,
		Class<?> leftType,
		Class<?> rightType,
		Class<?> resultType
){
	public BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, Class<?> operandType, Class<?> resultType){
		this(syntaxKind, kind, operandType, operandType, resultType);
	}

	public BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, Class<?> type){
		this(syntaxKind, kind, type, type, type);
	}

	static BoundBinaryOperator[] operators = {
			new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class),
			new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class),
			new BoundBinaryOperator(SyntaxKind.StarToken, BoundBinaryOperatorKind.Multiplication, Integer.class),
			new BoundBinaryOperator(SyntaxKind.SlashToken, BoundBinaryOperatorKind.Division, Integer.class),

			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Integer.class, Boolean.class),

			new BoundBinaryOperator(SyntaxKind.LessThanToken, BoundBinaryOperatorKind.LessThan, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.LessThanEqualsToken, BoundBinaryOperatorKind.LessThanEquals, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.GreaterThanToken, BoundBinaryOperatorKind.GreaterThan, Integer.class, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.GreaterThanEqualsToken, BoundBinaryOperatorKind.GreaterThanEquals, Integer.class, Boolean.class),

			new BoundBinaryOperator(SyntaxKind.AmpersandAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, Boolean.class),
			new BoundBinaryOperator(SyntaxKind.PipePipeToken, BoundBinaryOperatorKind.LogicalOr, Boolean.class),
	};

	public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Class<?> leftType, Class<?> rightType){
		for (BoundBinaryOperator operator : operators){
			if (operator.syntaxKind == syntaxKind && operator.leftType == leftType && operator.rightType == rightType) return operator;
		}
		return null;
	}
}
