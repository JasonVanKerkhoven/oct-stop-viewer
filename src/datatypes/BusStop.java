/**
 * Class:					BusStop.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2016
 * Version:					1.1.0
 * 
 * Purpose:					Basic datatype to hold information parsed from OC-Transpo API.
 * 
 * 
 * Update Log:				v1.1.0
 * 								- added option to suppress prints for routes with no trips
 * 								- error instance string removed
 * 								  (OCTException now thrown if error encountered during parsing)
 * 								- printing for case where no trips found over ALL routes patched
 * 							v1.0.0
 * 								- Basic constructor added
 * 								- Printable function
 */
package datatypes;



public class BusStop 
{	
	//declaring local instance constants
	public final int stopNum;
	public final String description;
	public final Route[] routes;
	
	
	//generic constructor
	public BusStop(int stopNum, String description, Route[] routes)
	{
		this.stopNum = stopNum;
		this.description = description;
		this.routes = routes;
	}
	
	
	//print info as chart
	public String getPrintable(boolean printEmpty)
	{
		String s = "Stop No. " + stopNum + ": " + description + "\n\n\n";
		if(routes.length > 0)
		{
			//check if any routes have trips
			if(!printEmpty)
			{
				//check routes for trips, exit when 1st instance of trips found
				boolean tripFlag = false;
				for (Route route : routes)
				{
					if (tripFlag = route.isTripsScheduled())
					{
						break;
					}
				}
				
				//return if no trips found
				if (!tripFlag)
				{
					return ("No trips found");
				}
			}
			
			//print each routes information
			for (Route route : routes)
			{
				String routeList = route.getPrintableList(printEmpty);
				if(routeList != null)
				{
					s += routeList + "\n\n\n";
				}
			}
		}
		else
		{
			s += "No scheduled buses found";
		}
		
		return s;
	}
}
