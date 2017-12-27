package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JTextArea;



import datatypes.BusStop;
import datatypes.Route;
import datatypes.Trip;

import java.awt.GridLayout;

public class FancyDisplay extends JFrame implements StopDisplay
{
	//ASCII
	private static final String ASCII_LOGO = 
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
	
	private static final String ASCII_ERROR = 
	"\n\n\n" +
	"8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888\n" +
	"88                                                                                                88\n" + 
	"88                                                                                                88\n" +
	"88        8 8888888888   8 888888888o.   8 888888888o.      ,o888888o.     8 888888888o.          88\n" + 
	"88        8 8888         8 8888    `88.  8 8888    `88.  . 8888     `88.   8 8888    `88.         88\n" + 
	"88        8 8888         8 8888     `88  8 8888     `88 ,8 8888       `8b  8 8888     `88         88\n" + 
	"88        8 8888         8 8888     ,88  8 8888     ,88 88 8888        `8b 8 8888     ,88         88\n" + 
	"88        8 888888888888 8 8888.   ,88'  8 8888.   ,88' 88 8888         88 8 8888.   ,88'         88\n" + 
	"88        8 8888         8 888888888P'   8 888888888P'  88 8888         88 8 888888888P'          88\n" + 
	"88        8 8888         8 8888`8b       8 8888`8b      88 8888        ,8P 8 8888`8b              88\n" + 
	"88        8 8888         8 8888 `8b.     8 8888 `8b.    `8 8888       ,8P  8 8888 `8b.            88\n" + 
	"88        8 8888         8 8888   `8b.   8 8888   `8b.   ` 8888     ,88'   8 8888   `8b.          88\n" + 
	"88        8 888888888888 8 8888     `88. 8 8888     `88.    `8888888P'     8 8888     `88.        88\n" +
	"88                                                                                                88\n" +
	"88                                                                                                88\n" +
	"8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888\n\n";
	
	//declaring class constants
	private static final String BUS_PANEL_ID = "normal";
	private static final String ERR_PANEL_ID = "error";
	private static final Font ASCII_FONT = new Font("Monospaced", Font.PLAIN, 8);
	private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 16);
	private static final Color DEFAULT_BACKGROUND_COLOR = new Color(48, 10, 36);
	private static final Color DEFAULT_TEXT_COLOR = Color.ORANGE;
	private static final Color DEFAULT_PADDING_COLOR = DEFAULT_BACKGROUND_COLOR;
	private static final int PADDING = 15;
	
	//declaring local instance variables
	private JPanel displayCards;
	private JTextArea log, errorMsg;
	private DisplayCell[] cells;
	private CardLayout cardManager;
	private boolean printEmpty;
	private int displayLines;
	private boolean errorState;
	
	
	//return current time
	private static String getCurrentTime() 
	{
	    return new SimpleDateFormat("hh:mm:ss aa").format(new Date());
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
		this.errorState = false;
		
		
		//add padding
		JPanel center, eastPad, westPad, northPad, southPad;
		center = new JPanel();
		center.setBackground(DEFAULT_BACKGROUND_COLOR);
		center.setLayout(new BorderLayout(20,20));
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
		this.getContentPane().add(center, BorderLayout.CENTER);
		this.getContentPane().add(northPad, BorderLayout.NORTH);
		this.getContentPane().add(southPad, BorderLayout.SOUTH);
		this.getContentPane().add(eastPad, BorderLayout.EAST);
		this.getContentPane().add(westPad, BorderLayout.WEST);
		
		
		//add panel to contain main display
		cardManager = new CardLayout(0, 0);
		displayCards = new JPanel(cardManager);
		displayCards.setBackground(DEFAULT_BACKGROUND_COLOR);
		displayCards.setBorder(null);
		center.add(displayCards, BorderLayout.CENTER);
		
		
		//add panels for normal and erroneous operation
		JPanel busPanel = new JPanel(new GridLayout(displayLines, 1, 0, 0));
		busPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
		busPanel.setBorder(null);
		displayCards.add(busPanel, BUS_PANEL_ID);
		
		JPanel errorPanel = new JPanel(new BorderLayout(20,20)); 
		errorPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
		errorPanel.setBorder(null);
		displayCards.add(errorPanel, ERR_PANEL_ID);
		
		cardManager.show(displayCards, BUS_PANEL_ID);
		
		
		//add aux panel
		JPanel auxPanel = new JPanel(new BorderLayout(0,0));
		auxPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
		auxPanel.setBorder(null);
		center.add(auxPanel, BorderLayout.EAST);
		
		
		
		//add to aux panel
		JTextArea header = new JTextArea();
		header.setBackground(DEFAULT_BACKGROUND_COLOR);
		header.setForeground(DEFAULT_TEXT_COLOR);
		header.setEditable(false);
		header.setHighlighter(null);
		header.setFont(ASCII_FONT);
		header.setText(ASCII_LOGO);
		auxPanel.add(header, BorderLayout.NORTH);
		
		log = new JTextArea();
		log.setBorder(null);
		log.setBackground(DEFAULT_BACKGROUND_COLOR);
		log.setForeground(DEFAULT_TEXT_COLOR);
		log.setFont(DEFAULT_FONT);
		log.setEditable(false);
		log.setHighlighter(null);
		auxPanel.add(log, BorderLayout.SOUTH);
		
		
		//add display lines to bus
		cells = new DisplayCell[displayLines];
		for (int i=0; i<displayLines; i++)
		{
			cells[i] = new DisplayCell();
			busPanel.add(cells[i]);
		}

		
		//add text/header to error panel
		JPanel errP = new JPanel(new GridBagLayout());
		errP.setBorder(null);
		errP.setBackground(DEFAULT_BACKGROUND_COLOR);
		errorPanel.add(errP, BorderLayout.NORTH);
		
		JTextArea errHeader = new JTextArea();
		errHeader.setBackground(DEFAULT_BACKGROUND_COLOR);
		errHeader.setForeground(DEFAULT_TEXT_COLOR);
		errHeader.setEditable(false);
		errHeader.setHighlighter(null);
		errHeader.setFont(ASCII_FONT);
		errHeader.setText(ASCII_ERROR);
		errP.add(errHeader);
	
		errorMsg = new JTextArea();
		errorMsg.setBorder(null);
		errorMsg.setBackground(DEFAULT_BACKGROUND_COLOR);
		errorMsg.setForeground(DEFAULT_TEXT_COLOR);
		errorMsg.setFont(DEFAULT_FONT);
		errorMsg.setEditable(false);
		errorMsg.setHighlighter(null);
		errorMsg.setWrapStyleWord(true);
		errorMsg.setLineWrap(true);
		errorPanel.add(errorMsg, BorderLayout.CENTER);
		
		
		//set visible
		if (fullscreen)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			this.setUndecorated(true);
		}
		this.setVisible(true);
	}
	
	
	//set text on onscreen logger
	public void setInfo(String txt)
	{
		log.setText(getCurrentTime() + "\n" + txt);
	}
	
	
	//set error state high or low
	public void clearError()
	{
		//set states
		if (errorState)
		{
			errorState = false;
			errorMsg.setText("");
			cardManager.show(displayCards, BUS_PANEL_ID);
		}
		
	}
	
	
	//set error message
	public void setError(String msg)
	{
		//set states
		if (!errorState)
		{
			errorState = true;
			cardManager.show(displayCards, ERR_PANEL_ID);
		}
		
		//display message
		errorMsg.setText(msg);
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
					//add route info to title
					String routeTitle, r;
					if (route.routeNum == Route.OTRAIN)		r = "O-Train";
					else									r = route.routeNum+"";
					routeTitle = stopTitle + ": " + r + " " + route.direction;
					
					if (route.isTripsScheduled())
					{
						String etas = "";
						for (Trip trip : route.trips)
						{
							//add preface/formating
							if (!etas.isEmpty())
							{
								etas += ", ";
							}
							else
							{
								etas = "ETA ... ";
							}
							
							//add time
							if (trip.adjustedScheduleTime <= 60)
							{
								etas += trip.adjustedScheduleTime;
							}
							else
							{
								int h = trip.adjustedScheduleTime / 60;
								int m = trip.adjustedScheduleTime % 60;
								etas += h + "h" + ":" + m + "m";
							}
							
							//add trailers
							if (trip.adjustmentAge >= 0)
							{
								etas += "*";
							}
							if (trip.isLastTrip)
							{
								etas += " (LAST TRIP)";
							}
						}
						cells[i].setText(routeTitle, etas);
						i++;
					}
					else if (printEmpty)
					{
						cells[i].setText(routeTitle, "No trips avalible");
						i++;
					}
				}
			}
			else
			{
				cells[i].setText(stopTitle, "No routes avalible");
				i++;
			}
		}
	}
}
