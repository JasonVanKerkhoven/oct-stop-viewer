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



import datatypes.BusStop;
import datatypes.Route;
import datatypes.Trip;

import java.awt.GridLayout;

public class FancyDisplay extends JFrame implements StopDisplay
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
	private static final Color DEFAULT_BACKGROUND_COLOR = new Color(48, 10, 36);
	private static final Color DEFAULT_TEXT_COLOR = Color.ORANGE;
	private static final Color DEFAULT_PADDING_COLOR = DEFAULT_BACKGROUND_COLOR;
	private static final int PADDING = 15;
	
	//declaring local instance variables
	private DisplayCell[] cells;
	private boolean printEmpty;
	private int displayLines;
	
	
	//return current time
	private static String getCurrentTime() 
	{
	    return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
	
	//generic constructor
	public FancyDisplay(boolean fullscreen, boolean printEmpty, int displayLines)
	{
		//init frame
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
		this.getContentPane().setForeground(DEFAULT_TEXT_COLOR);
		this.getContentPane().setLayout(new BorderLayout(0,0));
		this.printEmpty = printEmpty;
		this.displayLines = displayLines;
		
		
		//add padding
		JPanel mainPanel, eastPad, westPad, northPad, southPad;
		mainPanel = new JPanel();
		mainPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
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
		
		
		//add aux panel and bus panel
		JPanel auxPanel = new JPanel(new BorderLayout(0,0));
		JPanel busPanel = new JPanel(new GridLayout(displayLines, 1, 0, 0));
		auxPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
		busPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
		busPanel.setBorder(null);
		auxPanel.setBorder(null);
		mainPanel.add(auxPanel, BorderLayout.EAST);
		mainPanel.add(busPanel, BorderLayout.CENTER);
		
		
		//add to aux panel
		JTextArea header = new JTextArea();
		header.setBackground(DEFAULT_BACKGROUND_COLOR);
		header.setForeground(DEFAULT_TEXT_COLOR);
		header.setEditable(false);
		header.setHighlighter(null);
		header.setFont(ASCII_FONT);
		header.setText(ASCII);
		auxPanel.add(header, BorderLayout.NORTH);
		
		
		//add display lines to bus
		cells = new DisplayCell[displayLines];
		for (int i=0; i<displayLines; i++)
		{
			cells[i] = new DisplayCell("TITLE --> " +i,"under text");
			busPanel.add(cells[i]);
		}
		
		
		//set visible
		if (fullscreen)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			this.setUndecorated(true);
		}
		this.setVisible(true);
	}
	
	
	//set main output text
	public void updateStops(BusStop[] stopArr)
	{
		int i=0;
		for (BusStop stop : stopArr)
		{
			//form base title
			String stopTitle = "no." + stop.stopNum;
			
			//get routes
			if (stop.routes.length > 0)
			{
				for (Route route : stop.routes)
				{
					String routeTitle = stopTitle + ": " + route.routeNum + " " + route.direction;
					if (route.isTripsScheduled())
					{
						String etas = "";
						for (Trip trip : route.trips)
						{
							if (!etas.isEmpty())
							{
								etas += ", ";
							}
							etas += trip.adjustedScheduleTime;
							if (trip.adjustmentAge >= 0)
							{
								etas += "*";
							}
						}
						cells[i].setText(routeTitle, etas);
					}
					else if (printEmpty)
					{
						cells[i].setText(routeTitle, "No trips avalible");
					}
					else
					{
						i--;
					}
				}
			}
			else
			{
				cells[i].setText(stopTitle, "No routes avalible");
			}
			i++;
		}
	}
}
