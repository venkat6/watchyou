package sniffer;

import watchyou.util.PathHash;

public class Test
{

        public static void main(String[] args) {
        	/*String body = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";//\r\n<!--NewPage-->";
			Pattern p = Pattern.compile("\\A\\s*<!DOCTYPE HTML.*$", Pattern.MULTILINE);
			System.out.println(p.matcher(body).matches());
			*/
        	
        	byte[] arr = new byte[2];
        	arr[0] = (byte)'\r';
        	arr[1] = (byte)'\n';
        	System.out.println(arr.toString());
        	System.out.println(PathHash.getHex(arr));
        	
        	
        }
       
}