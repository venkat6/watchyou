package sniffer;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.application.Html;
import org.jnetpcap.protocol.tcpip.*;
import org.jnetpcap.protocol.tcpip.Http.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

import gui.BareBonesBrowserLaunch;

public class HttpParser implements HttpHandler {

	
	private Html html = new Html();
	@Override
    public void processHttp(Http http) {
        //System.out.printf("\n#%d %s", http.getPacket().getFrameNumber(), http.toString());
        
        // Ensure we are looking at a response object
        if (http.getMessageType() != Http.MessageType.RESPONSE) {
                return;
        }
        
        System.out.println("YOU ARE FUCKING AMAZING, STEVEN.  YOU FIGURED IT OUT.");
        
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
