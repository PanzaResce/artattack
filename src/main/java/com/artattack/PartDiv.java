package com.artattack;

import java.io.File;

/**
 * Questa classe è utilizzata per dividere i file in N parti uguali
 * 
 * Per le operazioni di divisione e unione viene chiamata il metodo del padre 
 * {@link FileDiv#DivideFile DivideFile} o {@link FileDiv#MergeFile MergeFile} senza override
 * 
 * @author marco
 *
 */

public class PartDiv extends FileDiv{
	
	/**
	 * Il numero di part in cui il file verrà diviso, di default 2
	 */
	private int numParts = 2;
	
	/**
	 * 
	 * In base al numero di parti passate, si imposta il valore del buffer di divisine/unione
	 * 
	 * @param fname il nome del file
	 * @param numparts Il numero di parti in cui il file verrà diviso
	 * @param mode Impostato a true per la modalità divisione o false per la modalità unione
	 */
	public PartDiv(String fname, boolean mode, int numparts) {
		super(fname, mode);
		setNumParts(numparts);
		setEXT(".part");
		if (mode) 
			setBufferSize();	
	}
	
	
	/**
	 * Questo costruttore è usato in particolare per la modalità unione, infatti
	 * il numero di parti non è necessario
	 * 
	 * @param fname
	 * @param mode
	 */
	public PartDiv(String fname, boolean mode) {
		super(fname, mode);
		setEXT(".part");
		if (mode) 
			setBufferSize();	
	}
	
	/**
	 * Costruttore usato se le operazioni di criptaggio e compressioen sono richieste
	 * 
	 * @param fname
	 * @param mode
	 * @param crypt
	 * @param zip
	 * @param numparts
	 */
	public PartDiv(String fname, boolean mode, boolean crypt, boolean zip) {
		super(fname, mode, crypt, zip);
		setEXT(".part");
		if (mode) 
			setBufferSize();
	}
	
	/**
	 * 
	 * @param fname
	 * @param mode
	 * @param crypt
	 * @param zip
	 * @param numparts
	 */
	public PartDiv(String fname, boolean mode, boolean crypt, boolean zip, int numparts) {
		super(fname, mode, crypt, zip);
		setNumParts(numparts);
		setEXT(".part");
		if (mode) 
			setBufferSize();
	}
	
	
	
	/**
	 * Chiamata dal costruttore per impostare la corretta grandezza di ogni buffer in base a {@link ParteDiv#numParts numParts} 
	 */
	public void setBufferSize() {
		File f = new File(getFilename());
		//int d = (int) f.length() / getNumParts();
		this.BufferSize = (int) f.length() / getNumParts();
		long r = f.length() % getNumParts();
		this.BufferSize += r;
	}
	/**
	 * @return il numero di parti
	 */
	public int getNumParts() {
		return numParts;
	}

	/**
	 * @param numParts the numParts to set
	 */
	public void setNumParts(int numParts) {
		this.numParts = numParts;
	}
}
