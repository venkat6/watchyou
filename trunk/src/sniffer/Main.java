package sniffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.*;
import org.jnetpcap.packet.analysis.JController;
import org.jnetpcap.protocol.tcpip.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

	/**
     * @param args
     */
    public static void main(String[] args) {
    	
        HttpAnalyzer httpAnalyzer = JRegistry.getAnalyzer(HttpAnalyzer.class);
        httpAnalyzer.add( new HttpParser() );
        StringBuilder errbuf = new StringBuilder();
        
        List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf
			    .toString());
			return;
		}

		System.out.println("Network devices found:");

		int i = 0;
		for (PcapIf device : alldevs) {
			String description =
			    (device.getDescription() != null) ? device.getDescription()
			        : "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
		}

		PcapIf device = alldevs.get(0); // We know we have atleast 1 device
		System.out
		    .printf("\nChoosing '%s' on your behalf:\n",
		        (device.getDescription() != null) ? device.getDescription()
		            : device.getName());
		
		
        
		Pcap pcap= Pcap.openLive("\\Device\\NPF_{7487DF0E-2B06-4660-9ED2-6E57E57094AA}", 65535, 1, 4444, errbuf); // wireless
        //Pcap pcap= Pcap.openLive("\\Device\\NPF_{56F5978B-5E2C-4FFF-9CD1-C6D8124A20D3}", 65535, 1, 4444, errbuf); // ethernet
        //Pcap pcap= Pcap.openOffline("specific.pcap", errbuf);
        if (pcap == null) {
                System.err.println(errbuf.toString());
                return;
        }
       
        
        
        
        pcap.loop(Pcap.LOOP_INFINATE, JRegistry.getAnalyzer(JController.class), null);
        //System.out.println("RET: "+ret);
        
        pcap.close();
    }
}

