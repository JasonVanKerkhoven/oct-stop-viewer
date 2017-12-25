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
import octy.OctyDisplay;



public abstract class Main 
{
	//execution
	public static void main(String[] args) 
	{
		//init variables
		StopTimeFetcher fetcher = new StopTimeFetcher();
		OctyDisplay display = new OctyDisplay(true);
		
		//declaring config variables
		boolean debug = false;
		boolean squashEmpty = true;
		
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
					break;
				default:
					System.out.println("Unknown flag given, accepted varients: -d, -a");
					System.exit(0);
					break;
			}
		}
		
		//init config
		
		//main update loop
		while (true)
		{
			
		}
	}
}
