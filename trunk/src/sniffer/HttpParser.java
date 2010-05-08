package sniffer;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.analysis.FragmentAssembly;
import org.jnetpcap.protocol.application.Html;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.*;
import org.jnetpcap.protocol.tcpip.Http.*;

import util.PathHash;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import gui.BareBonesBrowserLaunch;

public class HttpParser implements HttpHandler {

	private Html html = new Html();
	
	public void processHttp(FragmentAssembly ass)
	{
		JPacket packet = ass.getPacket();
		Http http = new Http();
		if(packet.hasHeader(http))
		{
			// get the first packet in the sequence
			// which has all the other headers (IP, TCP...)
			JPacket first = ass.getFragmentSequence().getPacketSequence().get(0);
			
			HttpBag bag = processHighLevelHeaders(first, packet, http.getMessageType());
			//CacheMap.ScanResults();
			// then call our handler on the http reassembled packet
			//processHttpPacket(http, http.getMessageType());
		}
	}
	
	@Override
    public void processHttp(Http http) {
		HttpBag bag = processHighLevelHeaders(http.getPacket(), http.getPacket(), http.getMessageType());
		//processHttpPacket(http, http.getMessageType());
		
	}
	
	// note - fullPacket and headerPacket may be the same object, but headerPacket is the packet which
	// contains the detailed Http/IP/TCP headers, and the fullPacket is the packet which contains the 
	// full payload (reassembled possibly)
	private HttpBag processHighLevelHeaders(JPacket headerPacket, JPacket fullPacket, Http.MessageType type)
	{
		Ip4 ip = new Ip4();
		if(!headerPacket.hasHeader(ip))
		{
			System.err.println("NO IP HEADER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return null;
		}
		Tcp tcp = new Tcp();
		if(!headerPacket.hasHeader(tcp))
		{
			System.err.println("NO TCP HEADER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return null;
		}
		
		if(type == Http.MessageType.REQUEST)
		{
			//System.out.println("Source IP:   " + ip.sourceToInt());
			//System.out.println("Source port: " + tcp.source());
			HttpBag bag = new HttpBag(headerPacket);
			CacheMap.insertRequest(ip.sourceToInt(), tcp.source(), bag);
			//System.err.println("Unknown MessageType\n" + p.toHexdump());
			return bag;
		}
		else if(type == Http.MessageType.RESPONSE)
		{
			HttpBag bag = CacheMap.insertResponse(ip.destinationToInt(), tcp.destination(), headerPacket, fullPacket);
			//System.err.println("HASHED PATH: " + PathHash.GetHashedFilePath(bag.getUrl()));
			//System.out.println("Dest IP:     " + ip.destinationToInt());
			//System.out.println("Dest port:   " + tcp.destination());
			return bag;
		}
		else
		{
			System.err.println("Unknown MessageType\n" + headerPacket.toHexdump());
			headerPacket.getHeader(new Http());
			return null;
		}
		
	}
	
	
	private void processHttpPacket(Http http, Http.MessageType messageType)
	{
        //System.out.printf("\n#%d %s", http.getPacket().getFrameNumber(), http.toString());
        
        if (http.getMessageType() == Http.MessageType.REQUEST) {
        	
        }
        else // RESPONSE
        {	        
	        ContentType type = http.contentTypeEnum();     
	        String real_content_type = http.fieldValue(Response.Content_Type);
	
	        switch (type) {
	                case JPEG:
	                		System.out.println("\n\nJPEG\n\n");
	                        break;
	                case PNG:
	                		System.out.println("PNG\n");
	                		break;
	                case GIF:
	                		System.out.println("GIF\n");
	                case HTML:
	                        
	                		//System.out.printf("\n#%d %s", http.getPacket().getFrameNumber(), http.toString());
	                        /*
	                		FileOutputStream fs = null;	
	                		try {
	                    		
	                    		int uniqueIdentifier = http.hashCode();
	                			fs = new FileOutputStream("C:\\" + uniqueIdentifier + ".html");
	                            Html theHtml = http.getPacket().getHeader(html);
	                            if(theHtml != null)
	                            {
	                            	String page = theHtml.page();
	                        		
	                        		if(http.hasField(Response.Content_Encoding))
	                        		{
		                            	if(http.fieldValue(Response.Content_Encoding).equals("gzip"))
		                            	{
		                            		// we have to use the payload, since http has no idea where the
		                            		// compressed header ends
		                            		ByteArrayInputStream stream = new ByteArrayInputStream(http.getPayload());
		                            		GZIPInputStream gin = new GZIPInputStream(stream);
		                            		while(gin.available() != 0)
		                            			fs.write(gin.read());
		                            		gin.close();
		                            	}
		                            	else
		                            	{
		                            		fs.write(http.getPayload());
											fs.close();
		                            	}
		                        		
		
		                            	//BareBonesBrowserLaunch.openURL("C:\\" + uniqueIdentifier + ".html");
		                            	//System.out.println(page);
	                        		}
	                        		else
	                        		{
	                        			System.err.println("NO CONTENT-ENCONDING PRESENT");
	                        			System.err.println(page);
	                        		}
	                        		
	                        	}
	                            else
	                            {
	                            	System.err.println("UNABLE TO PARSE HTML");
	                            }
	                		} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							finally
							{
								if(fs != null)
									try {
										fs.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
	                        */
	                	
	                		
	                	
	                        break;
	                default:
	                        //System.out.printf("Unknown content type: %s\t-\t%s\n", type, real_content_type);
	                        //System.out.println(http.getPacket());
	                        break;
	        }
        }
    }
}
