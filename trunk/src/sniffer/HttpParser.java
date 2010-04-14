package sniffer;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.analysis.FragmentAssembly;
import org.jnetpcap.protocol.application.Html;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.*;
import org.jnetpcap.protocol.tcpip.Http.*;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import gui.BareBonesBrowserLaunch;

public class HttpParser implements HttpHandler {

	
	private Html html = new Html();
	
	private static HashMap<Integer, HashMap<Integer, HttpBag>> mainMap = 
		new HashMap<Integer, HashMap<Integer, HttpBag>>();
	

	public void processHttp(FragmentAssembly ass)
	{
		JPacket packet = ass.getPacket();
		Http http = new Http();
		if(packet.hasHeader(http))
		{
			// get the first packet in the sequence
			// which has all the other headers (IP, TCP...)
			JPacket first = ass.getFragmentSequence().getPacketSequence().get(0);
			
			processHighLevelHeaders(first, http.getMessageType());
			
			// then call our handler on the http reassembled packet
			//processHttpPacket(http);
		}
	}
	
	@Override
    public void processHttp(Http http) {
		processHighLevelHeaders(http.getPacket(), http.getMessageType());
		//processHttpPacket(http);
	}
	
	private void processHighLevelHeaders(JPacket p, Http.MessageType type)
	{
		Ip4 ip = new Ip4();
		if(!p.hasHeader(ip))
		{
			System.err.println("NO IP HEADER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return;
		}
		Tcp tcp = new Tcp();
		if(!p.hasHeader(tcp))
		{
			System.err.println("NO TCP HEADER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return;
		}
		
		if(type == Http.MessageType.REQUEST)
		{
			System.out.println("Source IP:   " + ip.sourceToInt());
			System.out.println("Source port: " + tcp.source());
			if(!mainMap.containsKey(ip.sourceToInt()))
			{
				mainMap.put(ip.sourceToInt(), new HashMap<Integer, HttpBag>());
			}
			HashMap<Integer, HttpBag> ipMap = mainMap.get(ip.sourceToInt());
			ipMap.put(tcp.source(), new HttpBag(p));
		}
		else
		{
			if(!mainMap.containsKey(ip.destinationToInt()))
			{
				System.err.println("ERROR - key does not exist");
				//mainMap.put(ip.sourceToInt(), new HashMap<Integer, HttpBag>());
			}
			HttpBag bag = mainMap.get(ip.destinationToInt()).get(tcp.destination());
			bag.setResponsePacket(p);
			System.out.println("Dest IP:     " + ip.destinationToInt());
			System.out.println("Dest port:   " + tcp.destination());
		}
		
	}
	
	
	private void processHttpPacket(Http http)
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
	                        
	                		FileOutputStream fs = null;	
	                		try {
	                    		
	                    		int uniqueIdentifier = http.hashCode();
	                    		JPacket packet = http.getPacket();
	                    		boolean doesit = packet.hasHeader(Tcp.ID);
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
		                        		
		
		                            	BareBonesBrowserLaunch.openURL("C:\\" + uniqueIdentifier + ".html");
		                            	//System.out.println(page);
	                        		}
	                        		else
	                        		{
	                        			System.out.println("NO CONTENT-ENCONDING PRESENT");
	                        			System.out.println(page);
	                        		}
	                        		
	                        	}
	                            else
	                            {
	                            	System.out.println("UNABLE TO PARSE HTML, HERE'S THE HEADER:");
	                            	/*if(http.fieldValue(Response.ResponseCode).equals("200")) // TODO - this isn't passing, so we aren't setting it correctly in the c++ code?
	                            	{
	                            		System.out.println(http.getPayload());
	                            	//if(http.getUTF8Char(0) != '<')
	                            	//{
	                            		GZIPOutputStream gout = new GZIPOutputStream(fs);
	                            		gout.write(http.getPayload());
	                            		gout.close();
	                            	//}
	                            	}
	                            	else
	                            	{*/
	                            		//System.out.println("PAYLOAD IS OF LENGTH 0: ");
	                            		//System.out.println(http.getPacket());
	                            	//}
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
	                        
	                        break;
	                default:
	                        System.out.printf("Unknown content type: %s\t-\t%s\n", type, real_content_type);
	                        System.out.println(http.getPacket());
	                        break;
	        }
        }
    }
}
