package de.domisum.lib.auxilium.data.container.file;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@APIUsage
public class PropertiesFileWrapper
{

	// PROPERTIES
	private String filePath;

	// REFERENCES
	@Getter private Properties properties;


	/*
	// INITIALIZATION
	*/
	@APIUsage

	public PropertiesFileWrapper(String filePath)
	{
		this.filePath = filePath;

		loadFromFile();
	}


	/*
	// LOADING
	*/
	private void loadFromFile()
	{
		this.properties = new Properties();

		try(InputStream inputStream = new FileInputStream(this.filePath))
		{
			this.properties.load(inputStream);
		}
		catch(java.io.IOException e)
		{
			e.printStackTrace();
		}
	}


	/*
	// SAVING
	*/
	@APIUsage public void saveToFile(File file, String comment)
	{
		try(OutputStream outputStream = new FileOutputStream(file))
		{
			this.properties.store(outputStream, comment);
		}
		catch(java.io.IOException e)
		{
			e.printStackTrace();
		}
	}


	/*
	// GETTERS
	*/
	@APIUsage public String get(String key)
	{
		if(!this.properties.containsKey(key))
			throw new IllegalArgumentException("The properties file '"+this.filePath+"' does not contain the key '"+key+"'");

		return this.properties.getProperty(key);
	}


	/*
	// SETTERS
	*/
	@APIUsage public void set(String key, String value)
	{
		this.properties.setProperty(key, value);
	}

}
