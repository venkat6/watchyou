package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.EtchedBorder;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.analysis.JController;
import org.jnetpcap.protocol.tcpip.HttpAnalyzer;
import org.mozilla.browser.MozillaPanel;
import org.mozilla.browser.MozillaWindow;

import sniffer.HttpParser;

import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static MainFrame _instance;
	private JPanel jContentPane = null;
	private MozillaPanel browserPanel = null;
	private JPanel statsPanel = null;

	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
		_instance = this;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1024, 768);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout(2, 1);
			gridLayout.setColumns(1);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
			jContentPane.add(getStatsPanel());
			jContentPane.add(getBrowserPanel());
		}
		return jContentPane;
	}

	/**
	 * This method initializes browserPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBrowserPanel() {
		if (browserPanel == null) {
			try {
				browserPanel = new MozillaPanel();
				//browserPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Embedded browser"));
				browserPanel.setSize(500, 400);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return browserPanel;
	}
	
	public void navigate(String url) throws MalformedURLException
	{
		browserPanel.load(url);
	}
	
	public static MainFrame getInstance()
	{
		return _instance;
	}

	/**
	 * This method initializes statsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStatsPanel() {
		if (statsPanel == null) {
			statsPanel = new Home();
			//statsPanel.setLayout(new GridBagLayout());
		}
		return statsPanel;
	}

	public static void main(String[] args) throws Exception
	{
		//frame.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Embedded browser"));
		//frame.navigate("http://www.stevenssite.com");
		
		/* ----- start jnetpcap ------ */
		/*
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
		
		//Pcap pcap= Pcap.openLive("\\Device\\NPF_{7487DF0E-2B06-4660-9ED2-6E57E57094AA}", 65535, 1, 4444, errbuf); // wireless
        Pcap pcap= Pcap.openLive("\\Device\\NPF_{56F5978B-5E2C-4FFF-9CD1-C6D8124A20D3}", 65535, 1, 4444, errbuf); // ethernet
        //Pcap pcap= Pcap.openOffline("specific.pcap", errbuf);
        if (pcap == null) {
                System.err.println(errbuf.toString());
                return;
        }
        
        pcap.loop(Pcap.LOOP_INFINATE, JRegistry.getAnalyzer(JController.class), null);
        
        pcap.close();
        */
	}
}
