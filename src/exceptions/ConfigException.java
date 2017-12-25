/**
 * Class:					ConfigException.java
 * Project:					OC-Transpo
 * Author:					Jason Van Kerkhoven
 * Date of Update:			24/12/2017
 * Version:					1.0.0
 * 
 * Purpose:					Generic exception for reading config file
 * 
 * 
 * Update Log:				v1.0.0
 * 								- null
 */
package exceptions;



public class ConfigException extends Exception 
{
	//generic constructor
	public ConfigException(String msg)
	{
		super(msg);
	}
}
