package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NetworkUtil
{

	@API
	public static String getMacAddress()
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
		for(int i = 0; i<macAddressAsBytes.length; i++)
			macAddress.append(String.format("%02X%s", macAddressAsBytes[i], (i<(macAddressAsBytes.length-1)) ?
					"-" :
					""));

		return macAddress.toString();
	}

}
