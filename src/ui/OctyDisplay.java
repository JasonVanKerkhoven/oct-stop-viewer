package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JTextArea;

public class OctyDisplay extends JFrame
{
	//ASCII
	private static final String ASCII = 
	"\n\n\n" + 
	"    ,o888888o.         ,o888888o.8888888 8888888888    `8.`8888.      ,8'    \n" + 
	" . 8888     `88.      8888     `88.    8 8888           `8.`8888.    ,8'     \n" + 
	",8 8888       `8b  ,8 8888       `8.   8 8888            `8.`8888.  ,8'      \n" + 
	"88 8888        `8b 88 8888             8 8888             `8.`8888.,8'       \n" + 
	"88 8888         88 88 8888             8 8888              `8.`88888'        \n" + 
	"88 8888         88 88 8888             8 8888               `8. 8888         \n" + 
	"88 8888        ,8P 88 8888             8 8888                `8 8888         \n" + 
	"`8 8888       ,8P  `8 8888       .8'   8 8888      '88`       8 8888         \n" + 
	" ` 8888     ,88'      8888     ,88'    8 8888      8888       8 8888         \n" + 
	"    `8888888P'         `8888888P'      8 8888      `88'       8 8888         \n\n"; 
	
	//declaring class constants
	private static final Font ASCII_FONT = new Font("Monospaced", Font.PLAIN, 8);
	private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 16);
	private static final Color DEFAULT_PADDING_COLOR = Color.BLACK;
	private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
	private static final Color DEFAULT_TEXT_COLOR = Color.ORANGE;
	private static final int PADDING = 25;
	
	//declaring local instance variables
	JTextArea header;
	JTextArea timeOuput;
	JTextArea mainOutput;
	
	
	//return current time
	private static String getCurrentTime() 
	{
	    return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	
	//format jtextarea
	private static void initJTextArea(JTextArea a)
	{
		a.setBackground(DEFAULT_BACKGROUND_COLOR);
		a.setForeground(DEFAULT_TEXT_COLOR);
		a.setFont(DEFAULT_FONT);
		a.setEditable(false);
	}
	
	
	//generic constructor
	public OctyDisplay(boolean fullscreen) 
	{
		//init frame
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
		this.getContentPane().setForeground(DEFAULT_TEXT_COLOR);
		this.getContentPane().setLayout(new BorderLayout(0,0));
		
		//add padding
		JPanel mainPanel, eastPad, westPad, northPad, southPad;
		mainPanel = new JPanel();
		mainPanel.setBackground(DEFAULT_PADDING_COLOR);
		mainPanel.setLayout(new BorderLayout(20,20));
		eastPad = new JPanel();
		eastPad.setBackground(DEFAULT_PADDING_COLOR);
		eastPad.setPreferredSize(new Dimension(PADDING,0));
		westPad = new JPanel();
		westPad.setBackground(DEFAULT_PADDING_COLOR);
		westPad.setPreferredSize(new Dimension(PADDING,0));
		northPad = new JPanel();
		northPad.setPreferredSize(new Dimension(0,PADDING));
		northPad.setBackground(DEFAULT_PADDING_COLOR);
		southPad = new JPanel();
		southPad.setPreferredSize(new Dimension(20,PADDING));
		southPad.setBackground(DEFAULT_PADDING_COLOR);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.getContentPane().add(northPad, BorderLayout.NORTH);
		this.getContentPane().add(southPad, BorderLayout.SOUTH);
		this.getContentPane().add(eastPad, BorderLayout.EAST);
		this.getContentPane().add(westPad, BorderLayout.WEST);
		
		
		//add JFrame to hold header
		JPanel auxPanel = new JPanel();
		auxPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
		auxPanel.setLayout(new BorderLayout(20, 20));
		mainPanel.add(auxPanel, BorderLayout.EAST);
		
		//add and config header
		header = new JTextArea();
		initJTextArea(header);
		header.setFont(ASCII_FONT);
		header.setText(ASCII);
		auxPanel.add(header, BorderLayout.NORTH);
		
		//add and config east aux
		timeOuput = new JTextArea();
		initJTextArea(timeOuput);
		auxPanel.add(timeOuput, BorderLayout.SOUTH);
		
		//add and config main output
		mainOutput = new JTextArea();
		initJTextArea(mainOutput);
		mainPanel.add(mainOutput, BorderLayout.CENTER);
		
		//set visible
		if (fullscreen)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			this.setUndecorated(true);
		}
		this.setVisible(true);
		this.setOutput("SAMPLE TEXT");
	}
	
	
	//set main output text
	public void setOutput(String text)
	{
		timeOuput.setText("@ " + getCurrentTime());
		mainOutput.setText(text);
	}
}
