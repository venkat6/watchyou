import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.analysis.JController;
import org.jnetpcap.protocol.application.Html;
import org.jnetpcap.protocol.tcpip.*;
import org.jnetpcap.protocol.tcpip.Http.*;
import java.io.*;
import java.util.zip.*;

public class Test {

	public static class PrintHtml implements HttpHandler {
		
		public void processHttp(Http http) {
			//System.out.printf("\n#%d %s", http.getPacket().getFrameNumber(), http.toString());
			
			// Ensure we are looking at a response object
			if (http.getMessageType() != Http.MessageType.RESPONSE) {
				return;
			}
			
			ContentType type = http.contentTypeEnum();
			/*if (type != ContentType.JPEG) {
				System.out.println(type.toString());
				return;
			}*/
			
			switch (type) {
				case JPEG:
					return;
				case HTML:
					System.out.printf("\n#%d %s", http.getPacket().getFrameNumber(), http.toString());
					
					System.out.println("Payload LENGTH: " + http.getPayloadLength());
					System.out.println("Payload OFFSET: " + http.getPayloadOffset());
					// TODO: charset needs to be dynamic, and decrypt dynamically
					
						Inflater decompresser = new Inflater();
						byte[] buf = new byte[5000];
						
						int i = http.getPayloadOffset();
						int length = http.getPayloadLength() + i > http.size() ? http.size() : http.getPayloadLength();
						http.getPacket().getByteArray(0, buf, i, length);
						decompresser.setInput( buf , 0, length );
						
						// TODO: work this out correctly
						byte[] result = new byte[5000];
						
						// TODO: make sure there isn't more data
					try {
						decompresser.inflate(result);
						decompresser.end();
						
						System.out.println( new String( result, "utf-8") );
					} catch (DataFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					
					
				/*try {
					GZIPInputStream in = new GZIPInputStream( new ByteArrayInputStream( http.getPayload() ) );
					StringBuilder sb = new StringBuilder();
					
					byte[] buf = new byte[1024];
					while ((in.read(buf)) > 0) {
						// TODO: This may write to much data if less then 1024 bytes
						sb.append(buf);
					}
					System.out.println( sb.toString() );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					
					
				
					//System.out.println( http.getPacket().toHexdump() );
					//for( byte b : http.getPayload() )
						//System.out.print( b );
					break;
				default:
					//System.out.printf("Unkown content type %s\n", type);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpAnalyzer httpAnalyzer = JRegistry.getAnalyzer(HttpAnalyzer.class);
		httpAnalyzer.add( new Test.PrintHtml() );
		
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap= Pcap.openOffline("test.pcap", errbuf);
		if (pcap == null) {
			System.err.println(errbuf.toString());
			return;
		}
		
		pcap.loop(Pcap.LOOP_INFINATE, JRegistry.getAnalyzer(JController.class), null);
		
		pcap.close();
	}
	
	
	
}