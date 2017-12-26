/**
 * Class:					StopTimeFetcher.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2017
 * Version:					1.2.2
 * 
 * Purpose:					Fetch bus times form OCTranspo API
 * 							Uses generic HTTP requests.
 * 
 * 
 * Update Log:				v1.2.2
 * 								- surpress print for empty trips
 * 							v1.2.1
 * 								- Parsing for singular route (ie not in JSONArray) added
 * 								- Parsing for singular trip (ie not in JSONArray) improved
 * 								- No longer crashes when parsing stop with only 1 route
 * 							v1.2.0
 * 								- Parsing array bug patched
 * 								- Now handles case that trips array is given, but empty
 * 							v1.1.0
 * 								- Parsing for all stop JSON added
 * 								- Now handles case that trips array is not given
 * 							v1.0.0
 * 								- HTTP Req for get all stop info added
 */
package ctrl;



//import libraries
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

//import packages
import datatypes.BusStop;
import datatypes.Route;
import datatypes.Trip;
import exceptions.OCTException;



public class StopTimeFetcher 
{
	//declaring static class constants
	private static final String APP_ID_DEFAULT 	= "6b5dfc58";
	private static final String API_KEY_DEFAULT = "1bac140937e949eace1827738526c557";
	private static final String HTTP_FORMAT = "json";
	private static final String RESPONSE_FORMAT = "UTF-8";
	
	public static final String GET_ALL_BUS_TIMES_URL = "http://api.octranspo1.com/v1.2/GetNextTripsForStopAllRoutes";
	
	//declaring possible error codes
	public static final int ERROR_BAD_KEY 		= 1;
	public static final int ERROR_BAD_QUERY 	= 2;
	public static final int ERROR_BAD_STOPNO	= 10;
	public static final int ERROR_BAD_ROUTENO	= 11;
	public static final int ERROR_NO_ROUTE		= 12;
	
	//declaring local instance constants
	private final String APP_ID;
	private final String API_KEY;
	
	
	//generic constructor
	public StopTimeFetcher()
	{
		this(APP_ID_DEFAULT, API_KEY_DEFAULT);
	}
	
	//custom id/key
	public StopTimeFetcher(String id, String key)
	{
		APP_ID = id;
		API_KEY = key;
	}
	
	
	//decode error code
	private String decodeError(int errorCode)
	{
		String s = "Error " + errorCode + ": ";
		switch(errorCode)
		{
			case(ERROR_BAD_KEY):		return s += "Invalid API key";
			case(ERROR_BAD_QUERY):		return s += "Unable to query data source";
			case(ERROR_BAD_STOPNO):		return s += "Invalid stop number";
			case(ERROR_BAD_ROUTENO):	return s += "Invalid route number";
			case(ERROR_NO_ROUTE):		return s += "Stop does not service route";
			default:					return s += "Unknown error code";
		}
	}
	
	
	//send HTTP req to OC Transpo servers, return raw JSON
	public String fetchAllBusTimes(int stopNum) throws IOException
	{
		//build URL
		String uri =	"appID=" + APP_ID +				//add ID
						"&apiKey=" + API_KEY +			//add key
						"&stopNo=" + stopNum +			//add stop number
						"&format="  + HTTP_FORMAT;		//set format
		String urlFull = GET_ALL_BUS_TIMES_URL + "?" + uri;
		InputStream dataStream = null;
		
		//open stream for HTTP req
        try 
        {
        	//init data stream from http server
    		dataStream = new URL(urlFull).openStream();
    		
        	//open buffered reader and read all data from stream (UTF-8)
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream, Charset.forName(RESPONSE_FORMAT)));
            
            //read all bytes as UTF-8 characters in buffer
            StringBuilder rawChars = new StringBuilder();
            int currentChar;
            while ((currentChar = reader.read()) != -1) 
            {
            	rawChars.append((char)currentChar);
            }
            
            //return JSON response from server         
            return rawChars.toString();
        }
        //append URL to exception for debug
        catch (IOException e)
        {
        	throw new IOException (e.getMessage()+"\nHTTP Req used: "+urlFull);
        }
        //close streams
        finally 
        {
        	if(dataStream != null)
        	{
        		dataStream.close();
        	}
        }
	}
	
	
	//parse JSON file for all bus times
	public BusStop fetchAndParseAllStopTimes(int stopNum) throws IOException, ParseException, OCTException
	{
		//fetch stop info
		String json = fetchAllBusTimes(stopNum);
		System.out.println(json); //DEBUG

		//declaring method variables
		JSONParser parser = new JSONParser();
		String jsonString;
		JSONObject layer;

		//declaring parsed data
		int stopNo;
		String stopDesc;
		Route[] routes;

		//unwrap header into string
		parser = new JSONParser();
		layer = (JSONObject)parser.parse(json);
		jsonString = layer.get("GetRouteSummaryForStopResult").toString();

		try
		{
			//unwrap stop info and save
			layer = (JSONObject)parser.parse(jsonString);
			String error = layer.get("Error").toString();

			//check for error before proceeding
			if (!error.isEmpty())
			{
				try
				{
					//decode error if only contains error code
					error = this.decodeError(Integer.parseInt(error));
				}
				catch (NumberFormatException e){}

				//throw exception
				throw new OCTException(error);
			}

			stopNo = Integer.parseInt(layer.get("StopNo").toString());
			stopDesc = layer.get("StopDescription").toString();
			jsonString = layer.get("Routes").toString();

			//unwrap route information
			JSONArray routeArr = null;
			Object routeLayer = parser.parse(jsonString);
			if (routeLayer.getClass() == JSONArray.class)
			{
				routeArr = (JSONArray)routeLayer;
			}
			else if (routeLayer.getClass() == JSONObject.class)
			{
				layer = (JSONObject)routeLayer;
				jsonString = layer.get("Route").toString();

				//check if multiple routes nested in array
				Object parsed = parser.parse(jsonString);
				if (parsed.getClass() == JSONArray.class)
				{
					routeArr = (JSONArray)parsed;
				}
				else if (parsed.getClass() == JSONObject.class)
				{
					routeArr = new JSONArray();
					routeArr.add(parsed);
				}
				else
				{
					System.out.println("ERROR");		//TODO handle this
					System.exit(0);
				}
			}
			else
			{
				System.out.println("ERROR");		//TODO handle this
				System.exit(0);
			}


			//iterate through routes
			routes = new Route[routeArr.size()];
			for (int c=0; c < routeArr.size(); c++)
			{
				//declaring route states
				int routeNo;
				int dirID;
				String dir;
				String heading;
				Trip[] trips;

				//parse data for route
				layer = (JSONObject)parser.parse(routeArr.get(c).toString());
				routeNo = Integer.parseInt(layer.get("RouteNo").toString());
				dirID = Integer.parseInt(layer.get("DirectionID").toString());
				dir = layer.get("Direction").toString();
				heading = layer.get("RouteHeading").toString();
				jsonString = layer.get("Trips").toString();
				
				//unwrap trip information into array (OC Transpo returns 5 different ways due to partial updates on their API >:[ )
				if (jsonString != null && !jsonString.isEmpty())
				{
					JSONArray tripArr = null;
					Object tripParsed = parser.parse(jsonString);
					if (tripParsed.getClass() == JSONObject.class)
					{
						layer = (JSONObject)parser.parse(jsonString);
						System.out.println(layer.toString());
						Object tripGet = layer.get("Trip");
						
						//trip information nested in Trip
						if (tripGet != null)
						{
							jsonString = tripGet.toString();
							
							//check if multiple routes nested in array
							tripParsed = parser.parse(jsonString);
							if (tripParsed.getClass() == JSONArray.class)
							{
								tripArr = (JSONArray)tripParsed;
							}
							else if (tripParsed.getClass() == JSONObject.class)
							{
								tripArr = new JSONArray();
								tripArr.add(tripParsed);
							}
							else
							{
								System.out.println("ERROR");		//TODO handle this
								System.exit(0);
							}
						}
						//trip information given explicitly 
						else
						{
							tripArr = new JSONArray();
							tripArr.add((JSONObject)parser.parse(jsonString));
						}
					}
					else if (tripParsed.getClass() == JSONArray.class)
					{
						tripArr = (JSONArray)tripParsed;
					}
					else
					{
						System.out.println("ERROR");		//TODO handle this
						System.exit(0);
					}
					
					//prep array to hold trips
					trips = new Trip[tripArr.size()];
					
					//iterate through all JSON objects in array
					for (int i=0; i < tripArr.size(); i++)
					{
						//declaring trip states
						String dest, startTime, type;
						int adjTime;
						boolean lastTrip;
						double adjAge, lat, lon, gps;
	
						//parse data from trip at index
						layer = (JSONObject)parser.parse(tripArr.get(i).toString());
						//save mandatory fields and convert to relevant datatype
						dest = layer.get("TripDestination").toString();
						startTime = layer.get("TripStartTime").toString();
						adjTime = Integer.parseInt(layer.get("AdjustedScheduleTime").toString());
						adjAge = Double.parseDouble(layer.get("AdjustmentAge").toString());
						lastTrip = Boolean.parseBoolean(layer.get("LastTripOfSchedule").toString());
						type = layer.get("BusType").toString();
	
						//check if latitude data exists and save
						String s;
						if ((s = layer.get("Latitude").toString()).isEmpty())
						{
							lat = Double.NaN;
						}
						else
						{
							lat = Double.parseDouble(s);
						}
						//check if longitude data exists and save
						if ((s = layer.get("Longitude").toString()).isEmpty())
						{
							lon = Double.NaN;
						}
						else
						{
							lon = Double.parseDouble(s);
						}
						//check if GPS Speed data exists and save
						if ((s = layer.get("GPSSpeed").toString()).isEmpty())
						{
							gps = Double.NaN;
						}
						else
						{
							gps = Double.parseDouble(s);
						}
	
						//construct trip and save to master trip array
						trips[i] = new Trip(dest,
								startTime,
								adjTime,
								adjAge,
								lastTrip,
								type,
								lat,
								lon,
								gps);
					}
				}
				else
				{
					trips = new Trip[0];
				}

				//save route to master route array
				routes[c] = new Route(routeNo, dirID, dir, heading, trips);
			}

			//construct and return BusStop data
			return new BusStop(stopNo, stopDesc, routes);
		}
		catch (NumberFormatException e)
		{
			throw new ParseException(0);		//TODO replace 0 with non-null parameter
		}
	}
	
	
	//execution
	public static void main(String[] args) 
	{
		//declaring config variables
		boolean debug = false;
		boolean printEmpty = false;
		int stopNo = -1;
		
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
					try
					{
						stopNo = Integer.parseInt(arg);
					}
					catch (NumberFormatException e)
					{
						System.out.println("Stop number must be integer in range 0 - " + Integer.MAX_VALUE);
						System.exit(0);
					}
					break;
			}
		}
		
		//get stop info
		if (stopNo > 0)
		{
			try
			{
				//create new fetcher
				StopTimeFetcher fetcher = new StopTimeFetcher();
				
				//fetch and parse bus info
				System.out.println("Fetching...");
				BusStop stop = fetcher.fetchAndParseAllStopTimes(stopNo);
				
				//print
				System.out.println("\n" + stop.getPrintable(printEmpty));
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
		else
		{
			System.out.println("Must give valid stop number");
		}
	}
}
