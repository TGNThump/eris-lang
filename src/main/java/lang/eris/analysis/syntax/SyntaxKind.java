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
