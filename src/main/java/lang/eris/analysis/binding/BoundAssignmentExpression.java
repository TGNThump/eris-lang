package lang.eris.analysis.binding;

import lang.eris.analysis.VariableSymbol;

public record BoundAssignmentExpression(
		VariableSymbol variableSymbol,
		BoundExpression boundExpression
) implements BoundExpression{
	@Override
	public Class<?> type(){
		return variableSymbol.type();
	}

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.AssignmentExpression;
	}
}
