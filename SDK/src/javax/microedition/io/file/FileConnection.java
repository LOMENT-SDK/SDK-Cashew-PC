package javax.microedition.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.microedition.io.Connection;

public interface FileConnection extends Connection {

	boolean exists();

	void create()throws IOException;

	void delete()throws IOException;

	OutputStream openOutputStream()throws IOException;

	void close()throws IOException;

	InputStream openInputStream()throws IOException;

	String getURL();

	Enumeration list(String string, boolean b) throws IOException;

	void setFileConnection(String selectedFile)throws IOException;

	String getPath();

	String getName();

	long fileSize();

	void mkdir();


}
