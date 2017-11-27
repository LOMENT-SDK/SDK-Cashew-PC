package javax.microedition.io.file;

import java.io.File;
import java.util.Enumeration;

public class FileSystemRegistry {

	public static Enumeration listRoots() {
		// TODO Auto-generated method stub
		return new Enumeration() {
			File[] roots = File.listRoots();
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
					return roots[index++].getAbsolutePath();
				}
				return null;
			}
			
		};
	}

}
