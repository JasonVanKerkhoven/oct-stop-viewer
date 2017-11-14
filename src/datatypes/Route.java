/**
 * Class:					Route.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2016
 * Version:					1.0.1
 * 
 * Purpose:					Basic datatype to hold information parsed from OC-Transpo API.
 * 							
 * 
 * Update Log:				v1.0.1
 * 								- Added method to suppress printing stops with 0 trips
 * 							v1.0.0
 * 								- Basic Constructor added
 */
package datatypes;

public class Route
{
	//declaring local class constants
	public static final String DIV = "\n-------------------------------------";
	
	//declaring local instance constants
	public final int routeNum;
	public final int directionID;
	public final String direction;
	public final String heading;
	public final Trip[] trips;
	
	
	//generic constructor
	public Route(int routeNum, int directionID, String direction, String heading, Trip[] trips)
	{
		this.routeNum = routeNum;
		this.directionID = directionID;
		this.direction = direction;
		this.heading = heading;
		this.trips = trips;
	}
	
	
	//return true if there are any trips scheduled
	public boolean isTripsScheduled()
	{
		if (trips == null)
		{
			return false;
		}
		else if (trips.length == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	//get a nice printable of Route information in a list
	public String getPrintableList(boolean printEmpty)
	{
		String s  = "Route " + routeNum + ":\t" + heading + ", " + direction + DIV;
		
		if (trips != null)
		{
			for (Trip trip : trips)
			{
				s += "\n" + trip.computeArrival() + " to " + trip.destination;
				if(trip.isLastTrip)
				{
					s += "\tLAST TRIP";
				}
			}
			
			return s;
		}
		else if(printEmpty)
		{
			return s + "\nNo trips found";
		}
		else
		{
			return null;
		}
	}
	
}
