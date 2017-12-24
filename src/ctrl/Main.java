/**
 * Class:					Main.java
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
import exceptions.OCTException;



public abstract class Main 
{
	//declaring class constants
	private static final String APP_ID 		= "6b5dfc58";
	private static final String API_KEY 	= "1bac140937e949eace1827738526c557";
	

	//execution
	public static void main(String[] args) 
	{
		//declaring config variables
		boolean debug = false;
		boolean squashEmpty = true;
		Integer stopNum = null;
		Integer updateFreq = null;		//call limit of 10k per day ==> 6.94 per min
		
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
					squashEmpty = false;
				
				
			}
		}
		
		
		//run octy
		if(args.length <= 0 || args[0].equals("gui"))
		{
			System.out.println("GUI non-functional");
		}
		//one shot for running used in terminal
		else
		{
			//declaring possible parameters
			int stopNo = 0;
			boolean printEmpty = true;
			
			try
			{
				if(args.length == 1)
				{
					printEmpty = false;
					stopNo = Integer.parseInt(args[0]);
				}
				else if(args.length >= 2)
				{
					printEmpty = (args[0].equals("printall") || args[0].equals("true") || args[0].equals("1"));
					stopNo = Integer.parseInt(args[1]);
				}
				
				//create new fetcher and check stop num for validity
				StopTimeFetcher fetcher = new StopTimeFetcher(APP_ID, API_KEY);
				
				//fetch and parse bus info
				System.out.println("Fetching...");
				BusStop stop = fetcher.fetchAndParseAllStopTimes(stopNo);
				
				//print
				System.out.println("\n" + stop.getPrintable(printEmpty));
			}
			catch (NumberFormatException e)
			{
				System.out.println("Stop number must be integer in range 0 - " + Integer.MAX_VALUE);
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
				System.out.println("Cannot reach URL \"" + StopTimeFetcher.GET_ALL_BUS_TIMES_URL + "\"");
			}
			catch (ParseException e)
			{
				System.out.println(e.getMessage());	//TODO HANDLE
			}
			catch (OCTException e)
			{
				System.out.println(e.getMessage()); //TODO HANDLE
			}
		}
	}
}
