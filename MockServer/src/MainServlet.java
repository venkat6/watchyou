import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.PathHash;

/**
 * Servlet implementation class MainServlet
 */
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final int FILEBUFFERSIZE = 1024;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
	    //PrintWriter out = response.getWriter();
	    String filename;
	    if(request.getHeader("referer") != null && request.getHeader("referer").contains("localhost:8080"))
    	{
	    	filename = PathHash.GetHashedFilePath(request.getPathInfo());
    	}
	    else
    	{
	    	filename = "C:/" + request.getPathInfo();
    	}
	    File file = new File(filename);
	    int counter = 0;
	    while(!file.exists() && counter < 15)
	    {
	    	try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	counter++;
	    }
	    
	    if(file.isFile() && file.exists())
	    {
		    ServletOutputStream outstream = response.getOutputStream();
		    InputStream in = new FileInputStream(filename);
		    //String mimeType = [ code to get mimetype of data to be served ];
		    byte[] bytes = new byte[FILEBUFFERSIZE];
		    int bytesRead;
	
		    //response.setContentType(mimeType);
	
		    while ((bytesRead = in.read(bytes)) != -1) {
		        outstream.write(bytes, 0, bytesRead);
		    }
	
		    // do the following in a finally block:
		    in.close();
		    outstream.close();
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
