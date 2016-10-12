package de.domisum.lib.auxilium.data.container.file;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

@APIUsage
public class PropertiesFileWrapper
{

	// PROPERTIES
	private String filePath;

	// REFERENCES
	private Properties properties;


	/*
	// CONSTRUCTOR
	*/
	@APIUsage
	public PropertiesFileWrapper(String filePath)
	{
		this.filePath = filePath;

		loadFile();
	}

	private void loadFile()
	{
		properties = new Properties();

		try(InputStream inputStream = new FileInputStream(filePath))
		{
			properties.load(inputStream);
		}
		catch(java.io.IOException e)
		{
			e.printStackTrace();
		}
	}


	/*
	// GETTERS
	*/
	@APIUsage
	public String get(String key)
	{
		if(!properties.containsKey(key))
			throw new IllegalArgumentException("The properties file '"+filePath+"' does not contain the key '"+key+"'");

		return properties.getProperty(key);
	}

}
