
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.awt.*;

import processing.core.*;

public class ProcessingTwitter extends PApplet
{
	public static TwitterSettings tsettings;
	
	TwitterStream ts; //Twitter Stream Service
	PFont font; //Display Font
	Message m; //Instance of a twitter message
	ArrayList<TextFader> f = new ArrayList<TextFader>(); //List of current displayed hashtags
	ArrayList<TextFader> _dying = new ArrayList<TextFader>(); //List of hashtags flagged for disposal
	Random r = new Random(); //Random number generator
	TextFader _t; //reference to the currently selected hashtag
	ShowStatus _s; //Reference to the tweet display object
	boolean _showStatus; //Flag indicating whether to display the full tweet
	boolean _selectedOne; //Flag indicating if a hashtag has been selected
	SettingsButton sb; //Reference to a settings button
	
	public static void main(String args[]) {
		//initialise processing PApplet
	    PApplet.main(new String[] { "--present", "ProcessingTwitter" });
	}
	
	public void setup()
	{
		//Set up display
		size(500, 530, JAVA2D);
		frameRate(25);
		smooth();
		
		//Instantiate a new twitter stream and launch it with default settings
		ts = new TwitterStream();
		ts.go();
		
		//Load display font
		font = loadFont("BrowalliaNew-48.vlw"); 
		
		//Instantiate a new settings button
		sb = new SettingsButton(this, font);
		
		//Instantiate a new tweet displayer
		_s = new ShowStatus(this);

	}
	
	//Iterate through list of hashtags marked for removal and kill them
//	public void deleteDeadFaders()
//	{
//		for(int i = 0; i < f.size(); i++)
//		{
//			if(!f.get(i)._isAlive)
//				f.remove(i);
//		}
//	}
	
	public void draw()
	{
		//Clear Background
		background(0);

		//Draw horizontal line separating tweets from status display
		stroke(255,255,255,100);
		strokeWeight(2);
		line(0,420,500,420);
		
		//Reset local variable indicating if a textfader is currently selected
		//and whether a status shoudl be displayed
		_selectedOne = false;
		_showStatus = false;
		
		//System.out.println(ts.getMessages().length);
		//c = 0;
		
		//Read any awaiting messages from the queue
		while ((m = ts.readMessage()) != null) // && c<2
		{
			for (String ht:m.getHashTags())
			{
				//System.out.println(ht);
				
				//Create a new TextFader for each hashtag
				f.add(new TextFader(font, ht, Color.WHITE, this, m));
				
				//If array size limit is passed then delete oldest members
				if (f.size() > 200)
				{
					f.get(0).setDying(true);
					_dying.add(f.get(0));
					f.remove(0);
				}
			}
			
			//counter to limit posts per cycle to maintain performance during spikes
			//c++;
		}
		
		//When no internet connection is available, this can replace the above block to use randomly
		//generated numbers in place of tweets
//		if(r.nextInt(4)%4 >= 1)
//		{
//			f.add(new TextFader(font, String.valueOf(r.nextInt(20)) , Color.WHITE, this, new Message()));
//			if (f.size() > 250)
//			{
//				f.get(0).setDying(true);
//				_dying.add(f.get(0));
//				f.remove(0);
//			}
//		}
		
		//Get current mouse position
		PVector _mousePos = new PVector(mouseX, mouseY);
		
		//Enumerate through each textFader
		for(int i = 0; i < _dying.size(); i++)
		{
			if (((TextFader)(_dying.get(i)))._isAlive)
			{
				((TextFader)(_dying.get(i))).update();
				((TextFader)(_dying.get(i))).draw(this);
			}
			else
			{
				_dying.remove(i);
				i--;
			}
		}
	
		//Enumerate through each textFader
		for(TextFader t : f)
		{		
			if (PVector.dist(_mousePos, t.getDotCentreScreenPosition()) < 10 && !_selectedOne && t._stopped)
			{	
				//If the mouse is within the selection threshold of the textfade then select it
				t.setIsMouseNear(true);
				_selectedOne = true;
				_t = t;
			}
			else if (t.getIsMouseNear())
			{
				//Reset status of textfader
				t.setIsMouseNear(false);
			}
			
			if (_t != null && _t.getText().equals(t.getText()) && t._stopped && t != _t && _t.getIsMouseNear())
			{
				//Draw a line to the selected textfader if it shares a text value
				stroke(255,255,255,70);
				strokeWeight(10);
				line(	
					_t.getDotCentreScreenPosition().x,
					_t.getDotCentreScreenPosition().y,
					t.getDotCentreScreenPosition().x,
					t.getDotCentreScreenPosition().y
					);
				
				//Draw line on bottom bar
				strokeWeight(2);
				stroke(100);
				line(
					t.getDotCentreScreenPosition().x,
					415,
					t.getDotCentreScreenPosition().x,
					425
					);
				
				strokeWeight(1);
				//Set attached property of textFader
				t.setIsAttached(true);
			}
			else if (_t != null)
			{
				//reset attached flag if the text values are different
				t.setIsAttached(false);
			}
			
			//if selected item has grown to full extent then set status to draw
			if (t.getIsMouseNear() && t._stopped && !_showStatus	)
			{
				_showStatus = true;
				_s.setActive(true);
				//_s.setMessage(t.getMessage());
				//_s.setAvatarURL(t.getMessage()._userProfileImageURL);
			}
			
			t.update();
			t.draw(this);
		}
		
		if (_t != null)
		{
			if (!_t.getMessage().equals(_s.getMessage()))
			{
				_s.setMessage(_t.getMessage());
			}
			
			_t.setColour(Color.WHITE);
			_t.draw(this);
		}
		
		if (_showStatus)
		{
			_s.draw(font);
		}	
		
		sb.draw();
	}
	
	public void mouseClicked()
	{
		if(sb.clicked(mouseX, mouseY))
		{
			ts.showSettings();
		}
	}
}