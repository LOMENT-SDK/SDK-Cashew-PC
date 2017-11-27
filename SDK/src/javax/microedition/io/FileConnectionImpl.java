package javax.microedition.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.microedition.io.file.FileConnection;

class FileConnectionImpl implements FileConnection {
	
	File file =null;
	
	FileConnectionImpl(String url){
		file = new File(url);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(file!=null){
			file=null;
		}
	}

	@Override
	public void create() throws IOException {
		// TODO Auto-generated method stub
		if(file!=null){
			file.createNewFile();
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		if(file!=null){
			file.delete();
		}
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		if(file!=null){
			return file.exists();
		}
		return false;
	}

	@Override
	public InputStream openInputStream() throws FileNotFoundException ,IOException{
		// TODO Auto-generated method stub
		if(file!=null&&file.canRead()){
			return new FileInputStream(file);
		}
		return null;
	}

	@Override
	public OutputStream openOutputStream() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		if(file!=null&&file.canWrite()){
			return new FileOutputStream(file);
		}
		return null;
	}

	@Override
	public long fileSize() {
		// TODO Auto-generated method stub
		if(file!=null){
			return file.length();
		}
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		if(file!=null){
			if(file.isFile())
				return file.getName();
			else
				return file.getName()+File.separator;
		}
		return File.separator;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		if(file!=null){
			if(!file.isFile()){
				String path = file.getParent();
				if(path!=null)
					return file.getParent()+File.separator;
				else
					return File.separator;
			}else{
				return File.separator;
			}
		}
		return File.separator;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		if(file!=null){
			return file.getPath();
		}
		return File.separator;
	}

	@Override
	public Enumeration list(final String string, boolean b) throws IOException {
		// TODO Auto-generated method stub
		return new Enumeration() {
			File[] roots = file.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
//					if(!dir.canWrite()){
//						return false;
//					}
					File current = new File(dir.getAbsolutePath()+File.separator+filename);
					if(current.isDirectory()){
						return true;
					}
					if(string==null||string.equals("*")){
						return true;
					}
					if(filename.endsWith(string.replace("*.", ".").trim())){
						return true;
					}
					return false;
				}
			});
			int index=0;
			@Override
			public boolean hasMoreElements() {
				// TODO Auto-generated method stub
				if(roots!=null&&index<roots.length){
					return true;
				}
				return false;
			}

			@Override
			public Object nextElement() {
				// TODO Auto-generated method stub
				if(roots!=null&&index<roots.length){
					if(roots[index].isDirectory()){
						return roots[index++].getPath()+File.separator;
					}else{
						return roots[index++].getName();
					}
					
				}
				return null;
			}
			
		};
	}

	@Override
	public void setFileConnection(String selectedFile) throws IOException {
		// TODO Auto-generated method stub
		if(file!=null){
			file=new File(selectedFile);
		}
	}

	@Override
	public void mkdir() {
		// TODO Auto-generated method stub
		if(file!=null){
			file.mkdirs();
		}
	}

}
