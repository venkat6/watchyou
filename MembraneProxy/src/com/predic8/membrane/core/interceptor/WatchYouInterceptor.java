package com.predic8.membrane.core.interceptor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.predic8.membrane.core.Router;
import com.predic8.membrane.core.config.XMLElement;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.HeaderName;

public class WatchYouInterceptor implements Interceptor {

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "WatchYou Interceptor";
	}

	public String getId() {
		// TODO Auto-generated method stub
		return "WatchYouID";
	}

	public Outcome handleRequest(Exchange exc) throws Exception {
		// TODO Auto-generated method stub
		String uri = exc.getRequest().getUri();
		
		Header hdr = exc.getRequest().getHeader();
		if(hdr != null)
		{
			String referer = hdr.getFirstValue(new HeaderName("Referer"));
			if(referer != null && !referer.isEmpty())
			{
				System.err.println("\nReferer: " + referer + "\n");
				if(referer.contains("localhost:8080"))
				{
					System.err.println("Redirecting...");
					exc.getRequest().setUri(uri.replace(hdr.getHost(), "localhost:8080"));
					hdr.setHost("localhost:8080");
					System.err.println("URI: " + exc.getRequest().getUri());
					System.err.println("Host: " + hdr.getHost());
				}
			}
			
		}
		return Outcome.CONTINUE;
	}

	public Outcome handleResponse(Exchange exc) throws Exception {
		// TODO Auto-generated method stub
		return Outcome.CONTINUE;
	}

	public void setDisplayName(String name) {
		// TODO Auto-generated method stub

	}

	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	public void setRouter(Router router) {
		// TODO Auto-generated method stub

	}

	public XMLElement parse(XMLStreamReader token) throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	public void write(XMLStreamWriter out) throws XMLStreamException {
		// TODO Auto-generated method stub

	}

}
