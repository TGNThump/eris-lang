package lang.eris.analysis.binding;

public record BoundAssignmentExpression(
		String name,
		BoundExpression boundExpression
) implements BoundExpression{
	@Override
	public Class<?> type(){
		return boundExpression.type();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.AssignmentExpression;
	}
}
