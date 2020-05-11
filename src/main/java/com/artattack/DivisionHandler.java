package com.artattack;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that handles FileDiv objects with a simple queue (ArrayList)
 * @author marco
 *
 */
public class DivisionHandler {
	
	private ArrayList<FileDiv> queue;
	
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
	 * Call the {@link FileDiv#FileDiv FileDiv} method for each object in the queue
	 */
	public void split() {
		for(int i=0; i < queue.size(); i++) {
			FileDiv e = queue.get(i);
			long parts = e.DivideFile();
			System.out.println("FINITO: " + e.getFilename() + " diviso in " + parts + " parti");
		}
	}
	/**
	 * Call the {@link FileDiv#MergeFile MergeFile} method for each object in the queue
	 * @throws IOException
	 */
	public void merge() throws IOException{
		for(int i=0; i < queue.size(); i++) {
			queue.get(i).MergeFile();
		}
		
	}
	
	public void clear() {
		queue.clear();
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

}
