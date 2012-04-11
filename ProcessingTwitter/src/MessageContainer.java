
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageContainer{
	//private List _messages = new ArrayList();
    private MessageQueue _messages = new MessageQueue(20);
	private List _listeners = new ArrayList();
    
    public void add(Message m)
    {
    	_messages.enqueue(m);
    	fireMessageEvent(m);
    }
    
    public Message[] ToMessageArray()
    {
    	return _messages.ToMessageArray();
    }
     
    public Message read()
    {
    	return _messages.dequeue();
    }
    
    public synchronized void addMessageListener(IMessageListener l) {
        _listeners.add(l);
    }
    
    public synchronized void removeMessageListener(IMessageListener l) {
        _listeners.remove(l);
    }
    
    private synchronized void fireMessageEvent(Message m) {
        MessageEvent message = new MessageEvent(this, m);
        Iterator listeners = _listeners.iterator();
        while(listeners.hasNext()) 
        {
            ((IMessageListener)listeners.next()).messageReceived(message);
        }
    }
}
