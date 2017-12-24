package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import exceptions.ConfigException;

public class ConfigReader
{
	//declaring class constants
	private static final int MIN_PERIOD = 15;
	
	//declaring local instance variables
	private String configPath;
	private String config;
	private int[] stops;
	private int period;
	
	
	//generic constructor
	public ConfigReader(String configPath)
	{
		//init
		this.configPath = configPath;
		this.config = "";
		this.stops = new int[0];
		this.period = -1;
	}
	
	
	//generic setters
	public void setConfigPath(String configPath)
	{
		this.configPath = configPath;
	}
	
	
	//generic getters
	public int[] getStops()
	{
		return stops;
	}
	public int getPeriod()
	{
		return period;
	}
	public String getConfigFilePath()
	{
		return configPath;
	}
	
	
	//parse config file
	public boolean parse() throws ConfigException
	{
		//declaring method variables
		String rawContent = "";
		File cfgFile = null;
		
		//read config file, throw exception if does not exist
		try
		{
			File file = new File(configPath);
			if (file.exists())
			{
				Scanner scanner = new Scanner(file);
				rawContent = scanner.useDelimiter("\\Z").next();
				scanner.close();
			}
			else
			{
				throw new ConfigException("File at \"" + configPath + "\" not found");
			}
		}
		catch (FileNotFoundException e)
		{
			throw new ConfigException("File at \"" + configPath + "\" not found");
		}
		
		//parse config if changed
		if (!rawContent.equals(config))
		{
			//declaring method variables
			int newPeriod = -1;
			LinkedList<Integer> newStops = new LinkedList<Integer>();
			
			//break at new lines, remove tabs+spaces
			String[] lines = (rawContent.replace(" ","").replace("\t","")).split("\\r?\\n");
			
			//nav through each line, keep track of line num
			for (int i=0; i<lines.length; i++)
			{
				//remove any comments
				String line = lines[i];
				if (line.contains("#"))
				{
					line = line.split("#", 2)[0];
				}
				
				//parse if non-empty line
				if (!line.isEmpty())
				{
					//format for update period
					if (line.matches("[0-9]+[s|m|h]"))
					{
						//parse out unit and update period
						char unit = line.charAt(line.length()-1);
						newPeriod = Integer.parseInt(line.substring(0, line.length()-1));
						
						//convert update period to seconds
						switch(unit)
						{
							case('m'):		
								newPeriod = newPeriod*60;
								break;
							case('h'):
								newPeriod = newPeriod*3600;
								break;
						}
						//enforce min update period of 15sec
						if (newPeriod < MIN_PERIOD)
						{
							newPeriod = MIN_PERIOD;
						}
						
					}
					//format for new stop
					else if (line.matches("[0-9]+"))
					{
						newStops.add(Integer.parseInt(line));
					}
					//unknown format
					else
					{
						throw new ConfigException("line "  + (i+1) + ": unknown line format");
					}
				}
			}
			
			//check if valid values were found
			if (newPeriod == -1)
			{
				throw new ConfigException("no update period found");
			}
			else
			{
				//update instance variables
				this.period = newPeriod;
				this.stops = newStops.stream().mapToInt(i->i).toArray();
				this.config = rawContent;
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	
	@Override
	//return useful info as string
	public String toString()
	{
		String s = "period=" + period + ", stops=[";
		boolean f = true;
		for (int i : stops)
		{
			if (!f)		s += ",";
			else		f = false;
			
			s += i;
		}
		return (s + "]");
	}
	
	
	/*
	public static void main(String[] args) throws ConfigException 
	{
		ConfigReader reader = new ConfigReader("config/stops.cfg");
		System.out.println(reader.toString());
		System.out.println(reader.parse());
		System.out.println(reader.parse());
		System.out.println(reader.parse());
		System.out.println(reader.toString());
	}
	*/
}



