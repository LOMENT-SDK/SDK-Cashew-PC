package javax.microedition.io;

import java.io.IOException;

import javax.microedition.io.file.FileConnection;
import javax.wireless.messaging.MessageConnection;



public class Connector {

	public static final int READ_WRITE = 2;
	public static final int READ = 0;

//	public static Connection open(String serverUrl) throws IOException{
//		// TODO Auto-generated method stub
////		if(serverUrl.startsWith("sms://")){
////			return MessageConnectionImpl.getInstance();
////		}
////		return null;
//	}

	public static Connection open(String string, int readWrite) throws IOException{
		// TODO Auto-generated method stub
		if(string.startsWith("file://")){
			return new FileConnectionImpl(string.substring(7));
		}
		else if(string.startsWith("http://") || string.startsWith("https://")){
			return new HTTPConnectionImpl(string);
		}
		return null;
			
	}
	
	public static Connection open(String string) throws IOException{
		// TODO Auto-generated method stub
		if(string.startsWith("file://")){
			return new FileConnectionImpl(string.substring(7));
		}
		else if(string.startsWith("http://") || string.startsWith("https://")){
			return new HTTPConnectionImpl(string);
		}
		return null;
			
	}

}
