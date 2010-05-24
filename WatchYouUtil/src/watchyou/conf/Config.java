package watchyou.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private static Properties props; 
	
	private static final String KEY_OUTPUTDIR = "outputdir";
	private static final String KEY_CAPTUREDEVICE = "capture.device";
	private static final String KEY_PROXY_HOST = "proxy.host";
	private static final String KEY_PROXY_PORT = "proxy.port";
	
	static
	{
		File f = new File(".");
		
		try {
			System.out.println(f.getCanonicalPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(System.getProperty("user.dir") + 
									System.getProperty("file.separator") + 
									"watchyou.properties");
			props.load(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(in != null)
			{
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getOutputDir()
	{
		return props.getProperty(KEY_OUTPUTDIR);
	}
	
	public static String getCaptureDevice()
	{
		return props.getProperty(KEY_CAPTUREDEVICE);
	}
	
	public static String getProxyHost()
	{
		return props.getProperty(KEY_PROXY_HOST);
	}
	
	public static int getProxyPort()
	{
		return Integer.parseInt(props.getProperty(KEY_PROXY_PORT));
	}
}
