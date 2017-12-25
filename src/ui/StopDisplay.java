package ui;

import datatypes.BusStop;

public interface StopDisplay
{
	//update the bus stop info
	public abstract void updateStops(BusStop[] stopArr);
}
