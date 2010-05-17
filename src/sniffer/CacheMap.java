package sniffer;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.regex.*;

import org.jnetpcap.packet.*;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Response;

import utilities.PathHash;

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
		if(bag == null)
		{
			System.err.println("Task #1 - No request found for this response"); // TODO - what to do here?
		}
		bag.setResponseHeaderPacket(headerPacket);
		Http http = bag.getResponseHeaderPacket().getHeader(new Http());
		try {
			if(http.hasField(Response.ResponseCode) && http.fieldValue(Response.ResponseCode).equals("200"))
			{
				bag.saveToDisk(PathHash.GetHashedFilePath((bag.getRelativeUrl())), dataPacket);
				if(http.hasField(Response.Content_Type) && http.fieldValue(Response.Content_Type).contains("text/html"))
				{
					String line = bag.getFirstLine();
					if(line != null)
					{
						Pattern doctypePattern = Pattern.compile("\\A\\s*<!DOCTYPE HTML.*$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
						Pattern htmlPattern = Pattern.compile("\\A\\s*<html.*$", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
						if(doctypePattern.matcher(line).matches() || htmlPattern.matcher(line).matches())
						{
							System.out.println("Navigating to url: " + bag.getUrl() + "\n\twith Hashed URL: " + PathHash.GetHashedFilePath(bag.getRelativeUrl()) + "\n");
							Main._gui.navigate(PathHash.GetHashedUrl(bag.getRelativeUrl()));
							Main._gui.getStatsPanel().addTreeNode(bag);
						}
					}
				}
			}
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
