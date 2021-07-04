package lang.eris.analysis.syntax;

public enum SyntaxKind{
	// Tokens
	BadToken,
	EndOfFileToken,
	WhitespaceToken,
	LiteralToken,
	PlusToken,
	MinusToken,
	StarToken,
	SlashToken,
	BangToken,
	BangEqualsToken,
	AmpersandAmpersandToken,
	AmpersandToken,
	PipePipeToken,
	PipeToken,
	EqualsEqualsToken,
	EqualsToken,
	LessThanEqualsToken,
	LessThanToken,
	GreaterThanEqualsToken,
	GreaterThanToken,
	OpenParenthesisToken,
	CloseParenthesisToken,
	IdentifierToken,

	// Keywords
	FalseKeyword,
	TrueKeyword,

	// Expressions
	LiteralExpression,
	BinaryExpression,
	UnaryExpression,
	ParenthesizedExpression,
	NameExpression,
	AssignmentExpression
}
