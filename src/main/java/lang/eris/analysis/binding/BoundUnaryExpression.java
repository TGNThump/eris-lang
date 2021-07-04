package lang.eris.analysis.binding;

public record BoundUnaryExpression(
		BoundUnaryOperator operator,
		BoundExpression operand
) implements BoundExpression{

	@Override
	public Class<?> type(){
		return operator.resultType();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.UnaryExpression;
	}
}
