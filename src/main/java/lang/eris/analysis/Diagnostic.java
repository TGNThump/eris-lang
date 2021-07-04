package lang.eris.analysis;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;

public record Diagnostic(
	TextSpan span,
	Message message
){
	private static final ParameterizedMessageFactory messageFactory = new ParameterizedMessageFactory();

	public Diagnostic(TextSpan span, String message, Object... args){
		this(span, messageFactory.newMessage(message, args));
	}

	@Override
	public String toString(){
		return message.getFormattedMessage();
	}
}
