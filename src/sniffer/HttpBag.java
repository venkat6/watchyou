package sniffer;

import org.jnetpcap.packet.JPacket;

public class HttpBag {
	
	private String _url; 
	private JPacket _request;
	private JPacket _response;
	
	public HttpBag(JPacket request)
	{
		//this._url = url;
		this._request = request;
	}
	
	public String getUrl()
	{
		return _url;
	}
	
	public JPacket getRequestPacket()
	{
		return _request;
	}
	
	public JPacket getResponsePacket()
	{
		return _response;
	}
	
	public void setResponsePacket(JPacket response)
	{
		this._response = response;
	}
}
