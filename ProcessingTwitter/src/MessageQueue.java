
import java.util.concurrent.locks.*;

public class MessageQueue {
  
	private final Lock lock = new ReentrantLock();
    private MessageQueueItem[] _messageQueue;
    private int _queueLength;
    private int _messageCount;
    private int _head;
    private int _tail;

	public MessageQueue(int queueLength)
	{
		_messageQueue = new MessageQueueItem[queueLength];
		_queueLength = queueLength;
		_head = -1;
		_tail = -1;
        _messageCount = 0;		

		//Initialise array of message items
		for (int i = 0; i <_messageQueue.length; i++)
		{
			_messageQueue[i] = new MessageQueueItem();
		}
	}

    public Message[] ToMessageArray()
    {
    	Message[] m = null;

    	try
    	{
    		if (lock.tryLock())
    		{
	    		m = new Message[_messageCount];
	    		int ptr = _head;
	          
	    		for(int i = 0; i < _messageCount; i++)
	    		{
	    			m[i] = _messageQueue[ptr]._msg;
	    			ptr = _messageQueue[ptr]._next;
	    		}
    		}
    		else
    		{
    			m = new Message[0];
    		}
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		try
			{
				lock.unlock();
			}
			catch(Exception ex)
			{
				
			}
    	}

      return m;
    }

	public void enqueue(Message m)
	{
		try
		{
			if (lock.tryLock())
			{
				int nextSlot = FindNextFreeSlot();
				_messageQueue[nextSlot]._msg = m;
				_messageQueue[nextSlot]._isEmpty = false;
				if (_tail != -1)
				_messageQueue[_tail]._next = nextSlot;
				_tail = nextSlot;
	            _messageCount++;
				
				if(_head == -1)
					_head = nextSlot;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				lock.unlock();
			}
			catch(Exception ex)
			{
				
			}
		}
	}
	
	private int FindNextFreeSlot()
	{
		int slotToCheck = -1;
		try
		{
			if(lock.tryLock())
			{
				if (_tail < _queueLength - 1)
					slotToCheck = _tail + 1;
				else
					slotToCheck = 0;
				
		
				while(_messageQueue[slotToCheck].getIsEmpty() == false)
				{
					if (slotToCheck == _queueLength)
						slotToCheck = 0;
					
					if (slotToCheck == _head)
					{
						System.out.println("Message queue is full.  Looping until free slot is available.");
					}
		
					if (slotToCheck < _queueLength - 1)
						slotToCheck ++;
					else
						slotToCheck = 0;
				}
			}
		}
		finally
		{
			try
			{
				lock.unlock();
			}
			catch(Exception ex)
			{
				
			}
		}
		return slotToCheck;
	}
	
	//remove the item from the head of the queue and promote the second item 
	public Message dequeue()
	{
		Message returnValue = null;
		
		if (_messageCount > 0)
		{
			try
			{
				if(lock.tryLock())
				{
					returnValue = _messageQueue[_head]._msg; 
					_messageQueue[_head]._isEmpty = true;
					_head = _messageQueue[_head]._next;
			        _messageCount--;
				}
			}
			catch (Exception ex)
			{
				
			}
			finally
			{
				try
				{
					lock.unlock();
				}
				catch(Exception ex)
				{
					
				}
			}
		}
		return returnValue;
	}
	
	//Return the item at the head of the queue without removing it from the queue
	public Message peek()
	{
		return _messageQueue[_head]._msg; 
	}
	//Inner class of linked list items 
	private class MessageQueueItem
	{
		private int _next;
		private Message _msg;
		private boolean _isEmpty;
		
		public boolean getIsEmpty()
		{
			return _isEmpty;
		}
		
		public MessageQueueItem()
		{
			_next = -1;
			_msg = null;
			_isEmpty = true;
		}
	}
}


 