package com.artattack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * In questa classe si utilizza un arraylist, che deve essere passato al costruttore o impostato in seguito,
 * per la gestione della divisione, inoltre viene sovrascritto il metodo {@link FileDiv#DivideFile DivideFile} 
 * @author marco
 *
 */
public class DimDiv extends FileDiv{
	
	/**
	 * L'arraylist è utilizzato per memorizzare la grandezza di ogni parte, esso deve essere gestito
	 * dall'utilizzatore della classe <br>
	 * L'ordine in cui vengono inserite le grandezze rappresenta le dimensioni delle parti che verranno generate <br>.
	 * Di default divide il file in 2 parti uguali
	 */
	public ArrayList<Long> division = new ArrayList<Long>();
	
	/**
 	 * 
	 * @param fname il nome del file 
	 * @param mode true se in modalità divisione, false se in modalità unione
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
	 * Divide il file basandosi sull'array {@link DimDiv#division division}, se vuoto lo divide in due parti uguali
	 * 
	 * @return il numero di parti se l'operazione ha successo, -1 se l'operazione fallisce, 0 se splitmode è false
	 */
	public long DivideFile() {
		if(isSplitmode()) {
			if(isEncrypted()) {
				//System.out.println("Inserisci la psw per cifrare l'archivio");
				
				//setPsw(new String(System.console().readPassword()));
				setKey(generateKey());
			}
			
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
	 * Funzione per impostare l'arraylist di default, due parti uguali
	 */
	private void defaultDivision() {
		File f = new File(getFilename());
		long flength = f.length();
		
		division.add(flength / 2);
		division.add(flength - (flength / 2));
	}
	
	/**
	 * @return l'array usato per la divisione
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

}
