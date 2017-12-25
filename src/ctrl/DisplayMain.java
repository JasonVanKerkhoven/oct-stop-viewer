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
import ui.OctyDisplay;



public abstract class DisplayMain 
{
	//execution
	public static void main(String[] args) 
	{		
		//declaring running flags
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
		StopTimeFetcher fetcher = new StopTimeFetcher();
		ConfigReader config = new ConfigReader(configPath);
		OctyDisplay display = new OctyDisplay(true);
		
		//main update loop
		while (true)
		{
			try 
			{
				config.parse();
				String s = "";
				for (int stopNo : config.getStops())
				{
					s += fetcher.fetchAndParseAllStopTimes(stopNo).getPrintable(printEmpty);
				}
				display.setOutput(s);
				
				//sleep
				Thread.sleep(config.getPeriod());
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (ConfigException e) 
			{
				e.printStackTrace();
			}
			catch (OCTException e)
			{
				e.printStackTrace();
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
