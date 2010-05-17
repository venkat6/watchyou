package sniffer;

import java.io.*;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Request;
import org.jnetpcap.protocol.tcpip.Http.Response;

public class HttpBag {
	
	private final char[] CRLF = { '\r', '\n' }; 
	
	private JPacket _requestHeader;
	private JPacket _responseHeader;
	private String _filename;
	
	public HttpBag(JPacket request)
	{
		this._requestHeader = request;
	}
	
	public String getUrl()
	{
		Http http = new Http();
		_requestHeader.getHeader(http);
		return http.fieldValue(Request.Host) + http.fieldValue(Request.RequestUrl);
	}
	
	public String getRelativeUrl()
	{
		Http http = _requestHeader.getHeader(new Http());
		return http.fieldValue(Request.RequestUrl);
	}
	
	public String getFirstLine()
	{
		File file = new File(_filename);
		if(file.length() <= 0)
			return null;
		Scanner s;
		try {
			s = new Scanner(file);
			return s.nextLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveToDisk(String filename, JPacket dataPacket)
	{
		Http http = new Http();
		Http dataHttp = new Http();
		_responseHeader.getHeader(http);
		dataPacket.getHeader(dataHttp);
		FileOutputStream fs;
		try {
			fs = new FileOutputStream(filename);
	    	ByteArrayOutputStream ms = null;
			// dechunk first
			if(http.isChunked())
    		{
				ms = new ByteArrayOutputStream();
				String lenstr;
				int CL_length, chunklen;
    			JPacket packet = dataHttp.getPacket();
				int i = dataHttp.getPayloadOffset();
				while(i < dataHttp.getPayloadOffset() + dataHttp.getPayloadLength())
    			{
					CL_length = packet.findUTF8String(i, CRLF) - CRLF.length;
	    			
    				lenstr = packet.getUTF8String(i, CL_length);
    				chunklen = Integer.valueOf(lenstr.trim(), 16).intValue();
    				if(chunklen > 0)
    					ms.write(packet.getByteArray(i + CL_length + CRLF.length, chunklen));
    				
    				i += CL_length + CRLF.length + chunklen + CRLF.length;
    			}
    		}
			
			// then gzip if necessary
			if(http.hasField(Response.Content_Encoding) && 
				http.fieldValue(Response.Content_Encoding).equals("gzip"))
	    	{	    				
	    		//System.out.println(dataHttp.getPacket().toHexdump(1500, true, true, true));
	    		ByteArrayInputStream stream = (ms == null) ? new ByteArrayInputStream(dataHttp.getPayload()) 
	    													: new ByteArrayInputStream(ms.toByteArray());
	    		GZIPInputStream gin = new GZIPInputStream(stream);
	    		while(gin.available() != 0)
	    			fs.write(gin.read());
	    		gin.close();
	    	}
	    	else if(dataHttp.hasPayload())
	    	{
	    		if(ms != null)
    			{
    				fs.write(ms.toByteArray());
    			}
	    		else
	    		{
	    			fs.write(dataHttp.getPayload());
	    		}
	    		fs.close();
	    	}
	    	else
	    	{
	    		fs.close();
	    	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this._filename = filename;
	}
	
	public JPacket getRequestPacket()
	{
		return _requestHeader;
	}
	
	public JPacket getResponseHeaderPacket()
	{
		return _responseHeader;
	}
	
	public void setResponseHeaderPacket(JPacket responseHeader)
	{
		this._responseHeader = responseHeader;
	}
	
	public String toString()
	{
		return this.getUrl();
	}
	
}
