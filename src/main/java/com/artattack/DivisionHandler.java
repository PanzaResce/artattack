package com.artattack;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * La classe che gestisce i tipi di classe {@link FileDiv} con una coda (ArrayList). <br>
 * 
 * Questa classe non distingue tra divisione e unione, poichè chiamando il metodo {@link FileDiv#run run} dell classe {@link FileDiv} 
 * viene scelta in automatico l'operazione corretta.
 * 
 * @author marco
 *
 */
public class DivisionHandler{
	
	/**
	 * La coda che contiene gli oggetti {@link FileDiv}
	 */
	private ArrayList<FileDiv> queue;
	
	/**
	 * Used in the {@link DivisionHandler#execute execute} function to count how many files have been completed
	 */
	private int currCompletedElements = 0;
	
	private String FileDivReg = "^(\\./)?.*\\.frame\\..*";
	private String DimDivReg = "^(\\./)?.*\\.dim\\..*";
	private String PartDivReg = "^(\\./)?.*\\.part\\..*";

	private String ZipReg = ".*(?:\\.frame\\.|\\.dim\\.|\\.part\\.)(crypt\\.)?.*\\.zip$";
	private String CryptReg = ".*(?:\\.frame\\.|\\.dim\\.|\\.part\\.)crypt\\..*";
	
	
	/**
	 * Il costruttore semplicemente istanzia la coda {@link DimDiv#division} 
	 */
	public DivisionHandler() {
		this.queue = new ArrayList<FileDiv>();
	}
	
	/**
	 * Aggiunge il file alla coda
	 * @param f l'oggetto {@link FileDiv#FileDiv FileDiv}
	 */
	public void addFile(FileDiv f) {
		queue.add(f);
	}
	
	/**
	 * Aggiunge il file alla coda in base al pattern del file, questa funzione viene utilizzata solo per l'unione, la funzione analizza
	 * il nome del file per capire se è criptato o zippato
	 * @param f	il nome del file da analizzare
	 * @param splitmode se true la funzione non esegue nessun'azione
	 */
	public void addFile(String f, boolean splitmode) {
		if(!splitmode) {
			boolean filediv = Pattern.matches(FileDivReg, f);		
			boolean dimdiv = Pattern.matches(DimDivReg, f);
			boolean partdiv = Pattern.matches(PartDivReg, f);
			
			boolean zip = Pattern.matches(ZipReg, f);
			boolean crypt = Pattern.matches(CryptReg, f);
			
			if(filediv)
				queue.add(new FileDiv(f, splitmode, crypt, zip));
				
			else if(dimdiv)
				queue.add(new DimDiv(f, splitmode, crypt, zip)); 
				
			else if(partdiv)
				queue.add(new PartDiv(f, splitmode, crypt, zip)); 
		}
	}
	
	/**
	 * Rimuove il file dalla coda
	 * @param index l'indice dell'elemento da eliminare
	 */
	public void removeFile(int index) {
		queue.remove(index);
	}
	
	/**
	 * Restituisce l'oggeto in base all'indice nella coda
	 * @param index l'indice dell'elemento che si vuole ottenere
	 * @return l'oggetto richiesto
	 */
	public FileDiv getFile(int index) {
		return queue.get(index);
	}
	
	/**
	 * Chiama il metodo {@link FileDiv#run Run} e crea un thread per ogni oggetto nella coda
	 */
	public void execute() {
		//ArrayList threads = new ArrayList();

		for(int i=0; i < queue.size(); i++) {
			FileDiv e = queue.get(i);
			Thread t = new Thread(e);
			t.start();
			//threads.add(t);
			//long parts = e.DivideFile();
			//System.out.println("FINITO: " + e.getFilename() + " diviso in " + parts + " parti");
		}
		
		/*
		for (int i=0; i < threads.size(); i++) {
			try {
				((Thread) threads.get(i)).join();
				this.currCompletedElements++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		this.clear();
		
	}
	
	/**
	 * Rimuove tutti gli oggetti dalla coda
	 */
	public void clear() {
		queue.clear();
	}
	
	/**
	 * 
	 * @return the number of file in the queue
	 */
	public int getLength() {
		return queue.size();
	}
	
	//REFACTOR//
	public String toString() {
		String out = "";
		
		for(int i = 0; i < queue.size(); i++) {
			FileDiv element = getFile(i);
			out += element.getFilename() + "\n";
		}
		
		return out;
	}

	/**
	 * @return the currCompletedElements
	 */
	public int getCurrCompletedElements() {
		return currCompletedElements;
	}

	/**
	 * @param currCompletedElements the currCompletedElements to set
	 */
	public void setCurrCompletedElements(int currCompletedElements) {
		this.currCompletedElements = currCompletedElements;
	}
	
}
