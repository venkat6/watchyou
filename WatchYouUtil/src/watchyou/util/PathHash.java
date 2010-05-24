package watchyou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import watchyou.conf.Config;

public class PathHash {
	
	private static MessageDigest digest;
	private static final String HEXES = "0123456789ABCDEF";
	
	static
	{
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String GetHashedFilePath(String url)
	{
		return Config.getOutputDir() + GetHexHash(url) + ".sniffed";
	}
	
	public static String GetHashedUrl(String url)
	{		
		//String[] pathparts = url.split("\\?");
		//String filename = (pathparts.length > 0) ? pathparts[0] : url;
		//int index = filename.lastIndexOf('.');
		
		/*
		String ext = filename.substring(index);
		if(ext.endsWith("/"))
		{
			ext = ".html";
		}
		*/
		return "http://localhost:8080/" + GetHexHash(url) + ".sniffed";
	}
	
	private static String GetHexHash(String data)
	{
		if(digest == null)
		{
			return "ERROR-NULL_MESSAGE_DIGEST";
		}

		digest.update(data.getBytes());
		byte[] hash = digest.digest();
		
		return getHex(hash);
	}
	
	public static String getHex(byte[] raw ) {
		if (raw == null) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
		return hex.toString();
  	}

}
