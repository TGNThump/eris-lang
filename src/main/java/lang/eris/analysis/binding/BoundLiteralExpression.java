package lang.eris.analysis.binding;

public record BoundLiteralExpression(
		Object value
) implements BoundExpression{
	@Override
	public Class<?> type(){
		return value.getClass();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.LiteralExpression;
	}
}
