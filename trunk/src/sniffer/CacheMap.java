package sniffer;

import java.util.HashMap;
import java.util.Iterator;

import org.jnetpcap.packet.*;

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
	
	public static void insertResponse(int ip, int port, JPacket p)
	{
		if(!mainMap.containsKey(ip))
		{
			System.err.println("ERROR - key does not exist");
			//mainMap.put(ip.sourceToInt(), new HashMap<Integer, HttpBag>());
		}
		HttpBag bag = mainMap.get(ip).get(port);
		bag.setResponsePacket(p);
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
