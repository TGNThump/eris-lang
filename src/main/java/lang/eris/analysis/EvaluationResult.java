package lang.eris.analysis;

public record EvaluationResult(
		DiagnosticBag diagnostics,
		Object value
){}
