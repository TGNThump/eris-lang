package lang.eris.analysis.binding;

public record BoundBinaryExpression(
		BoundExpression left,
		BoundBinaryOperatorKind operatorKind,
		BoundExpression right
) implements BoundExpression{
	@Override
	public Class<?> type(){
		return left.type();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.BinaryExpression;
	}
}
