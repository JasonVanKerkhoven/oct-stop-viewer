package ui;

import datatypes.BusStop;

public interface StopDisplay
{
	//update the bus stop info
	public abstract void updateStops(BusStop[] stopArr);
	
	//display text in log or prompt
	public abstract void setInfo(String txt);
	
	//clear error state
	public abstract void clearError();
	
	//set error state high and display message
	public abstract void setError(String msg);
}
