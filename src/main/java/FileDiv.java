/**
 * 
 */
package main.java;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * This class define the behavior of a general file division routine
 * <br>
 *  By default this class divide the file in even parts of 4Kb each, otherwise the part size can be specified
 * <br>
 * 
 * @author marco
 *
 */
public class FileDiv {
	protected boolean encrypted;
	protected boolean zipped;
	protected String filename;
	protected int BufferSize = 4096;
	
	final String EXT = ".frame";
	
	/**
	 * The dafault {@code BufferSize} is 4096 (4Kb)
	 * 
	 * @param fname The file name
	 */
	public FileDiv(String fname) {
		setFilename(fname);
		setCrypted(false);
		setZipped(false);
	}
	
	public FileDiv(String fname, int buffersize) {
		setFilename(fname);
		setCrypted(false);
		setZipped(false);
		setBufferSize(buffersize);
	}
	
	public FileDiv(String fname, boolean crypt, boolean zip, int buffersize) {
		setFilename(fname);
		setCrypted(crypt);
		setZipped(zip);
		setBufferSize(buffersize);
	}
	
	/**
	 * Divide the file in even parts based on {@link main.java.FileDiv#BufferSize BufferSize}
	 * <br>
	 * Use the {@link main.java.FileDiv#readWrite readWrite} method to write
	 * 
	 * @return The number of parts or -1 if the operation fails
	 */
	public long DivideFile() {
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
	
	/**
	 * This method read from {@code source} {@code numBytes} bytes and write to {@code dest}
	 * 
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
	 * 
	 * This method generate the name of the part's file based on the {@link main.java.FileDiv#encrypted encrypted} and 
	 * {@link main.java.FileDiv#zipped zipped} values and the {@code index} of the part
	 * The constant {@value main.java.FileDiv#EXT} is used to define the used division routine
	 * 
	 * 
	 * @param index The index of the file part
	 * @return The file part name
	 */
	protected String generateOutputFilename(long index) {
		String filename = getFilename();
		
		String plainName = filename.substring(0,filename.lastIndexOf("."));
		String ext = filename.substring(filename.lastIndexOf("."), filename.length());
		
		if(isCrypted()) {
			return plainName+index+EXT+".crypt"+ext;
		}else if(isZipped()) {
			return plainName+index+EXT+".zip"+ext;
		}
		
		return plainName+index+EXT+ext;
	}
	

	/**
	 * @return true if the file is encrypted
	 */
	public boolean isCrypted() {
		return encrypted;
	}

	/**
	 * @param encrypted the encrypted to set
	 */
	public void setCrypted(boolean encrypted) {
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
