package sniffer;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.regex.*;

import org.jnetpcap.packet.*;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Response;

import util.PathHash;

public class CacheMap { // implements Iterable<HttpBag>

	private static HashMap<Integer, HashMap<Integer, HttpBag>> mainMap = 
		new HashMap<Integer, HashMap<Integer, HttpBag>>();
	
	public static void insertRequest(int ip, int port, HttpBag bag)
	{
		if(!mainMap.containsKey(ip))
		{
			mainMap.put(ip, new HashMap<Integer, HttpBag>());
		}
		HashMap<Integer, HttpBag> ipMap = mainMap.get(ip);
		ipMap.put(port, bag);
	}
	
	public static HttpBag insertResponse(int ip, int port, JPacket headerPacket, JPacket dataPacket)
	{
		if(!mainMap.containsKey(ip))
		{
			System.err.println("ERROR - key does not exist");
			//mainMap.put(ip.sourceToInt(), new HashMap<Integer, HttpBag>());
		}
		HttpBag bag = mainMap.get(ip).get(port);
		bag.setResponseHeaderPacket(headerPacket);
		Http http = bag.getResponseHeaderPacket().getHeader(new Http());
		try {
			if(http.hasField(Response.ResponseCode) && !http.fieldValue(Response.ResponseCode).equals("304"))
			{
				bag.saveToDisk(PathHash.GetHashedFilePath((bag.getUrl())), dataPacket);
				if(http.fieldValue(Response.Content_Type).contains("text/html"))
				{
					String line = bag.getFirstLine();
					if(line != null)
					{
						Pattern doctypePattern = Pattern.compile("\\A\\s*<!DOCTYPE HTML.*$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
						Pattern htmlPattern = Pattern.compile("\\A\\s*<html.*$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
						if(doctypePattern.matcher(line).matches() || htmlPattern.matcher(line).matches())
						{
							System.out.println("Navigating to url: " + bag.getUrl() + "\n\twith Hashed URL: " + PathHash.GetHashedFilePath(bag.getUrl()) + "\n");
							Main._gui.navigate(PathHash.GetHashedUrl(bag.getUrl()));
						}
					}
				}
			}
		} 
		catch (NullPointerException e)
		{
			// do nothing
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bag;
	}
	
	public static String getRequestUrlForIpAndPort(int ip, int port)
	{
		return mainMap.get(ip).get(port).getUrl();
	}

	public static void ScanResults()
	{
		System.out.println("---------------------------------------");
		for(Integer i : mainMap.keySet())
		{
			HashMap<Integer, HttpBag> bags = mainMap.get(i);
			for(Integer j : bags.keySet())
			{
				System.out.println(bags.get(j).getUrl());
			}
		}
		System.out.println("---------------------------------------");

	}
}
