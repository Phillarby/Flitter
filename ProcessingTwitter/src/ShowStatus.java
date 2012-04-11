
import java.awt.Color;

import processing.core.*;

public class ShowStatus {
	
	Message _message;
	PApplet _p;
	PVector _pos;
	int _height;
	int _width;
	String _avatarURL;
	PImage _avatar;
	boolean _active;
	boolean _imageLoading;
	boolean _imageLoaded;
	//Thread t;
	imageLoader il;
	
	public Message getMessage(){ return _message; }
	public void setMessage(Message m) 
	{ 
		_message = m; 
		setAvatarURL(m.getUserProfileImageUrl());
	}
	
	public void setPApplet(PApplet p) { _p = p;}
	public PApplet getPApplet() {return _p;}
	
	public void setPosition(PVector p) { _pos = p; }
	public PVector getPosition() { return _pos; }
	
	public void setActive(boolean active) {_active = active;}
	public boolean getActive() {return _active;}
	
	public void setAvatarURL(String avatarURL) 
	{
		if (!avatarURL.equals(_avatarURL))
		{
			_avatarURL = avatarURL;
			_imageLoaded = false;
			_imageLoading = false;
			il = new imageLoader();
		}
	}
	public String getAvatarURL() {return _avatarURL;}
	
	public PImage getAvatar()
	{
		if (!_imageLoaded)
		{
			return null;
		}

		return _avatar;
	}
	
	public void setAvatar(PImage avatar) {_avatar = avatar;}
	
	public ShowStatus(PApplet p)
	{
		_p = p;
		_active = false;
	}
	
	public ShowStatus(PApplet p, PVector pos, Message m)
	{
		_p = p;
		_pos = pos;
		_active = false;
		_message = m;
	}
	
	public void update()
	{
		
	}
	 
	public void draw(PFont font)
	{
		if (_active)
		{				
			_p.textFont(font);
			_p.stroke(255);
			_p.fill(10);
			_p.rect(10, 430, 480, 90);
			if (_imageLoaded && _avatar != null)
				_p.image(_avatar, 20, 450);
			_p.fill(255);
			_p.textSize(30);
			_p.text(getMessage().getUserScreenName(), 90, 430, 370, 40);
			_p.textSize(20);
			_p.textLeading(15);
			_p.text(getMessage().getText(), 90, 460, 370, 60);
		}
	}
	
	private class imageLoader extends Thread
	{
		public imageLoader()
		{
			super();
			start();
		}
		
		@Override
		public void run() {
			try
			{
				//Thread.sleep(500);
				_imageLoading = true;
				_imageLoaded = false;
				
				_avatar = _p.loadImage(_avatarURL.replace("\\", ""), "jpg");
				
				_imageLoading = false;
				_imageLoaded = true;
			}
			catch (Exception ex)
			{
				
			}
		}
	}
}
