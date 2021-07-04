package lang.eris.analysis.binding;

public record BoundVariableExpression(
		String name,
		Class<?> type
) implements BoundExpression {

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.VariableExpression;
	}
}
