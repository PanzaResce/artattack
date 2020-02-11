package com.artattack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class DimDiv extends FileDiv{
	
	public DimDiv(String fname, boolean mode) {
		super(fname, mode);
		setEXT(".dim");
	}
	
	
	public DimDiv(String fname, boolean mode, boolean crypt, boolean zip) {
		super(fname, mode, crypt, zip);
		setEXT(".dim");
	}
	
	
	/**
	 * Ask the user the dimension of each file part, the dimension is asked in Kb
	 * 
	 * @return the number of parts, -1 if the operation fails 0 if the splitmode is false
	 */
	public long DivideFile() {
		if(isSplitmode()) {
			if(isEncrypted()) {
				System.out.println("Inserisci la psw per cifrare l'archivio");
				
				setPsw(new String(System.console().readPassword()));
				setKey(generateKey());
			}
				
			File f = new File(getFilename());
			long flength = f.length();
			ArrayList<Long> division = new ArrayList<Long>();
			long remains = flength;
			long part = 0;
			
			System.out.println("Grandezza File: " + humanReadableByteCountBin(flength));
			
			do {
				System.out.println("Specificare grandezza parte in Kb ("+ remains + " Kb rimanenti)");
				
				part = Long.parseLong(System.console().readLine()) * 1024;
				
				if (part > remains)
					part = remains;
				
				remains -= part;
				
				division.add(part);
				
			}while(remains > 0);
			
			try {	
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
