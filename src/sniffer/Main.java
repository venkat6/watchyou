package sniffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.*;
import org.jnetpcap.packet.analysis.JController;
import org.jnetpcap.protocol.tcpip.*;

import watchyou.conf.Config;

import gui.MainFrame;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Main {

	/**
     * @param args
	 * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
    	
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
/*
		PcapIf device = alldevs.get(0); // We know we have atleast 1 device
		System.out
		    .printf("\nChoosing '%s' on your behalf:\n",
		        (device.getDescription() != null) ? device.getDescription()
		            : device.getName());
	*/	
		
        
		Pcap pcap= Pcap.openLive(Config.getCaptureDevice(), 65535, Pcap.MODE_PROMISCUOUS, 4444, errbuf); // config device

		//Pcap pcap= Pcap.openLive("wlan0", 65535, Pcap.MODE_PROMISCUOUS, 4444, errbuf); // steven's wireless
        //Pcap pcap= Pcap.openLive("\\Device\\NPF_{56F5978B-5E2C-4FFF-9CD1-C6D8124A20D3}", 65535, 1, 4444, errbuf); // steven's ethernet
        //Pcap pcap= Pcap.openOffline("chunked.pcap", errbuf);
        if (pcap == null) {
                System.err.println(errbuf.toString());
                return;
        }
        
        /* SET UP UI */
		
		_gui = new MainFrame();
		_gui.setTitle("My browser");
		//_gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		_gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(600, 800);
		_gui.setVisible(true);
		_gui.navigate("about:");
        
		/* END UI SETUP - NOW START THE FRIGGIN MACHINE! */
        
        pcap.loop(Pcap.LOOP_INFINATE, JRegistry.getAnalyzer(JController.class), null);
        //System.out.println("RET: "+ret);
        
        pcap.close();
    }
    public static MainFrame _gui;
}

