

import java.util.List;
import java.util.ArrayList;

public class TwitterStream implements IMessageListener, ISettingsListener{
	
	private TwitterConsumer tw;
	private MessageContainer mc;
	private MessageQueue mq;
	Thread T;
	//TwitterSettings tsettings;

    public Message[] getMessages() {return mq.ToMessageArray();}
    public Message readMessage() {return mq.dequeue();}
    
    public TwitterStream()
    {
    	ProcessingTwitter.tsettings = new TwitterSettings();
    	ProcessingTwitter.tsettings.addSettingsListener(this);
		
		
    	mc = new MessageContainer();
		mc.addMessageListener(this);
		mq = new MessageQueue(2500);
		tw = new TwitterConsumer("plhcr", "r645645c", "https://stream.twitter.com/1/statuses/filter.json", mc);
    }
    
    public void showSettings()
    {
    	Thread Tset = new Thread(ProcessingTwitter.tsettings);
		Tset.run();
    }
    
    //Load a new set of flitter settings and apply them to the twitter connection
    public void ReplaceSettings(TwitterSettings s)
    {
    	tw.ReplaceSettings(s);
    }
    
	public void go()
	{
		
		T = new Thread(tw);
		T.start();
	}
	
	//Each time a message is received from twitter
	public void messageReceived(MessageEvent event) {
		Message m = mc.read();		
		
		//Allow filtering of message before exposing them publicly
		if (m.getUserLanguage().equals("en") && !m.getText().contains("\\u"))
		//if (true)
		{
			mq.enqueue(m);
//			System.out.println("User Screen Name: " + m.getUserScreenName());
//			System.out.println("Status: " + m.getText());
//			System.out.println("User Language: " + m.getUserLanguage());
//			System.out.print("Hashtags: ");
//			for (String s: m.getHashTags())
//			{
//				System.out.print(s + " ");
//			}
//			System.out.println();
//			System.out.println("Profile Image: " + m.getUserProfileImageUrl().replace("\\",""));
//			System.out.println("Retweeted: " + m.getRetweeted());
//			System.out.println("---------------------------------------------------------------");
			//print hashtags
			//for(String s: m.getHashTags())
			//{
			//	System.out.println(s);
			//}
		}		
	}
	
	@Override
	public void settingsReceived(SettingsEvent event) {
		//System.out.println("Settings Received");

		tw.stop();
		tw.ReplaceSettings((TwitterSettings)event.getSource());
		T = new Thread(tw);
		T.start();
		
		TwitterSettings t = (TwitterSettings)event.getSource();
		t.dispose();
	}
} 
