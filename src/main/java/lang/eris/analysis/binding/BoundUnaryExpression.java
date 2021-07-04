package lang.eris.analysis.binding;

public record BoundUnaryExpression(
		BoundUnaryOperator operator,
		BoundExpression operand
) implements BoundExpression{

	@Override
	public Class<?> type(){
		return operand.type();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.UnaryExpression;
	}
}
