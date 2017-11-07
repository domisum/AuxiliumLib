package de.domisum.lib.auxilium.data.container.file;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.exceptions.InvalidConfigurationException;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@API
public class PropertiesFileWrapper
{

	private File file;
	private String displayName;

	@Getter private Properties properties;


	// INIT
	@API public PropertiesFileWrapper(File file)
	{
		this(file, "unnamed");
	}

	@API public PropertiesFileWrapper(File file, String displayName)
	{
		this.file = file;
		this.displayName = displayName;

		loadFromFile();
	}


	// LOADING
	private void loadFromFile()
	{
		if(!this.file.exists())
			throw new InvalidConfigurationException(
					"The '"+this.displayName+"' file does not exist ("+this.file.getAbsoluteFile().getAbsolutePath()+")");

		this.properties = new Properties();
		try(InputStream inputStream = new FileInputStream(this.file))
		{
			this.properties.load(inputStream);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	// SAVING
	@API public void save(String comment)
	{
		try(OutputStream outputStream = new FileOutputStream(this.file))
		{
			this.properties.store(outputStream, comment);
		}
		catch(java.io.IOException e)
		{
			e.printStackTrace();
		}
	}


	// GETTERS
	@API public String get(String key)
	{
		if(!this.properties.containsKey(key))
			throw new IllegalArgumentException("The '"+this.displayName+"' properties file does not contain the key '"+key+"'");

		return this.properties.getProperty(key);
	}

	@API public int getInt(String key)
	{
		String valueString = get(key);

		try
		{
			return Integer.parseInt(valueString);
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException("The value saved at '"+key+"' is no integer");
		}
	}


	// SETTERS
	@API public void set(String key, String value)
	{
		this.properties.setProperty(key, value);
	}

}
