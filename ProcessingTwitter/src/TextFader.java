
import java.awt.*;
import java.util.Random;

import processing.core.*;

public class TextFader{

	PVector _pos;
	float _size;
	Color _colour;
	String _text;
	int _life;
	int _lifeLimit;
	PFont _font;
	boolean _isAlive; 
	float _alpha;
	boolean _stopped;
	float _dotFade;
	PApplet _p;
	boolean _isMouseNear;
	boolean _isAttached;
	PVector _dotCentre;
	Message _message;
	ShowStatus _s;
	boolean _isMaxSize;
	boolean _dying;
	
	public float getXPos() {return _pos.x;}
	public void setXPos(float x) {_pos.x = x;}
	
	public float getYPos() {return _pos.y;}
	public void setYPos(float y) {_pos.y = y;}
	
	public float getTextSize() {return _size;}
	public void setTextSize(float size) {_size = size;}
	
	public Color getColour() {return _colour;}
	public void setColour(Color colour) {_colour = colour;}
	
	public String getText() {return _text;}
	public void setText(String text) {_text = text;}
	
	public int getLife() {return _life;}
	public void setLife(int life) {_life = life;}
	
	public int getLifeLimit() {return _lifeLimit;}
	public void setLifeLimit(int lifeLimit) {_lifeLimit = lifeLimit;}
	
	public boolean getIsAlive() {return _isAlive;}
	public void setIsAlive(boolean isAlive) {_isAlive = isAlive;}
	
	public boolean getIsMouseNear() {return _isMouseNear;}
	public void setIsMouseNear(boolean isMouseNear) {_isMouseNear = isMouseNear;}
	
	public boolean getIsAttached() {return _isAttached;}
	public void setIsAttached(boolean isAttached) {_isAttached = isAttached;}
	
	public boolean getIsMaxSize() {return _isMaxSize;}
	public void setIsMaxSize(boolean isMaxSize) {_isMaxSize = isMaxSize;}
	
	public float getDotCentreXPos() {return _dotCentre.x;}
	public void setDotCentreXPos(float x) {_dotCentre.x = x;}
	
	public float getDotCentreYPos() {return _dotCentre.y;}
	public void setDotCentreYPos(float y) {_dotCentre.y = y;}
	
	public Message getMessage() {return _message;}
	public void setMessage(Message m) {_message = m;}
	
	public boolean getDying() {return _dying;}
	public void setDying(boolean d) {_dying = d;}
	
	public PVector getScreenPosition()
	{
		return new PVector(_p.screenX(getXPos(), getYPos()), _p.screenY(getXPos(), getYPos()));
	}
	
	public PVector getDotCentreScreenPosition()
	{
		return new PVector(_p.screenX(getDotCentreXPos(), getDotCentreYPos()), _p.screenY(getDotCentreXPos(), getDotCentreYPos()));
	}
	
	public TextFader(PFont font, String text, Color colour, PApplet p, Message m)
	{
		_p = p;
		Random r = new Random();
		
		_font = font;
		_text = text;
		_colour = colour;
		_life = 0;
		_size = 8f;
		_pos = new PVector(r.nextInt(_p.width - 50), r.nextInt(_p.height - 150),0);
		_alpha = 0;
		_stopped = false;
		_dotFade = 0;
		_isAttached = false;
		_isAlive = true;
		_dotCentre = new PVector();
		_message = m;
		_s = new ShowStatus(_p,_pos, _message);
		_s.setActive(false);
		_isMaxSize = false;
		_dying = false;
		 
		_dotCentre.x = getXPos() + _p.textWidth(getText())/2;
		_dotCentre.y = getYPos() + 25;
	}
	
	//Update the TextFader ahead of the next draw cycle
	public void update()
	{
		_life++;
		_isMaxSize = false;
		
		//If the fader is dying then shrink and fade
		if(_dying)
		{	
			if (_size >= 0) 
				_size = _size - 0.005f;
			
			if ((_dotFade = (_alpha = _alpha - 2)) <= 0)
			{
				_isAlive = false;
			}
		}
		else
		{
			//When text is first entering, shrink it until it reaches it's target size
			if (_size > 0.3 && !_stopped)
				_size = _size - 0.05f;
			else if (!_stopped)
				_stopped = true;
			
			//Once text has landed, fade in the red dot by adjusting alpha
			if (_stopped && _dotFade < 255)
				_dotFade = _dotFade + 20;
			
			//Fade characters in to max alpha
			if (_alpha < 100)
				_alpha = _alpha + 0.5f;
			
			//Set any size change properties
			if(_stopped && _isMouseNear && _size < 1)
			{
				//If mouse is near then scale up
				_size = _size + 0.05f;
			}
			//else
			if(_stopped && _isAttached && _size < 0.6)
			{
				//If attached to a live status then scale up
				_size = _size + 0.02f;
			}
			//else
			if(_stopped && _size >= 1)
			{
				//If attached to a live status then scale up
				_isMaxSize = true;
			}
			//else
			if (_stopped && !_isMouseNear && !_isAttached && _size > 0.3)
			{
				//If nether near or attached then scale down
				_size = _size - 0.05f;
			}
		}
	}
	
	//Draw the TextFader output
	public void draw(PApplet p)
	{
		_p.pushMatrix();
		
		if (_life <= 155)
			_p.translate(getDotCentreXPos() + 620 - 4 *_life - (_p.textWidth(getText())/2) * _size, getDotCentreYPos() - 20 * _size);
		else
			_p.translate(getDotCentreXPos() - (_p.textWidth(getText())/2) * _size, getDotCentreYPos() - 20 * _size);
		
		_p.textFont(_font);
		_p.scale(_size);
		_p.fill(_colour.getRed(), _colour.getGreen(), _colour.getBlue(), _alpha);
		_p.text(getText(), 0, 0);
		_p.popMatrix();
		
		if (_stopped) drawDot();
	}
	
	private void drawDot()
	{
		_p.strokeWeight(5);
		_p.stroke(255,255,255,100);
		_p.pushMatrix();
		_p.translate(getDotCentreXPos(), getDotCentreYPos());
		_p.scale(_size);
		_p.fill(100, 0, 0, _dotFade);
		_p.ellipse(0, 0, 20, 20);
		_p.popMatrix();
		_p.strokeWeight(1);
	}
}
