package lang.eris.analysis.binding;

import lang.eris.analysis.VariableSymbol;

public record BoundVariableExpression(
		VariableSymbol variableSymbol
) implements BoundExpression {

	@Override
	public BoundNodeKind boundNodeKind(){
		return BoundNodeKind.VariableExpression;
	}

	@Override
	public Class<?> type(){
		return variableSymbol.type();
	}
}
