package lang.eris.analysis;

import lang.eris.analysis.binding.Binder;
import lang.eris.analysis.syntax.SyntaxTree;

public record Compilation(
		SyntaxTree syntax
){

	public EvaluationResult evaluate(){
		var binder = new Binder();
		var boundExpression = binder.bindExpression(syntax.root());

		var diagnostics = syntax.diagnostics().concat(binder.diagnostics());
		if (!diagnostics.isEmpty()){
			return new EvaluationResult(diagnostics, null);
		}

		var evaluator = new Evaluator(boundExpression);
		var value = evaluator.evaluate();
		return new EvaluationResult(new DiagnosticBag(), value);
	}
}
