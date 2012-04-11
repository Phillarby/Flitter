import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PShapeSVG.Font;
import processing.core.PVector;

public class SettingsButton {
	PApplet _p;
	String _text;
	PVector _pos;
	float _width;
	float _height;
	PFont _font;
	boolean _show;
	int _offset;
	
	public SettingsButton(PApplet p, PFont f)
	{
		_p = p;
		_font = f;
		
		_text = "Settings";
		_pos = new PVector(400, 412);
		_width = 70f;
		_height = 15f;
	}
	
	private void update()
	{
		//_width = 1f;
		_pos.x = _p.mouseX; 
		if (_p.mouseY >= _pos.y && _p.mouseY <= _pos.y + _height)
			_show = true;
		else
			_show = false;
		
		if (_show && _offset < _width)
			_offset = _offset + 5;
		else if (!_show)
			_offset = 0;
	}
	
	public void draw()
	{
		update();
		_p.pushMatrix();
		_p.translate(_pos.x - _offset / 2, _pos.y);
		_p.fill(0);
		_p.strokeWeight(2);
		_p.line(0, 0, 0, _height);
		
		if (_show)
		{
			_p.fill(10);
			_p.rect(0, 0, _offset, _height);
			_p.textFont(_font);
			_p.textSize(20);
			_p.fill(150);
			_p.translate(12, 13);
			if (_offset >= _width)
				_p.text(_text, 0, 0);
		}
		
		_p.popMatrix();
	}
	
	public boolean clicked (int x, int y)
	{
		if (_show)
		{
			return true;
		}
		
		return false;
	}
}
