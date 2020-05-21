package com.artattack;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Class that handles FileDiv objects with a simple queue (ArrayList)
 * @author marco
 *
 */
public class DivisionHandler {
	
	private ArrayList<FileDiv> queue;
	
	private boolean completed = false;

	private String FileDivReg = "^(\\./)?.*\\.frame\\..*";
	private String DimDivReg = "^(\\./)?.*\\.dim\\..*";
	private String PartDivReg = "^(\\./)?.*\\.part\\..*";
	

	private String ZipReg = ".*(?:\\.frame\\.|\\.dim\\.|\\.part\\.)(crypt\\.)?.*\\.zip$";
	private String CryptReg = ".*(?:\\.frame\\.|\\.dim\\.|\\.part\\.)crypt\\..*";
	
	public DivisionHandler() {
		this.queue = new ArrayList<FileDiv>();
	}
	
	/**
	 * Add the file to the queue
	 * @param f
	 */
	public void addFile(FileDiv f) {
		queue.add(f);
	}
	
	/**
	 * Add the file to the queue based on the file name pattern
	 * @param f
	 */
	public void addFile(String f, boolean splitmode) {
		boolean filediv = Pattern.matches(FileDivReg, f);		
		boolean dimdiv = Pattern.matches(DimDivReg, f);
		boolean partdiv = Pattern.matches(PartDivReg, f);
		
		boolean zip = Pattern.matches(ZipReg, f);
		boolean crypt = Pattern.matches(CryptReg, f);
		
		if(filediv)
			queue.add(new FileDiv(f, splitmode, crypt, zip, 4096));
			
		else if(dimdiv)
			queue.add(new DimDiv(f, splitmode, crypt, zip)); 
			
		else if(partdiv)
			queue.add(new PartDiv(f, splitmode, crypt, zip, 4)); 
	}
	
	/**
	 * Remove the file from the queue
	 * @param index
	 */
	public void removeFile(int index) {
		queue.remove(index);
	}
	
	/**
	 * Get the file by giving the index in the queue
	 * @param index
	 */
	public FileDiv getFile(int index) {
		return queue.get(index);
	}
	
	/**
	 * Call the {@link FileDiv#run Run} method for each object in the queue
	 */
	public synchronized void execute() {
		ArrayList threads = new ArrayList();

		for(int i=0; i < queue.size(); i++) {
			FileDiv e = queue.get(i);
			Thread t = new Thread(e);
			t.start();
			threads.add(t);
			//long parts = e.DivideFile();
			//System.out.println("FINITO: " + e.getFilename() + " diviso in " + parts + " parti");
		}
		
		for (int i=0; i < threads.size(); i++)
			try {
				((Thread) threads.get(i)).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public synchronized void clear() {
		queue.clear();
	}
	
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
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
}
