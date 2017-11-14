/**
 * Class:					StopController.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2017
 * Version:					0.1.0
 * 
 * Purpose:					Launch the main program using either terminal or
 * 							or GUI.
 * 
 * 							If use of terminal, take arguments for stop number and
 * 							print results back to terminal.
 * 
 * 							If use of GUI, create instance of GUI controller.
 * 
 * 
 * Update Log:				v0.1.0
 * 								- basically everything broken from refactoring
 */
package ctrl;



//import libraries
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//import packages
import ui.StopTimeUI;
import ui.Tab;
import ui.dialogs.AddStopDialog;



public class StopTimeController implements ActionListener
{
	//declaring class constants
	public static final String TITLE = "OC Transpo Stop Times";
	public static final String VERSION = "v0.1.0";
	
	//declaring local instance constants
	private final String API_KEY;
	private final String APP_ID;
	
	//declaring local instance variables
	private StopTimeFetcher fetcher;
	private StopTimeUI ui;
	private ArrayList<Integer> tabMapping;
	
	
	//generic constructor
	public StopTimeController(String appId, String apiKey)
	{
		//init
		API_KEY = apiKey;
		APP_ID = appId;
		fetcher = new StopTimeFetcher(APP_ID, API_KEY);
		tabMapping = new ArrayList<Integer>();
		
		//init UI
		WindowAdapter adapter = new WindowAdapter()
		{
		    @Override
		    public void windowClosing(WindowEvent windowEvent)
		    {
		    	//save session info
		    	saveSession(getSessionJSON());
		    	
		    	//exit
		    	System.exit(0);
		    }
		};
		ui = new StopTimeUI(TITLE + " " + VERSION, this, adapter);
	}
	
	
	//get current session info as JSON
	public String getSessionJSON()
	{
		//TODO
		return "<SESSION_JSON>";
	}
	
	
	//save session info to disk
	public void saveSession(String session)
	{
		//TODO
		System.out.println("Writing...\n" + session);
	}
	
	
	//add a new stop for tracking
	public void addStop()
	{
		//get info via dialog
		AddStopDialog dialog = new AddStopDialog(ui, TITLE);
		
		//add stop
		if(dialog.getCloseMode() == AddStopDialog.CLOSE_MODE_OKAY)
		{
			//get bus time info for stop
			try
			{
				int num = dialog.getStopNumber();
				String parsedInfo = fetcher.parseAllBusTimes(fetcher.fetchAllBusTimes(num));
				
				//create new tab
				ui.addStop(num, dialog.getStopName(), parsedInfo);
				tabMapping.add(num);
			}
			//error in getting information from server
			catch (IOException e)
			{
				ui.displayErrorDialog(TITLE, e.getMessage());
			}
		}
	}
	
	
	//remove the currently selected stop
	public void removeStop()
	{
		//get the current tab from ui
		int tab = ui.getSelectedTab();
		System.out.println("selected: " + tab);
		if(tab != -1)
		{
			//remove from local mapping
			tabMapping.remove(0);
			
			//remove from UI
			System.out.println(ui.removeTab(0));
		}
	}
	
	
	//refresh the currently selected stop
	public void refreshSelectedStop()
	{
		//get the current tab from ui
		int tab = ui.getSelectedTab();
		if(tab != -1)
		{
			//get the stop number
			Integer stopNum = tabMapping.get(tab);
			if(stopNum != null)
			{
				try
				{
					//fetch and parse response
					ui.printToTab(tab, "Waiting for Server Response...");
					String parsedInfo = fetcher.parseAllBusTimes(fetcher.fetchAllBusTimes(stopNum));
					ui.setTabText(tab, parsedInfo);
				}
				catch (IOException e)
				{
					//TODO
				}
			}
			else
			{
				//TODO handle this error
			}
		}
	}
	
	
	@Override
	//respond to user actions from UI
	public void actionPerformed(ActionEvent ae) 
	{
		switch(ae.getActionCommand())
		{
			//add new stop
			case(StopTimeUI.MENU_ADD):
				addStop();
				break;
			
			//aux close button
			case(StopTimeUI.MENU_CLOSE):
				saveSession(getSessionJSON());
				System.exit(0);
			
			//update a stop
			case(Tab.BTN_REFRESH):
				refreshSelectedStop();
				break;
			
			//remove the currently selected stop
			case(StopTimeUI.MENU_REMOVE):
				removeStop();
				break;
		}
	}
}
