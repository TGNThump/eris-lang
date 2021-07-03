package lang.eris.analysis;

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

	// Expressions
	LiteralExpression,
	BinaryExpression,
	UnaryExpression,
	ParenthesizedExpression
}
