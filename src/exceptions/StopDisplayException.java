/**
 * Class:					StopDisplayException.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			09/01/2018
 * Version:					1.0.0
 * 
 * Purpose:					Generic exception for error displaying bus stops
 * 
 * 
 * Update Log:				v1.0.0
 * 								- null
 */
package exceptions;

public class StopDisplayException extends Exception 
{
	//generic constructor
	public StopDisplayException(String msg)
	{
		super(msg);
	}
}
