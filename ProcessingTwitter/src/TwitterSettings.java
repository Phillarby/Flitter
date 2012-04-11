
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import java.awt.event.*;

public class TwitterSettings extends JFrame implements Runnable{
	private String _username;
	private String _password;
	private ArrayList<String> _track;
	private ArrayList<String> _follow;
	private JScrollPane  _scroller;
	private Form _form;
	private List _listeners = new ArrayList();
	
	public TwitterSettings()
	{
		_username = "plhcr";
		_password = "r645645c";
		_track = new ArrayList<String>();
		_follow = new ArrayList<String>();
		_form = new Form();
		_scroller = new JScrollPane(_form);
		
		this.setTitle("Flitter Settings");
		this.getContentPane().add(_scroller);
		this.setSize(500, 650);
	}
	
	public String getUsername(){ return _username; }
	public void setUsername(String u) {_username = u; }
	public String getPassword(){ return _password; }
	public void setPassword(String p) {_password = p; }
	public String getTrackCSV() 
	{
		_track = _form._jtTrack.getStrings();
		
		String s = "";
		for (String st: _track)
		{
			s = s + st + ",";
		}
		
		if (s.length() >= 2)
			return s.substring(0, s.length()-1);
		else
			return "";
	}
	
	public synchronized void addSettingsListener(ISettingsListener l) {
        _listeners.add(l);
    }
    
    public synchronized void removeSettingsListener(ISettingsListener l) {
        _listeners.remove(l);
    }
    
    private synchronized void fireSettingsEvent() {
    	SettingsEvent settings = new SettingsEvent(this);
        Iterator listeners = _listeners.iterator();
        while(listeners.hasNext()) 
        {
            ((ISettingsListener)listeners.next()).settingsReceived(settings);
        }
    }
	
	private class RemovableTextList extends JPanel
	{
		JList _list;
		JTextField _addText;
		JButton _add;
		JButton _clear;
		DefaultListModel _model;
		GridBagLayout _formLayout;
		GridBagConstraints _layoutContraints;
		JScrollPane _scroller;
		
		protected RemovableTextList()
		{
			_formLayout = new GridBagLayout();
			_layoutContraints = new GridBagConstraints();
			_layoutContraints.fill = GridBagConstraints.HORIZONTAL;
			_layoutContraints.anchor = GridBagConstraints.WEST;
			
			this.setLayout(_formLayout);
			
			_model = new DefaultListModel();
			_list = new JList(_model);
			_list.setVisibleRowCount(10);
			_scroller = new JScrollPane(_list);
			_scroller.setHorizontalScrollBar(null);
			
			_addText = new JTextField("", 20);
			_add = new JButton("Add");
			_add.addActionListener(new AddItemListener());
			
			_clear = new JButton("Clear List");
			_clear.addActionListener(new ClearListListener());
			
			//Add Components to gridbag
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 0;
			_layoutContraints.gridwidth = 1;
			_formLayout.setConstraints(_addText, _layoutContraints);
			this.add(_addText);
			
			_layoutContraints.gridx = 1;
			_layoutContraints.gridy = 0;
			_layoutContraints.gridwidth = 1;
			_formLayout.setConstraints(_add, _layoutContraints);
			this.add(_add);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 1;
			_layoutContraints.gridwidth = 1;
			_layoutContraints.weighty = 3;
			_formLayout.setConstraints(_scroller, _layoutContraints);
			this.add(_scroller);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 2;
			_layoutContraints.gridwidth = 1;
			_formLayout.setConstraints(_clear, _layoutContraints);
			this.add(_clear);
		}
		
		public void addString(String text)
		{
			_model.addElement(text);
		}

		public ArrayList<String> getStrings()
		{
			ArrayList<String> returnValue = new ArrayList<String>();
			
			for(Object o:_model.toArray())
			{
				returnValue.add(o.toString());
			}
			
			return returnValue;
		}
		
		private class AddItemListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addString(_addText.getText());
				_addText.setText("");
			}
		}
		
		private class ClearListListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_model.clear();
			}
		}
	}
	
	private class Form extends JPanel 
	{
		JLabel _jlTitle;
		GridBagLayout _formLayout;
		GridBagConstraints _layoutContraints;
		JLabel _jlUsername;
		JTextField _jtUsername;
		JLabel _jlPassword;
		JTextField _jtPassword;
		JLabel _jlTrack;
		RemovableTextList _jtTrack;
		JList _jtFollow;
		JButton _jbAddTrack;
		JButton _jbAddFollow;
		JTextArea _jlAccountHeader;
		JTextArea _jlKeywordHeader;
		JLabel _jlAccount;
		JLabel _jlKeywords;
		JButton _jbSave;
		
		String _accountHeader;
		String _keywordsHeader;
		
		protected Form()
		{
			this.setBackground(Color.WHITE);
			
			_accountHeader = "Flitter requires a valid twitter account.  The default account can be changed " +
							 "to any other valid account if you are experiencing connection problems.  This" +
							 "can result if too many reconnections have occured over a short period of time.";
			
			_keywordsHeader = "Twitter posts by keywords. If no keywords are specified " +
			                  "you will receive the raw stream of all posts, which can sometimes be too " + 
			                  "many to be able to visually take in. \n\n " +
			                  "The more regularly posts are made to twitter including the specifed keywords, the " +
	          				  "more activity you will see in flitter.  With this in mind, it is " +
	          				  "best to initially include a varied selecttion of relevant keywords.";
			
			_formLayout = new GridBagLayout();
			_layoutContraints = new GridBagConstraints();
			_layoutContraints.fill = GridBagConstraints.VERTICAL;
			_layoutContraints.anchor = GridBagConstraints.NORTHWEST;
			
			this.setLayout(_formLayout);
			
			_jlTitle = new JLabel("Settings");
			_jlTitle.setFont(new Font("Dialog", Font.PLAIN, 35));
			
			_jlAccount = new JLabel("Account Details");
			_jlAccount.setFont(new Font("Dialog", Font.PLAIN, 24));
			
			_jlKeywords = new JLabel("Keywords");
			_jlKeywords.setFont(new Font("Dialog", Font.PLAIN, 24));
			
			_jlAccountHeader = new JTextArea(_accountHeader);
			_jlAccountHeader.setEditable(false);
			_jlAccountHeader.setSize(450, 100);
			_jlAccountHeader.setWrapStyleWord(true);
			_jlAccountHeader.setLineWrap(true);
			
			_jlKeywordHeader = new JTextArea(_keywordsHeader);
			_jlKeywordHeader.setEditable(false);
			_jlKeywordHeader.setSize(450, 100);
			_jlKeywordHeader.setWrapStyleWord(true);
			_jlKeywordHeader.setLineWrap(true);
			
			_jlUsername = new JLabel("Twitter Username:");
			_jlUsername.setSize(75, 25);
			
			_jtUsername = new JTextField(_username, 20);
			_jtUsername.setSize(150, 150);
			
			_jlPassword = new JLabel("Password:");
			_jtPassword = new JPasswordField(_password, 20);
			
			_jlTrack = new JLabel("Tracking keywords:");
			_jtTrack = new RemovableTextList();
			_jtTrack.setSize(300, 350);
			
			_jbSave = new JButton("Done");
			
			//Add components to layout
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 0;
			_layoutContraints.gridwidth = 2;
			_layoutContraints.anchor = GridBagConstraints.CENTER;
			_formLayout.setConstraints(_jlTitle, _layoutContraints);
			this.add(_jlTitle);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 1;
			_layoutContraints.gridwidth = 2;
			_formLayout.setConstraints(_jlAccount, _layoutContraints);
			this.add(_jlAccount);
			 
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 2;
			_layoutContraints.gridwidth = 2;
			_formLayout.setConstraints(_jlAccountHeader, _layoutContraints);
			this.add(_jlAccountHeader);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 3;
			_layoutContraints.gridwidth = 1;
			_formLayout.setConstraints(_jlUsername, _layoutContraints);
			this.add(_jlUsername);
			
			_layoutContraints.gridx = 1;
			_layoutContraints.gridy = 3;
			_layoutContraints.weightx = 50;
			_formLayout.setConstraints(_jtUsername, _layoutContraints);
			this.add(_jtUsername);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 4;
			_formLayout.setConstraints(_jlPassword, _layoutContraints);
			this.add(_jlPassword);
			
			_layoutContraints.gridx = 1;
			_layoutContraints.gridy = 4;
			_formLayout.setConstraints(_jtPassword, _layoutContraints);
			this.add(_jtPassword);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 5;
			_layoutContraints.gridwidth = 2;
			_formLayout.setConstraints(_jlKeywords, _layoutContraints);
			this.add(_jlKeywords);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 6;
			_layoutContraints.gridwidth = 2;
			_formLayout.setConstraints(_jlKeywordHeader, _layoutContraints);
			this.add(_jlKeywordHeader);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 6;
			_layoutContraints.gridwidth = 1;
			_formLayout.setConstraints(_jlTrack, _layoutContraints);
			this.add(_jlTrack);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 7;
			_layoutContraints.gridwidth = 2;
			_formLayout.setConstraints(_jtTrack, _layoutContraints);
			this.add(_jtTrack);
			
			_layoutContraints.gridx = 0;
			_layoutContraints.gridy = 8;
			_layoutContraints.gridwidth = 2;
			_layoutContraints.anchor = GridBagConstraints.CENTER;
			_formLayout.setConstraints(_jbSave, _layoutContraints);
			this.add(_jbSave, JButton.CENTER);
			
			//Add default keywords
			_jtTrack.addString("News");
			
			_jbSave.addActionListener(new SettingsSavedListener());
		}
		
		private class SettingsSavedListener implements ActionListener
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setUsername(_jtUsername.getText());
				setPassword(_jtPassword.getText());
				fireSettingsEvent();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.setVisible(true);
	}
}
