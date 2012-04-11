
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TwitterConsumer implements Runnable {
  
	String _username;
	String _password;
	String _url;
	String _trackingCSV;
	MessageContainer _mc;
    MessageBuilder mb;
    boolean _stop;
	
	public String getUsername() {return _username;}
	public String getPassword() {return _password;}
	public String getURL() {return _url;}
	public String getTrackingCSV() 
	{
		if (_trackingCSV != null & _trackingCSV != "")
			return _trackingCSV;
		else
			return "news";
	}
	public MessageContainer getMessageContainer() {return _mc;}
	
	public void setUsername(String username) {_username = username;}
	public void setPassword(String password) {_password = password;}
	public void setURL(String url) {_url = url;}
	public void setMessageContainer(MessageContainer mc) {_mc = mc;}
	public void setTrackingCSV(String t) {_trackingCSV = t;}
	
	HttpURLConnection connection;
	BufferedReader in;
	InputStream is;
	InputStreamReader ir;
	
	String current;
	
	public TwitterConsumer(String username, String password, String url, MessageContainer mc)
	{
		
		setUsername(username);
		setPassword(password);
		setURL(url);
		setMessageContainer(mc);
        mb = new MessageBuilder();
        _stop = false;
	}
 
	public void stop()
	{
		_stop = true;
	}
	
	public void ReplaceSettings(TwitterSettings s)
	{	
		setUsername(s.getUsername());
		setPassword(s.getPassword());
		setTrackingCSV(s.getTrackCSV());
	}
	
	public void makeConnection()
	{
		try
		{
			URL url = new URL(getURL());
			String userPassword = getUsername() + ":" + getPassword();
			String encoding = Base64.encodeBytes(userPassword.getBytes());		
		    
			connection = (HttpURLConnection)url.openConnection();;
		    connection.setRequestMethod("POST");
		    connection.setDoOutput(true);
		    connection.setDoInput(true);
		    
		    connection.setRequestProperty("Authorization", "Basic " + encoding);
		    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write("track=" + getTrackingCSV());
	        wr.flush();
		    
		    is = connection.getInputStream();
		    ir = new InputStreamReader(is);
		    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    
		    while ((current = in.readLine()) != null && !_stop) {
		    	getMessageContainer().add(mb.parseMesage(current));
		    }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		} 
		finally
		{
			System.out.println("Connection Updated");
		}
	}
	
	public void run()
	{
		_stop = false;
		makeConnection();
	}
}