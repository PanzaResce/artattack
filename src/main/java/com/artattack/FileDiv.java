package com.artattack;

import java.io.BufferedOutputStream;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.proto.Keyset;
import com.google.crypto.tink.JsonKeysetWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.GeneralSecurityException;
import java.util.zip.*;

/**
 * 
 * This class define the behavior of a general file division routine
 * <br>
 *  By default this class divide the file in even parts of 4Kb each, otherwise the part size can be specified
 * <br>
 * The {@link FileDiv#splitmode splitmode} property determine the usage of the class, 
 * if set to true the file passed to the constructor is the one who will be split, 
 * if set to false the file passed need to be the first generated from the split routine (Ex : 
 * {@code file0.frame.txt} )
 * <br>
 * 
 * This tool use the Tink library to encrypt and decrypt data with the <a href ="https://github.com/google/tink/blob/master/docs/JAVA-HOWTO.md">Symmetric Key Encryption</a>
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
	
	/**
	 * psw is set to a specific data if the {@link FileDiv#encrypted encrypted} property is true, 
	 * it represents the password given to the archive by the user 
	 */
	protected String psw = null;
	/**
	 * it represents the key generated in the {@link FileDiv#DivideFile DivideFile} method and 
	 * is saved in a cleartext keysets on disk. <br>
	 * Further implementation should change the storage system to a remote key management systems
	 * 
	 */
	protected KeysetHandle key = null;
	
	/**
	 * set to the value of the constant {@value FileDiv#CRYPT} if {@link FileDiv#encrypted encrypted} property is true
	 * the value is set in the {@link FileDiv#setEncrypted setEncrypted} method
	 */
	protected String CRYPTs = "";
	/**
	 * set to the value of the constant {@value FileDiv#ZIP} if {@link FileDiv#zipped zipped} property is true
	 * the value is set in the {@link FileDiv#setZipped setZipped} method
	 */
	protected String ZIPs = "";
	
	final String EXT = ".frame";
	final String CRYPT = ".crypt";
	final String ZIP = ".zip";
	
	final String keyArchive = "kst.json";
	
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
	
	public FileDiv(String fname, boolean mode, boolean crypt, boolean zip) {
		setFilename(fname);
		setSplitmode(mode);
		setEncrypted(crypt);
		setZipped(zip);
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
	//---REFACTOR---//
	public long DivideFile() {
		if(isSplitmode()) {
			if(isEncrypted()) {
				System.out.println("Inserisci la psw per cifrare l'archivio");
				
				setPsw(new String(System.console().readPassword()));
				setKey(generateKey());
			}
			
			try {
				RandomAccessFile raf = new RandomAccessFile(getFilename(), "r");
		        long sourceSize = raf.length();
		        long bytesPerSplit = getBufferSize() ;
		        long numSplits = sourceSize / bytesPerSplit;
		        long remainingBytes = sourceSize - (bytesPerSplit * numSplits);
		
		        for(int destIx=0; destIx < numSplits; destIx++) {
		            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(generateOutputFilename(destIx)));
	                readWrite(raf, bw, bytesPerSplit, isEncrypted());            
		            bw.close();
		            if(isZipped()) zipFile(generateOutputFilename(destIx));
		            
		        }
		        if(remainingBytes > 0) {
		            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(generateOutputFilename(numSplits)));
		            readWrite(raf, bw, remainingBytes, isEncrypted());
		            bw.close();
		            if(isZipped()) zipFile(generateOutputFilename(numSplits));
		        }
	            raf.close();
	            return (remainingBytes > 0) ? numSplits+1 : numSplits;
	            
			}catch(IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
		
	}
	
	/**
	 * Take the first file generated from the previous {@link FileDiv#DivideFile DivideFile} operation
	 * and merge all the parts <br>
	 * By default this method doesn't delete the parts
	 * If the method need to merge zipped files it firstly unzip the single archives (file0.frame.txt.zip ---> file0.frame.txt) 
	 * and then merge them all together 
	 * @return 1 if the operation worked, 0 otherwise
	 * @throws IOException The exception is thrown by the {@code FileNotFoundException} catch if the file passed to 
	 * the constructor does not exists, otherwise the {@code FileNotFoundException} return 1 to the function
	 */
	public int MergeFile() throws IOException {
		if(!isSplitmode()) {
			if(isEncrypted()) {
				System.out.println("Inserisci la psw per poter leggere l'archivio");
				
				setPsw(new String(System.console().readPassword()));
				setKey(getKeyFromArchive());
			}
			
			int index=0;
			BufferedOutputStream bw = null;
			
			String filename = getFilename();
			String plainName = getFilePlainName(filename).substring(0, getFilePlainName(filename).length()-1);
			String ext = getFileExt(filename);
			String outputFilename = plainName+ext;
			
			try {	
	            bw = new BufferedOutputStream(new FileOutputStream(outputFilename));
	            
	            while(true) {
	            	//String app = plainName+index+EXT+CRYPTs+ext+ZIPs;
	            	RandomAccessFile raf = new RandomAccessFile(unzipFile(plainName+index+EXT+CRYPTs+ext+ZIPs), "r");
	            	readWrite(raf, bw, raf.length(), isEncrypted());
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
	 * <br> if {@link FileDiv#encrypted encrypted} is set to true it encrypts data if 
	 * {@link FileDiv#splitmode splitmode} is true or decrypt if {@link FileDiv#splitmode splitmode} is false
	 * <br>
	 * by deafult this method does not close the streams
	 * 
	 * @param source Source stream, in this class is a RandomAccessFile by default
	 * @param dest Destination stream
	 * @param numBytes The number of file read and written
	 * @param encr If true the stream has to be encrypted
	 * @throws IOException Not handled
	 * @see <a href ="https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html#read(byte[])">RandomAccesFile.read()</a>
	 */
	protected void readWrite(RandomAccessFile source, BufferedOutputStream dest, long numBytes, boolean encr) throws IOException {
	    byte[] buf = new byte[(int) numBytes];
	    
	    int val = source.read(buf);
	    
		if(val != -1) {
			if(encr) {
				try {
					Aead aead = getKey().getPrimitive(Aead.class);
					if(isSplitmode()) {
						buf = aead.encrypt(buf, getPsw().getBytes());
					}else {
						buf = aead.decrypt(buf, getPsw().getBytes());
					}
					
				}catch(GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
			dest.write(buf);
		}
	  
	}
	
	/**
	 * Create the compressed file from the one passed to the function
	 * @param filename
	 * @throws IOException
	 */
	protected void zipFile(String filename) throws IOException{
		FileInputStream fis = new FileInputStream(filename);
		
		ZipEntry e = new ZipEntry(filename);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filename+ZIP));
		out.putNextEntry(e);
		
		byte[] data = new byte[(int) new File(filename).length()];
		fis.read(data);
		out.write(data, 0, data.length);
		out.closeEntry();

		out.close();
		fis.close();
	}
	
	/**
	 * Unzip from {@code zippedFilename} and then return the name of the unzipped file, 
	 * if not in zip mode return the zippedFilename as passed
	 * @param zippedFilename
	 * @throws IOException
	 */
	protected String unzipFile(String zippedFilename) throws IOException{
		if(isZipped()) {
			
	        ZipInputStream zis = new ZipInputStream(new FileInputStream(zippedFilename));
	        ZipEntry zipEntry = zis.getNextEntry();
	        
	        String outputFileName = zipEntry.getName();
	        byte[] buffer = new byte[1024];
	        
            while (zipEntry != null) {
                FileOutputStream fos = new FileOutputStream(outputFileName);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
			
            return outputFileName;
			
		}else {
			return zippedFilename;
		}
	}
	
	/**
	 * Extract the name of the file from the full name, if this is used in merge-mode 
	 * remove the {@value FileDiv#EXT} from the file name
	 * @param filename {@link FileDiv#filename filename}
	 * @return Ex : {@code file1.txt ---> file1} or if {@link FileDiv#splitmode splitmode} = {@code false} 
	 * {@code file0.frame.txt ---> file0}
	 */
	//---REFACTOR---//
	protected String getFilePlainName(String filename) {
		if(filename.contains(EXT)) {
			String app = filename.substring(0,filename.lastIndexOf("."));
			if(isEncrypted() || isZipped()) {
				app = app.substring(0, app.lastIndexOf("."));
				return app.substring(0, app.lastIndexOf("."));
			}else {
				return app.substring(0, app.lastIndexOf("."));
			}
		}else {
			return filename.substring(0,filename.lastIndexOf("."));
		}
	}
	
	/**
	 * Extract the extension from the file name
	 * @param filename {@link FileDiv#filename filename}
	 * @return Ex : {@code file1.txt ----> .txt		file1.txt.zip ----> .txt}
	 */
	protected String getFileExt(String filename) {
		if (isZipped() && !isSplitmode()) {
			String app = filename.substring(0, filename.lastIndexOf("."));
			return app.substring(app.lastIndexOf("."), app.length());
		}
		return filename.substring(filename.lastIndexOf("."), filename.length());
	}
	
	/**
	 * This method generate the name of the part's file based on the {@link FileDiv#encrypted encrypted} value
	 * and the {@code index} of the part<br>
	 * 
	 * The constant {@value FileDiv#EXT} define the used division routine
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
		}
		
		return plainName+index+EXT+ext;
	}	
	
	/**
	 * Generate the key and save it to file {@value FileDiv#keyArchive}
	 * 
	 * 
	 * 
	 * @return the KeySetHandle object
	 */
	protected KeysetHandle generateKey() {
		try {
			KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM);
		    CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(new File(this.keyArchive)));

			return keysetHandle;
		}catch(GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the key from file {@value FileDiv#keyArchive}
	 * @return the KeysetHandle object 
	 */
	protected KeysetHandle getKeyFromArchive() {
		try {
		    return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(this.keyArchive)));
		}catch(GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return true if the file is encrypted
	 */
	public boolean isEncrypted() {
		return encrypted;
	}

	/**
	 * if {@code encrypted} is true it also set the {@link FileDiv#CRYPTs CRYPTs} property to {@value FileDiv#CRYPT}
	 * and trigger the tink register 
	 * @param encrypted the encrypted to set
	 */
	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
		if (encrypted) {
			try{
				AeadConfig.register();
			}catch(GeneralSecurityException e) {
				e.printStackTrace();
			}
			this.CRYPTs = this.CRYPT;
		}
		
	}

	/**
	 * @return true if the file is zipped
	 */
	public boolean isZipped() {
		return zipped;
	}

	/**
	 * if {@code zipped} is true it also set the {@link FileDiv#ZIPs ZIPs} property to {@value FileDiv#ZIP}
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
		if(zipped) this.ZIPs = this.ZIP;
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

	/**
	 * @return the psw
	 */
	public String getPsw() {
		return psw;
	}

	/**
	 * @param psw the psw to set
	 */
	public void setPsw(String psw) {
		this.psw = psw;
	}

	/**
	 * @return the key
	 */
	public KeysetHandle getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(KeysetHandle key) {
		this.key = key;
	}
	
	
}
