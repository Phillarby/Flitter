import processing.core.*;

public class ImageProvider implements Runnable {

	private PImage _img;
	private String _url;
	private boolean _isLoaded;
	private boolean _isLoading;
	
	public ImageProvider(PApplet p)
	{
		
	}
	
	public void setURL(String url)
	{
		_url = url;
		_isLoading = false;
		_isLoaded = false;
	}
	
	@Override
	public void run() {
		while (true)
		{
			
		}
		
	}

}
