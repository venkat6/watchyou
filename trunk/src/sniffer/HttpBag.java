package sniffer;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Request;

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
		Http http = new Http();
		_request.getHeader(http);
		return http.fieldValue(Request.Host) + http.fieldValue(Request.RequestUrl);
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
