package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

@API
public final class NetworkUtil
{

	private NetworkUtil()
	{
		throw new UnsupportedOperationException();
	}


	@API public static String getMacAddress()
	{
		byte[] macAddressAsBytes;
		try
		{
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			macAddressAsBytes = network.getHardwareAddress();
		}
		catch(SocketException|UnknownHostException e)
		{
			throw new UncheckedIOException(e);
		}

		StringBuilder macAddress = new StringBuilder();
		for(int i = 0; i < macAddressAsBytes.length; i++)
			macAddress.append(String.format("%02X%s", macAddressAsBytes[i], (i < (macAddressAsBytes.length-1)) ? "-" : ""));

		return macAddress.toString();
	}

}
