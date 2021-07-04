package lang.eris.analysis.binding;

import lang.eris.analysis.syntax.SyntaxKind;

public record BoundUnaryOperator(
		SyntaxKind syntaxKind,
		BoundUnaryOperatorKind kind,
		Class<?> operandType,
		Class<?> resultType
){
	public BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind kind, Class<?> operandType){
		this(syntaxKind, kind, operandType, operandType);
	}

	static BoundUnaryOperator[] operators = {
			new BoundUnaryOperator(SyntaxKind.BangToken, BoundUnaryOperatorKind.LogicalNegation, Boolean.class),
			new BoundUnaryOperator(SyntaxKind.PlusToken, BoundUnaryOperatorKind.Identity, Integer.class),
			new BoundUnaryOperator(SyntaxKind.MinusToken, BoundUnaryOperatorKind.Negation, Integer.class),
	};

	public static BoundUnaryOperator bind(SyntaxKind syntaxKind, Class<?> operandType){
		for (BoundUnaryOperator operator : operators){
			if (operator.syntaxKind == syntaxKind && operator.operandType == operandType) return operator;
		}
		return null;
	}
}
