/**
 * 
 */
package com.artattack;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * This class define the behavior of a general file division routine
 * <br>
 *  By default this class divide the file in even parts of 4Kb each, otherwise the part size can be specified
 * <br>
 * The {@link FileDiv#splitmode splitmode} property determine the usage of the class, 
 * if set to true the file passed to the constructor is the one who will be split, 
 * if set to false the file will be the first generated from the split routine (Ex : 
 * {@code file0.frame.txt} )
 * 
 * @author marco
 *
 */
public class FileDiv {
	protected boolean encrypted;
	protected boolean zipped;
	/**
	 * splitmode determine the usage of the class, if set to true the {@link FileDiv#DivideFile DivideFile} 
	 * method can be triggered otherwise the {@link FileDiv#MergeFile MergeFile} method can be triggered
	 * 
	 */
	protected boolean splitmode;
	/**
	 * if {@link FileDiv#splitmode splitmode} is true it represents the file to be split, 
	 * if false it is the first file generated from the {@link FileDiv#DivideFile DivideFile} operation
	 */
	protected String filename;
	protected int BufferSize = 4096;
	
	final String EXT = ".frame";
	final String CRYPT = ".crypt";
	final String ZIP = ".zip";
	
	/**
	 * The default {@code BufferSize} is 4096 (4Kb)
	 * 
	 * @param fname The file name
	 * @param mode Set to true for split-mode or false for merge-mode
	 */
	public FileDiv(String fname, boolean mode) {
		setFilename(fname);
		setSplitmode(mode);
		setEncrypted(false);
		setZipped(false);
	}
	
	public FileDiv(String fname, boolean mode, int buffersize) {
		setFilename(fname);
		setSplitmode(mode);
		setEncrypted(false);
		setZipped(false);
		setBufferSize(buffersize);
	}
	
	public FileDiv(String fname, boolean mode, boolean crypt, boolean zip, int buffersize) {
		setFilename(fname);
		setSplitmode(mode);
		setEncrypted(crypt);
		setZipped(zip);
		setBufferSize(buffersize);
	}
	
	/**
	 * Divide the file in even parts based on {@link FileDiv#BufferSize BufferSize}
	 * <br>
	 * Use the {@link FileDiv#readWrite readWrite} method to write
	 * 
	 * @return The number of parts, -1 if the operation fails 0 if the splitmode is false
	 */
	public long DivideFile() {
		if(isSplitmode()) {
			try {
				RandomAccessFile raf = new RandomAccessFile(getFilename(), "r");
		        long sourceSize = raf.length();
		        long bytesPerSplit = getBufferSize() ;
		        long numSplits = sourceSize / bytesPerSplit;
		        long remainingBytes = sourceSize - (bytesPerSplit * numSplits);
		
		        for(int destIx=0; destIx < numSplits; destIx++) {
		            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(generateOutputFilename(destIx)));
	                readWrite(raf, bw, bytesPerSplit);            
		            bw.close();
		        }
		        if(remainingBytes > 0) {
		            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(generateOutputFilename(numSplits)));
		            readWrite(raf, bw, remainingBytes);
		            bw.close();
		        }
	            raf.close();
	            return (remainingBytes > 0) ? numSplits+1 : numSplits;
	            
			}catch(IOException e) {
				System.out.println(e.getStackTrace());
				return -1;
			}
		}
		return 0;
		
	}
	
	/**
	 * Take the first file generated from the previous {@link FileDiv#DivideFile DivideFile} operation
	 * and merge all the parts <br>
	 * By default this method doesn't delete the parts
	 * @return 1 if the operation worked, 0 otherwise
	 * @throws IOException The exception is thrown by the {@code FileNotFoundException} catch if the file passed to 
	 * the constructor does not exists, otherwise the {@code FileNotFoundException} return 1 to the function
	 */
	public int MergeFile() throws IOException {
		if(!isSplitmode()) {
			int index=0;
			BufferedOutputStream bw = null;
			
			String filename = getFilename();
			String plainName = getFilePlainName(filename).substring(0, getFilePlainName(filename).length()-1);
			String ext = getFileExt(filename);
			String outputFilename = plainName+ext;
			
			try {	
	            bw = new BufferedOutputStream(new FileOutputStream(outputFilename));
	            
	          
	            while(true) {
	            	RandomAccessFile raf = new RandomAccessFile(plainName+index+EXT+ext, "r");
	            	readWrite(raf, bw, raf.length());
	            	index++;
	            	raf.close();
	            }
	            
				//System.out.print(plainName+0+EXT+ext);
	
			}catch(FileNotFoundException e) {
				if(index!=0) {
					bw.close();
					return 1;
				}else {
					throw new IOException("File "+filename+" not found, choose an existing file");
				}
			}catch(IOException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * This method read from {@code source} {@code numBytes} bytes and write to {@code dest}
	 * 
	 * @param source Source stream, in this class is a RandomAccessFile by default
	 * @param dest Destination stream
	 * @param numBytes The number of file read and written
	 * @throws IOException Not handled
	 * @see <a href ="https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html#read(byte[])">RandomAccesFile.read()</a>
	 */
	protected void readWrite(RandomAccessFile source, BufferedOutputStream dest, long numBytes) throws IOException {
	    byte[] buf = new byte[(int) numBytes];
	    int val = source.read(buf);	//read buf.length bytes (numBytes) from file
	    //System.out.print(val + ", ");
	    if(val != -1) {
	        dest.write(buf);
	    }
	}
	
	/**
	 * Extract the name of the file from the full name, if this is used in merge-mode 
	 * remove the {@value FileDiv#EXT} from the file name
	 * @param filename {@link FileDiv#filename filename}
	 * @return Ex : {@code file1.txt ---> file1} or if {@link FileDiv#splitmode splitmode} = false 
	 * {@code file0.frame.txt ---> file0}
	 */
	protected String getFilePlainName(String filename) {
		if(filename.contains(EXT)) {
			String app = filename.substring(0,filename.lastIndexOf("."));
			return app.substring(0, app.lastIndexOf("."));
		}else {
			return filename.substring(0,filename.lastIndexOf("."));
		}
	}
	
	/**
	 * Extract the extension from the file name
	 * @param filename {@link FileDiv#filename filename}
	 * @return Ex : {@code file1.txt ----> .txt}
	 */
	protected String getFileExt(String filename) {
		return filename.substring(filename.lastIndexOf("."), filename.length());
	}
	
	/**
	 * This method generate the name of the part's file based on the {@link FileDiv#encrypted encrypted} and 
	 * {@link FileDiv#zipped zipped} values and the {@code index} of the part<br>
	 * The constant {@value FileDiv#EXT} is used to define the used division routine
	 * 
	 * 
	 * @param index The index of the file part
	 * @return The file part name
	 */
	protected String generateOutputFilename(long index) {
		String filename = getFilename();
		
		String plainName = getFilePlainName(filename);
		String ext = getFileExt(filename);
		
		if(isEncrypted()) {
			return plainName+index+EXT+CRYPT+ext;
		}else if(isZipped()) {
			return plainName+index+EXT+ZIP+ext;
		}
		
		return plainName+index+EXT+ext;
	}	

	/**
	 * @return true if the file is encrypted
	 */
	public boolean isEncrypted() {
		return encrypted;
	}

	/**
	 * @param encrypted the encrypted to set
	 */
	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * @return true if the file is zipped
	 */
	public boolean isZipped() {
		return zipped;
	}

	/**
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}
	
	/**
	 * @return the splitmode
	 */
	public boolean isSplitmode() {
		return splitmode;
	}

	/**
	 * @param splitmode the splitmode to set
	 */
	public void setSplitmode(boolean splitmode) {
		this.splitmode = splitmode;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the BufferSize for read and write operations
	 */
	public int getBufferSize() {
		return BufferSize;
	}

	/**
	 * @param BufferSize the default BufferSize is 4Kb
	 */
	public void setBufferSize(int BufferSize) {
		this.BufferSize = BufferSize;
	}
	
}
