package lang.eris.analysis;

import lang.eris.analysis.syntax.SyntaxKind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DiagnosticBag implements Iterable<Diagnostic>{
	private final List<Diagnostic> diagnostics;

	private DiagnosticBag(List<Diagnostic> diagnostics){
		this.diagnostics = diagnostics;
	}

	public DiagnosticBag(){
		this(new ArrayList<>());
	}

	@Override
	public Iterator<Diagnostic> iterator(){
		return diagnostics.iterator();
	}

	public Stream<Diagnostic> stream(){
		return StreamSupport.stream(spliterator(), false);
	}

	public void addAll(DiagnosticBag diagnostics){
		this.diagnostics.addAll(diagnostics.diagnostics);
	}

	public DiagnosticBag concat(DiagnosticBag diagnostics){
		return new DiagnosticBag(Stream.concat(stream(), diagnostics.stream()).collect(Collectors.toList()));
	}

	public boolean isEmpty(){
		return diagnostics.isEmpty();
	}

	private void report(TextSpan span, String message, Object... args){
		diagnostics.add(new Diagnostic(span, message, args));
	}

	public void reportInvalidNumber(TextSpan textSpan, String text, Class<?> type){
		report(textSpan, "The number {} isn't a valid {}.", text, type.getSimpleName());
	}

	public void reportBadCharacter(int position, char current){
		report(new TextSpan(position, 1), "Bad character input {}.", current);
	}

	public void reportUnexpectedToken(TextSpan span, SyntaxKind actualKind, SyntaxKind expectedKind){
		report(span, "Unexpected token {}, expected {}.", actualKind, expectedKind);
	}

	public void reportUndefinedUnaryOperator(TextSpan span, String operatorText, Class<?> operandType){
		report(span, "Unary operator {} is not defined for type {}.", operatorText, operandType.getSimpleName());
	}

	public void reportUndefinedBinaryOperator(TextSpan span, String operatorText, Class<?> leftType, Class<?> rightType){
		report(span, "Binary operator {} is not defined for types {} and {}.", operatorText, leftType.getSimpleName(), rightType.getSimpleName());
	}

	public void reportUndefinedName(TextSpan span, String name){
		report(span, "Variable '{}' doesn't exist.", name);
	}
}
