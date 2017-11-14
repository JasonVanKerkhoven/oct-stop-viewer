/**
*Class:             StopTimeUI.java
*Project:          	OC Transpo Stop Times
*Author:            Jason Van Kerkhoven                                             
*Date of Update:    02/06/2017                                              
*Version:           1.0.0    
*                                                                                   
*Purpose:           N/A
*					
* 
*Update Log			v1.0.0
*						-
*/
package ui;


//import external libraries
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class StopTimeUI extends JFrame implements ActionListener
{
	//declaring class constants
	public static final String MENU_CLOSE = "m/file/close";
	public static final String MENU_ADD = "m/file/add";
	public static final String MENU_THEME = "m/options/theme";
	public static final String MENU_REMOVE = "m/options/remove";
	private static final int DEFAULT_WINDOW_X = 600;
	private static final int DEFAULT_WINDOW_Y = 600;
	
	//declaring local instance variables
	JTabbedPane tabplane;
	private ArrayList<Tab> tabs;
	ActionListener listener;
	

	//generic constructor
	public StopTimeUI(String title, ActionListener listener, WindowAdapter closeOverride) 
	{
		//set up main window frame
		super(title);
		this.setBounds(0, 0, DEFAULT_WINDOW_X, DEFAULT_WINDOW_Y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//init non-GUI elements
		this.listener = listener;
		tabs = new ArrayList<Tab>();
		
		//set up menu bar
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		//add categories to menu bar
		JMenu mnFile = new JMenu("File");
		JMenu mnEdit = new JMenu("Edit");
		JMenu mnOptions = new JMenu("Options");
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnFile);
		menuBar.add(mnEdit);
		menuBar.add(mnOptions);
		menuBar.add(mnHelp);
		
		//add to "File" category
		JMenuItem mntmAdd = new JMenuItem("Add Stop");
		mntmAdd.setActionCommand(MENU_ADD);
		mntmAdd.addActionListener(listener);
		mnFile.add(mntmAdd);
		
		JMenuItem mntmRemove = new JMenuItem("Remove Stop");
		mntmRemove.setActionCommand(MENU_REMOVE);
		mntmRemove.addActionListener(listener);
		mnFile.add(mntmRemove);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.setActionCommand(MENU_CLOSE);
		mntmClose.addActionListener(listener);
		mnFile.add(mntmClose);
		
		//add to "Options category
		JMenuItem mntmTheme = new JMenuItem("Theme");
		mntmTheme.setActionCommand(MENU_THEME);
		mntmTheme.addActionListener(this);
		mnOptions.add(mntmTheme);
		
		//add tab view
		tabplane = new JTabbedPane(JTabbedPane.TOP);
		this.getContentPane().add(tabplane, BorderLayout.CENTER);
		
		//set up close button custom
		if(closeOverride != null)
		{
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(closeOverride);
		}
		
		//set visible and center screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setVisible(true);
	}
	
	
	//add new tab
	public void addStop(int num, String name, String stopInfo)
	{
		//create new tab
		Tab tab = new Tab(num, name, listener);
		tab.setText(stopInfo);
		tab.printRefreshTime();
		
		//add tab
		tabs.add(tab);
		tabplane.add(tab);
	}
	
	
	//remove a tab
	public boolean removeTab(int index)
	{
		if(index >= 0 && tabplane.getTabCount()-1 < index)
		{
			tabplane.remove(index);
			tabs.remove(index);
			tabplane.repaint();
			System.out.println(index + "removed");
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
	//get the stop ID from the currently selected stop
	public int getSelectedTab()
	{
		return tabplane.getSelectedIndex();
	}
	
	
	//set a tabs main text area
	public void setTabText(int index, String content)
	{
		Tab toUpdate = tabs.get(index);
		toUpdate.setText(content);
		toUpdate.printRefreshTime();
		
	}
	
	
	//print a message to a tab
	public void printToTab(int index, String printable)
	{
		tabs.get(index).print(printable);
	}
	
	
	//display a error
	public void displayErrorDialog(String title, String message)
	{
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	
	//respond to user input
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		//respond based on action command
		switch(ae.getActionCommand())
		{
			case(StopTimeUI.MENU_THEME):
				//TODO alter theme
				break;
		}
	}

}
