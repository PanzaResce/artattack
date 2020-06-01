package com.artattack;

import java.awt.Color;

import GUI.AppFrame;

/**
*
* File Splitter
* 
* 
* <p>Il programma presenta due pannelli dove rispettivamente si possono unire e dividere dei file</p>
* <p>Sono presenti tre tipi di divisioni</p><br>
* <ul>
* 	<li>Di default si divide il file in parti uguali in base alla grandezza specificata per ogni parte</li>
* 	<li>Divide il file in un numero specificabile di parti </li>
* 	<li>Divide il file impostando singolarmente la grandezza di ogni parte </li>
* </ul>
* 
* 
* 
* 
* @author  Marco Panarelli
* @version 1.0
* @since   2019 
*/

public class App{

	public static void main(String[] args) {
		
		AppFrame a = new AppFrame("coso", new Color(242,242,242));
		a.setVisible(true);	
  
	    	
		
		/*
		int scelta = 2;
		switch(scelta) {
			case 1 :
				FileDiv split1 = new FileDiv("./prova.png", true, false, false, 4096);
				PartDiv split2 = new PartDiv("./test.txt", true, false, false, 6);
				DimDiv split3 = new DimDiv("./home.jpg", true, false, false);
				//long numparts = split.DivideFile();
				
				DivisionHandler de = new DivisionHandler();
				
				de.addFile(split1);
				de.addFile(split2);
				de.addFile(split3);

				de.split();
				
				//System.out.println("Finito : " + numparts + " parti");
				break;
			case 2 : 
				/*FileDiv merge1 = new FileDiv("./prova0.frame.png", false, false, false, 4096);
				PartDiv merge2 = new PartDiv("./test0.part.txt", false, false, false, 6);
				DimDiv merge3 = new DimDiv("./home0.dim.jpg", false, false, false);
				
				try {
					DivisionHandler dh = new DivisionHandler();
					
					dh.addFile("./prova0.frame.png");
					//dh.addFile("./test0.part.txt");
					//dh.addFile("./home0.dim.jpg");
					
					dh.merge();
					
					System.out.println("Finito !!");
				}catch(IOException e) {
					e.printStackTrace();
				}
				break;
		}
		*/
	}

}
