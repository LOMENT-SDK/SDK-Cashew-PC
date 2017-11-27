package javax.microedition.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
*
* @author Vijay
*/


public class StreamConnection implements Connection{

	StreamConnection streamConnection;
	OutputStream outputStream;
	Socket internal;
	public StreamConnection(Socket internal) {
		// TODO Auto-generated constructor stub
		this.internal = internal;
	}
	
	public InputStream openInputStream() throws IOException {
		// TODO Auto-generated method stub
		return internal.getInputStream();
	}
	public OutputStream openOutputStream()
    throws IOException{
		return internal.getOutputStream();
		
		
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		internal.close();
	}


}
