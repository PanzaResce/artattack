package com.artattack;

import java.io.BufferedOutputStream;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
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
 * Questa è la classe padre che definisce il comportamento di una generale routine di divisione/unione.
 * <br>
 *  Di default questa classe divide il file in parti uguali da 4 Kb l'una, la grandezza per ogni parte può comunque essere specificata.
 * <br>
 * La proprietà {@link FileDiv#splitmode splitmode} determina se la classe verrà usata per le operazion di divisione o di unione, 
 * se impostata a valore {@code true} il file passato tramite il costruttre sarà diviso,
 * se impostata a {@code false} il file passato deve essere il primo generato dalla divisione (Ex : 
 * {@code file0.frame.txt} ) per poi poter eseguire l'unione.
 * <br>
 * 
 * I metodi che eseguono la divisione e l'unione sono rispettivamente {@link FileDiv#DivideFile()} e {@link FileDiv#MergeFile()}.
 * <br>
 * 
 * La classe implementa anche l'interfaccia {@code Runnable} in modo che si possa affidare la divisione/unione di ogni file ad un
 * singolo thread ({@link FileDiv#run}).
 * <br>
 * Questo tool utilizza la libreria Tink per criptare e decriptare i dati con la  <a href ="https://github.com/google/tink/blob/master/docs/JAVA-HOWTO.md">Symmetric Key Encryption</a>
 * @author marco
 *
 */
public class FileDiv implements Runnable{
	
	/**
	 * se impostata a {@code true} il file passato alla classe verrà criptato (se {@link FileDiv#splitmode splitmode} è {@code true})
	 * o decriptato ((se {@link FileDiv#splitmode splitmode} è {@code false})
	 */
	protected boolean encrypted;
	
	/**
	 * se impostata a {@code true} il file passato alla classe verrà compresso (se {@link FileDiv#splitmode splitmode} è {@code true})
	 * o decompresso ((se {@link FileDiv#splitmode splitmode} è {@code false})
	 */
	protected boolean zipped;
	
	/**
	 * splitmode determina come verrà utilizzata la calsse, se impostata a true il metodo {@link FileDiv#DivideFile DivideFile} può essere
	 * utilizzato, altrimenti si potrà utilizzare il metodo {@link FileDiv#MergeFile MergeFile} per l'unione
	 * 
	 */
	protected boolean splitmode;
	
	/**
	 * se {@link FileDiv#splitmode splitmode} è impostata a {@code true} , filename rappresenta il file da dividere,
	 * se {@code false} filename rapprenseta il primo file generato dal metodo {@link FileDiv#DivideFile DivideFile}
	 */
	protected String filename;
	
	/**
	 * Definisce la dimensione di ogni singolo file dopo la divisione
	 */
	protected int BufferSize = 4096;
	
	/**
	 * psw ha un valore se la proprietà {@link FileDiv#encrypted encrypted} è impostata a {@code true},
	 * rappresenta la password utilizzata per criptare i file 
	 */
	protected String psw = null;
	
	/**
	 * rappresenta la chiave generata dal metodo {@link FileDiv#DivideFile DivideFile} ed è salvata
	 * in chiaro su disco
	 * Implementazioni future prevedono lo spostamento della chiave in un remote key management systems
	 * 
	 */
	protected KeysetHandle key = null;
	
	/**
	 * impostata con la costante {@value FileDiv#CRYPT} se la proprietà {@link FileDiv#encrypted encrypted} è {@code true}
	 * il valore viene impostato nel metodo {@link FileDiv#setEncrypted setEncrypted}
	 */
	protected String CRYPTs = "";
	
	/**
	 * impostata con la costante {@value FileDiv#ZIP} se la proprietà {@link FileDiv#zipped zipped} è {@code true}
	 * il valore viene impostato nel metodo {@link FileDiv#setZipped setZipped}
	 */
	protected String ZIPs = "";
	
	/**
	 * l'estensione del file di output, rappresenta anche il tipo di operazione utilizzata, ovvero la classe utilizzata. Le classi figlie
	 * modificheranno questa proprietà per identificare il tipo di divisione che svolgeranno
	 */
	protected String EXT = ".frame";
	final String CRYPT = ".crypt";
	final String ZIP = ".zip";
	
	/**
	 * Il nome del file generato per l'archiviazione della chiave 
	 */
	final String keyArchive = "kst.json";
	
	/**
	 * Il {@code BufferSize} di default è 4096 (4Kb)
	 * 
	 * @param fname Il nome del file
	 * @param mode impostata a {@code true} per la modalità divisione, a {@code false} per unione
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
	
	/**
	 * 
	 * @param crypt impostata a {@code true} se si vogliono criptare i file
	 * @param zip impostata a {@code true} se si vogliono zippare i file
	 */
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
	 * Chiama {@link FileDiv#DivideFile DivideFile} o {@link FileDiv#MergeFile MergeFile} in base a {@link FileDiv#splitmode splitmode}
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(splitmode)
			DivideFile();
		else
			try {
				MergeFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * Divide il file in parti uguali in base al valore di {@link FileDiv#BufferSize BufferSize} .<br>
	 * Usa il metodo {@link FileDiv#readWrite readWrite} per scrivere. <br>
	 * Se il file è criptato o compresso verranno aggiunte le estensioni {@link FileDiv#CRYPT} o {@link FileDiv#ZIP} alla parte generata. <br>
	 * Le parti generate hanno il formato nomefile+indice.formatodivisione+estensionefile {@literal ---> } file0.frame.txt, gli indici partono da 0. <br>
	 * 
	 * @return Il numero di parti, -1 se l'operazione fallisce, 0 se {@link FileDiv#splitmode splitmode} è false
	 */
	//---REFACTOR---//
	public long DivideFile() {
		if(isSplitmode()) {
			if(isEncrypted()) {
				//System.out.println("Inserisci la psw per cifrare l'archivio");
				
				//setPsw(new String(System.console().readPassword()));
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
	 * Prende il primo file generato in un'altra istanza dal metodo {@link FileDiv#DivideFile DivideFile}
	 * e unisce tutte le parti in un unico file. <br>
	 * Le varie parti devono essere tutte nella stessa directory e si presuppone che siano nel formato (nomefile+indice.formatodivisione+estensionefile) {@literal --->} filename1.frame.txt <br> 
	 * Di default il metodo, dopo l'unione, non elemina le singole parti. <br>
	 * Se il metodo deve unire dei file compressi, prima li decomprime (file0.frame.txt.zip {@literal --->} file0.frame.txt) e poi li unisce tutti insieme, lo stesso vale per i file criptati <br>
	 * @return 1 se l'operazione è portata a termine, 0 altrimenti.
	 * @throws IOException L'eccezione è lanciata dal catch {@code FileNotFoundException} se il file passato al costruttore non esiste, 
	 * altrimenti vuol dire che l'eccezione catturata è dovuta al fatto che l'operazione di unione è stata terminata
	 */
	public int MergeFile() throws IOException {
		if(!isSplitmode()) {
			if(isEncrypted()) {
				//System.out.println("Inserisci la psw per poter leggere l'archivio");
				
				//setPsw(new String(System.console().readPassword()));
				//setPsw("ciaociao");
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
	 * Il metodo legge da {@code source} {@code numBytes} bytes e li scrive in {@code dest}
	 * <br> Se {@link FileDiv#encrypted encrypted} è true e 
	 * {@link FileDiv#splitmode splitmode} è true cripta i dati o decripta se {@link FileDiv#splitmode splitmode} è false
	 * <br>
	 * Di default il metodo non chiude la stream {@code source}
	 * 
	 * @param source Source stream, in questa implementazione è un RandomAccessFile di default
	 * @param dest Destination stream
	 * @param numBytes Il numero di byte letti e scritti
	 * @param encr Se true lo stream viene criptato
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
	 * Comprime il file passato alla funzione
	 * @param filename Il file da comprimere
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
	 * Decomprime il file {@code zippedFilename} e ritorna il nome del file 
	 * @param zippedFilename Il file da decomprimere
	 * @return Se la classe non ha {@link FileDiv#zipped zipped} a {@code true} ritorna il nome del file passato, altrimenti il nome del file decompresso
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
            
            zis.close();
            return outputFileName;
			
		}else {
			return zippedFilename;
		}
	}
	
	/**
	 * Estrae il noem dal file (rimuove l'estensione)
	 * Se la classe è usata in merge-mode, la funzione rimuove {@link FileDiv#EXT} dal nome del file
	 * @param filename {@link FileDiv#filename filename}
	 * @return Ex : {@code file1.txt ---> file1} o se {@link FileDiv#splitmode splitmode} = {@code false} 
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
	 * Estrae l'estensione dal nome del file
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
	 * Il metodo genera il nome della singola parte del file che si sta dividendo in base a {@link FileDiv#encrypted encrypted} 
	 * e all'index della parte<br>
	 * 
	 * La costante {@link FileDiv#EXT} definisce il tipo di divisione utilizzata
	 * 
	 * @param index L'indice della parte del file
	 * @return IL nome della parte del file
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
	 * Genera la key e la salva su file {@value FileDiv#keyArchive}
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
	 * Prende la key dal file {@value FileDiv#keyArchive}
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
	 * @return true se il file è criptato o da criptare
	 */
	public boolean isEncrypted() {
		return encrypted;
	}

	/**
	 * se {@code encrypted} è true imposta anche la proprietà {@link FileDiv#CRYPTs CRYPTs} a {@value FileDiv#CRYPT}
	 * e attiva il Tink register 
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
	 * @return true se il file è zippato o da zippare
	 */
	public boolean isZipped() {
		return zipped;
	}

	/**
	 * se {@code zipped} è {@code true} imposta la proprietà {@link FileDiv#ZIPs ZIPs} a {@value FileDiv#ZIP}
	 * @param zipped the zipped to set
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
		if(zipped) this.ZIPs = this.ZIP;
	}
	
	/**
	 * @return true se la classe è nella modalità divisione (split), false se unione (merge)
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
	 * @return il nome del file
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
	 * @return la dimensione del buffer per le operazioni di lettura e scrittura
	 */
	public int getBufferSize() {
		return BufferSize;
	}

	/**
	 * @param BufferSize di default è 4Kb
	 */
	public void setBufferSize(int BufferSize) {
		if(BufferSize != 0) 
			this.BufferSize = BufferSize;
	}

	/**
	 * @return la psw
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
	 * @return la key dell'archivio
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
	
	/**
	 * @return l'estensione del tipo di divisione utilizzata
	 */
	public String getEXT() {
		return EXT;
	}

	/**
	 * @param eXT the eXT to set
	 */
	public void setEXT(String eXT) {
		EXT = eXT;
	}
	
}
