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
	AmpersandAmpersandToken,
	AmpersandToken,
	PipePipeToken,
	PipeToken,
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
	ParenthesizedExpression
}
