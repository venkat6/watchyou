package sniffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.analysis.JController;
import org.jnetpcap.protocol.application.Html;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.HttpAnalyzer;
import org.jnetpcap.protocol.tcpip.HttpHandler;
import org.jnetpcap.protocol.tcpip.Http.ContentType;

import util.PathHash;

import java.security.MessageDigest;
import java.util.regex.*;

public class Test
{
	/*
		public static class PrintHtml implements HttpHandler {
                private Html html = new Html();
                public void processHttp(Http http) {
                        System.out.printf("\n#%d %s", http.getPacket().getFrameNumber(), http.toString());
                        
                        // Ensure we are looking at a response object
                        if (http.getMessageType() != Http.MessageType.RESPONSE) {
                                return;
                        }
                        
                        System.out.println("A RESPONSE WAS FOUND!!!");
                }
        }
*/
        public static void main(String[] args) {
        	/*String body = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";//\r\n<!--NewPage-->";
			Pattern p = Pattern.compile("\\A\\s*<!DOCTYPE HTML.*$", Pattern.MULTILINE);
			System.out.println(p.matcher(body).matches());
			*/
        	
        	byte[] arr = new byte[2];
        	arr[0] = (byte)'\r';
        	arr[1] = (byte)'\n';
        	System.out.println(arr.toString());
        	System.out.println(PathHash.getHex(arr));
        	
        	/*
            HttpAnalyzer httpAnalyzer = JRegistry.getAnalyzer(HttpAnalyzer.class);
            //httpAnalyzer.add( new Test.PrintHtml() );
            StringBuilder errbuf = new StringBuilder();
           
            Pcap pcap= Pcap.openOffline("specific.pcap", errbuf);
            if (pcap == null) {
                    System.err.println(errbuf.toString());
                    return;
            }
            
            pcap.loop(Pcap.LOOP_INFINATE, JRegistry.getAnalyzer(JController.class), null);
            
            pcap.close();
            */
        }
       
}