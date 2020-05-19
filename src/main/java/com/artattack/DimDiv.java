package com.artattack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class DimDiv extends FileDiv{
	
	/**
	 * This arraylist is used to store the size of each file's part, this needs to be handled 
	 * by the user of the class <br>
	 * By default it split the file in two equal parts
	 */
	public ArrayList<Long> division = new ArrayList<Long>();
	
	/**
	 * The constructor simply call the father's one 
	 * @param fname
	 * @param mode
	 */
	public DimDiv(String fname, boolean mode) {
		super(fname, mode);
		defaultDivision();
		setEXT(".dim");
	}
	
	
	public DimDiv(String fname, boolean mode, boolean crypt, boolean zip) {
		super(fname, mode, crypt, zip);
		defaultDivision();
		setEXT(".dim");
	}
	
	public DimDiv(String fname, boolean mode, boolean crypt, boolean zip, ArrayList<Long> division) {
		super(fname, mode, crypt, zip);
		setDivision(division);
		setEXT(".dim");
	}


	/**
	 * Ask the user the dimension of each file part, the dimension is asked in Kb
	 * <br>
	 * It divides the file in two equal parts if {@link DimDiv#division division} array is empty
	 * 
	 * @return the number of parts, -1 if the operation fails 0 if the splitmode is false
	 */
	public long DivideFile() {
		if(isSplitmode()) {
			if(isEncrypted()) {
				//System.out.println("Inserisci la psw per cifrare l'archivio");
				
				//setPsw(new String(System.console().readPassword()));
				setKey(generateKey());
			}
			
			/*
			File f = new File(getFilename());
			long flength = f.length();
			ArrayList<Long> division = new ArrayList<Long>();
			long remains = flength;
			long part = 0;
			
			System.out.println("Grandezza File: " + humanReadableByteCountBin(flength));
			
			do {
				System.out.println("Specificare grandezza parte in Kb ("+ humanReadableByteCountBin(remains) + " rimanenti)");
				
				part = Long.parseLong(System.console().readLine()) * 1024;
				
				if (part > remains)
					part = remains;
				
				remains -= part;
				
				division.add(part);
				
			}while(remains > 0);
			*/
			//REFACTOR//
			try {	
				//set to default split method if empty
				if(division.isEmpty())
					defaultDivision();
				RandomAccessFile raf = new RandomAccessFile(getFilename(), "r");
				
				for(int i = 0; i < division.size(); i++) {
					BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(generateOutputFilename(i)));
	                readWrite(raf, bw, division.get(i), isEncrypted());            
		            bw.close();
		            if(isZipped()) zipFile(generateOutputFilename(i));
				}
				
				return division.size();
					
			}catch(IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 0;
	}
	
	/**
	 * Function for default division method, split the file in two equal parts
	 */
	private void defaultDivision() {
		File f = new File(getFilename());
		long flength = f.length();
		
		division.add(flength / 2);
		division.add(flength - (flength / 2));
	}
	
	/**
	 * @return the division
	 */
	public ArrayList<Long> getDivision() {
		return division;
	}


	/**
	 * @param division the division to set
	 */
	public void setDivision(ArrayList<Long> division) {
		this.division = division;
	}
	
	private static String humanReadableByteCountBin(long bytes) {
	    long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
	    return b < 1024L ? bytes + " B"
	            : b <= 0xfffccccccccccccL >> 40 ? String.format("%.1f KiB", bytes / 0x1p10)
	            : b <= 0xfffccccccccccccL >> 30 ? String.format("%.1f MiB", bytes / 0x1p20)
	            : b <= 0xfffccccccccccccL >> 20 ? String.format("%.1f GiB", bytes / 0x1p30)
	            : b <= 0xfffccccccccccccL >> 10 ? String.format("%.1f TiB", bytes / 0x1p40)
	            : b <= 0xfffccccccccccccL ? String.format("%.1f PiB", (bytes >> 10) / 0x1p40)
	            : String.format("%.1f EiB", (bytes >> 20) / 0x1p40);
	}
}
