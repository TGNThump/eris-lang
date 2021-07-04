package lang.eris.analysis.binding;

public record BoundBinaryExpression(
		BoundExpression left,
		BoundBinaryOperator operator,
		BoundExpression right
) implements BoundExpression{
	@Override
	public Class<?> type(){
		return operator.resultType();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.BinaryExpression;
	}
}
