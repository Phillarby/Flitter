
import java.util.EventObject;

public class MessageEvent extends EventObject {
	private Message _message;
	
	public MessageEvent(Object source, Message message)
	{
		super(source);
		_message = message;
	}
	
	public Message getMessage()
	{
		return _message;
	}
} 
