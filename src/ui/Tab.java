package ui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Tab extends JPanel 
{
	//declaring local class constants
	public static final String BTN_REFRESH = "btn/refresh";
	private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 13);
	
	//declaring local instance variables
	private JTextArea text;
	private JTextField outputLine;
	private int id;
	
	
	//generic constructor
	public Tab(int stopNum, String name, ActionListener listener) 
	{	
		//set up panel
		this.setLayout(new BorderLayout(0, 0));
		this.id = stopNum;
		
		//init and add text area for schedule printout
		JScrollPane scroll = new JScrollPane();
		this.add(scroll, BorderLayout.CENTER);
		text = new JTextArea("");
		text.setFont(DEFAULT_FONT);
		text.setEditable(false);
		scroll.setViewportView(text);
		
		//panel container to south portion of screen
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout(0, 0));
		add(southPanel, BorderLayout.SOUTH);
		
		//add refresh button
		JButton btnRefresh = new JButton("Update Times");
		btnRefresh.setFont(DEFAULT_FONT);
		btnRefresh.setActionCommand(BTN_REFRESH);
		btnRefresh.addActionListener(listener);
		southPanel.add(btnRefresh, BorderLayout.EAST);
		
		//add output line for text display
		outputLine = new JTextField();
		outputLine.setFont(DEFAULT_FONT);
		outputLine.setHorizontalAlignment(SwingConstants.LEFT);
		outputLine.setColumns(10);
		southPanel.add(outputLine, BorderLayout.CENTER);
		
		//set other tab metadata
		this.setVisible(true);
		this.setName(name);
	}

	
	//set main text area
	public void setText(String txt)
	{
		text.setText(txt);
	}
	
	
	//clear main text area
	public void clear()
	{
		text.setText("");
	}
	
	
	//print string to output line
	public void print(String printable)
	{
		outputLine.setText(printable);
	}
	
	
	//set output line to current time
	public void printRefreshTime()
	{
		this.print("Updated at " + new SimpleDateFormat("hh:mm:ss").format(new Date()));
	}
}
