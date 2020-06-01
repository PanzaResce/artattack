package com.artattack;

import java.io.File;

/**
 * This class is used to divide the file in a specified number of parts 
 * 
 * For the split and merge functions the {@link FileDiv#DivideFile DivideFile} and {@link FileDiv#MergeFile MergeFile}
 * are used without overriding
 * 
 * @author marco
 *
 */

public class PartDiv extends FileDiv{
	
	/**
	 * The number of parts in which the file will be split
	 */
	private int numParts = 2;
	
	/**
	 * The buffer size needs to be specified for the split function, it is not needed 
	 * for the merge function
	 * 
	 * @param fname
	 * @param numparts The number of parts in which the file will be divided
	 * @param mode Set to true for split-mode or false for merge-mode
	 */
	public PartDiv(String fname, boolean mode, int numparts) {
		super(fname, mode);
		setNumParts(numparts);
		setEXT(".part");
		if (mode) 
			setBufferSize();	
	}
	
	
	/**
	 * This constructor is specified used when the class runs in merge mode, 
	 * in this case the number of parts is not necessary
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
	 * Use this constructor if crypt or zip functions are needed
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
	 * Use this constructor if crypt or zip functions are needed
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
	 * The buffer size needs to be specified for the split function 
	 */
	public void setBufferSize() {
		File f = new File(getFilename());
		//int d = (int) f.length() / getNumParts();
		this.BufferSize = (int) f.length() / getNumParts();
		long r = f.length() % getNumParts();
		this.BufferSize += r;
	}
	/**
	 * @return the numParts
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
