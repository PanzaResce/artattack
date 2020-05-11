package com.artattack;

import java.awt.Color;
import java.io.IOException;
import GUI.AppFrame;

/**
*
* File Splitter
* <p>This program takes one file as input and divide it following different split routines </p><br>
* <ul>
* 	<li>By default it divides file by a frame size taken as input</li>
* 	<li>Divide file in N parts </li>
* 	<li>Divide file by specifying the dimension of each part </li>
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
		// TODO Auto-generated method stub
		
		
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
				*/
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
