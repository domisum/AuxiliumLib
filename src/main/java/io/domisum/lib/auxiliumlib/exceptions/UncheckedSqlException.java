package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.sql.SQLException;

@API
public class UncheckedSqlException
	extends RuntimeException
{
	
	@API
	public UncheckedSqlException(String message, SQLException cause)
	{
		super(message, cause);
	}
	
	@API
	public UncheckedSqlException(String message)
	{
		super(new SQLException(message));
	}
	
	@API
	public UncheckedSqlException(SQLException cause)
	{
		super(cause);
	}
	
}
