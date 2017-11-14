/**
 * Class:					Trip.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2016
 * Version:					1.0.2
 * 
 * Purpose:					Basic datatype to hold information parsed from OC-Transpo API.
 * 							Includes basic methods to compute accurate arrival time
 * 							based on GPS information, as well printing.
 * 
 * Update Log:				v1.0.2
 * 								- compute ArrivalTime() return fixed for real this time
 * 							v1.0.1
 * 								- computeArrivalTime() return fixed to fit format hh:mm
 * 								- "*" character appended to computeArrivalTime() return to
 * 								  denote time obtained from bus's on-board GPS system
 * 							v1.0.0
 * 								- Generic constructor added
 * 								- Method to compute arrival time based on adjusted time
 * 								  added.
 */
package datatypes;



//generic imports
import java.util.Calendar;



public class Trip 
{
	//declaring local instance constants
	public final String destination;
	public final String startTime;
	public final int adjustedScheduleTime;
	public final double adjustmentAge;
	public final boolean isLastTrip;
	public final String busType;
	public final double latitude;
	public final double longitude;
	public final double gpsSpeed;
	
	
	//generic constructor
	public Trip(String destination, 
				String startTime, 
				int adjustedScheduleTime, 
				double adjustmentAge,
				boolean isLastTrip,
				String busType,
				double latitude,
				double longitude,
				double gpsSpeed)
	{
		this.destination = destination;
		this.startTime = startTime;
		this.adjustedScheduleTime = adjustedScheduleTime;
		this.adjustmentAge = adjustmentAge;
		this.isLastTrip = isLastTrip;
		this.busType = busType;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gpsSpeed = gpsSpeed;
	}
	
	
	//compute time the bus will arrive (accounting for schedule adjustment)
	// * denotes time obtained from GPS value
	public String computeArrival()
	{
		//add ETA to current time
		Calendar calendar = Calendar.getInstance();
		int hh = calendar.get(Calendar.HOUR_OF_DAY);
		int mm = calendar.get(Calendar.MINUTE);
		
		//correct hh:mm format
		String time = new String();
		int mmETA = mm + this.adjustedScheduleTime;
		if (mmETA >= 60)
		{
			//calculate ETA and format to 24h clock
			int hhPrint = ((mmETA/60)+hh);
			int mmPrint = (mmETA%60);
			if (hhPrint >= 24)
			{
				hhPrint = hhPrint - 24;
			}
			
			//format to string hh:mm
			if (hhPrint < 10)
			{
				time += "0";
			}
			time += hhPrint + ":";
			if (mmPrint < 10)
			{
				time += "0";
			}
			time += mmPrint;
		}
		else
		{
			time = hh + ":";
			if(mmETA < 10)
			{
				time += "0";
			}
			time += mmETA;
		}
		
		if (adjustmentAge < 0)
		{
			return time + " ";
		}
		else
		{
			return time + "*";
		}
	}
}
