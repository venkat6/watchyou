package gui;

import java.awt.GridLayout;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.mozilla.browser.MozillaExecutor;
import org.mozilla.browser.MozillaPanel;
import org.mozilla.browser.XPCOMUtils;
import org.mozilla.interfaces.nsIPrefBranch;

import watchyou.conf.Config;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static MainFrame _instance;
	private JPanel jContentPane = null;
	private MozillaPanel browserPanel = null;
	private Home statsPanel = null;

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
				// set the HTTP proxy we are going to use in the browser
				MozillaExecutor.mozSyncExec(new Runnable() { public void run() {
				    nsIPrefBranch pref =
				    	XPCOMUtils.getService("@mozilla.org/preferences-service;1",
				    			nsIPrefBranch.class);
				    pref.setCharPref("network.proxy.http", Config.getProxyHost());
				    pref.setIntPref("network.proxy.http_port", Config.getProxyPort());
				    pref.setIntPref("network.proxy.type", 1);
				}});

				//browserPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Embedded browser"));
				//browserPanel.setMinimumSize(new Dimension(500, 800));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return browserPanel;
	}
	
	/**
	 * This method initializes statsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public Home getStatsPanel() {
		if (statsPanel == null) {
			statsPanel = new Home();
			//statsPanel.setLayout(new GridBagLayout());
		}
		return statsPanel;
	}
	
	
	/* Public access methods for the sniffer */
	
	public void navigate(String url) throws MalformedURLException
	{
		browserPanel.load(url);
	}
	
	public static MainFrame getInstance()
	{
		return _instance;
	}
		
}
