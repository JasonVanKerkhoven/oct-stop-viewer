/**
 * Class:					OCTException.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			15/09/2017
 * Version:					1.0.0
 * 
 * Purpose:					Generic exception throw after response with error code
 * 							from an HTTP Req using OCT API
 * 
 * 
 * Update Log:				v1.0.0
 * 								- null
 */
package exceptions;



public class OCTException extends Exception 
{
	//generic constructor
	public OCTException(String msg)
	{
		super(msg);
	}
}
