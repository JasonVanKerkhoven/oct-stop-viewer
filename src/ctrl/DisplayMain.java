/**
 * Class:					DisplayMain.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2017
 * Version:					1.0.1
 * 
 * Purpose:					If use of terminal, take arguments for stop number and
 * 							print results back to terminal.
 * 							If use of GUI, create instance of GUI controller.
 * 
 * 
 * Update Log:				
 * 							v1.0.1
 * 								- added suppression for routes with no trips
 * 								- added parsing for multiple arguments
 * 							v1.0.0
 * 								- added parsing for stops using StopTimeFetcher
 */
package ctrl;



//import libraries
import java.io.IOException;
import org.json.simple.parser.ParseException;

//import packages
import datatypes.BusStop;
import exceptions.ConfigException;
import exceptions.OCTException;
import io.ConfigReader;
import ui.FancyDisplay;
import ui.SimpleDisplay;
import ui.StopDisplay;



public abstract class DisplayMain 
{
	//execution
	public static void main(String[] args) 
	{		
		//declaring given parameters
		boolean debug = false;
		boolean printEmpty = false;
		String configPath = "config/stops.cfg";
		
		//parse parameters
		for (String arg : args)
		{
			switch (arg)
			{
				//set debug true
				case("-d"):
					debug = true;
					break;
				//print all routes (even with no trips)
				case("-a"):
					printEmpty = true;
					break;
				default:
					System.out.println("Unknown flag given, accepted varients: -d, -a");
					System.exit(0);
					break;
			}
		}
		
		//init variables
		boolean error = false;
		StopTimeFetcher fetcher = new StopTimeFetcher();
		ConfigReader config = new ConfigReader(configPath);
		StopDisplay display = new FancyDisplay(true, true, 6);

		//main update loop
		while (true)
		{
			//fetch new information and update
			try 
			{
				//get params from config file
				display.setInfo("Updating...");
				config.parse();
				BusStop[] arr = new BusStop[config.getStops().length];
				
				//get and parse from OC Transpo servers
				for (int i=0; i<config.getStops().length; i++)
				{
					arr[i] = fetcher.fetchAndParseAllStopTimes(config.getStops()[i]);
				}
				
				//update display
				display.setInfo("Idle");
				display.updateStops(arr);
				display.clearError();
			}
			
			//handle possible errors
			catch (ParseException e)
			{
				display.setError("JSON parsing error:\n\n" + e.getMessage());
			}
			catch (IOException e)
			{
				display.setError("Cannot connect to OC Transpo server:\n\n" + e.getMessage());
			}
			catch (ConfigException e) 
			{
				display.setError("Error in .cfg file \"../" + config.getConfigFilePath() + "\":\n\n" + e.getMessage());
			}
			catch (OCTException e)
			{
				display.setError("Error code detected in API response:\n\n" + e.getMessage());
			}
			
			//sleep for period
			finally
			{
				try
				{
					Thread.sleep((config.getPeriod() != -1)?config.getPeriod():ConfigReader.MIN_PERIOD_SEC*1000);
				}
				catch (InterruptedException e)
				{
					//TODO point of no return 
				}
			}
		}
	}
}
