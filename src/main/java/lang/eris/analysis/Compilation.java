package lang.eris.analysis;

import lang.eris.analysis.binding.Binder;
import lang.eris.analysis.syntax.SyntaxTree;

import java.util.Map;

public record Compilation(
		SyntaxTree syntax
){

	public EvaluationResult evaluate(Map<VariableSymbol, Object> variables){
		var binder = new Binder(variables);
		var boundExpression = binder.bindExpression(syntax.root());

		var diagnostics = syntax.diagnostics().concat(binder.diagnostics());
		if (!diagnostics.isEmpty()){
			return new EvaluationResult(diagnostics, null);
		}

		var evaluator = new Evaluator(boundExpression, variables);
		var value = evaluator.evaluate();
		return new EvaluationResult(new DiagnosticBag(), value);
	}
}
