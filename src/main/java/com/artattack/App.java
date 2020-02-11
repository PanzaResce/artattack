package com.artattack;

import java.io.IOException;

import com.artattack.FileDiv;


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
		int scelta = 2;
		switch(scelta) {
			case 1 :
				//FileDiv split = new FileDiv("./prova.png", true, false, true, 4096);
				//PartDiv split = new PartDiv("./test.txt", true, false, false, 6);
				DimDiv split = new DimDiv("./test.txt", true, false, true);
				long numparts = split.DivideFile();
				
				System.out.println("Finito : " + numparts + " parti");
				break;
			case 2 : 
				//FileDiv merge = new FileDiv("./test0.frame.txt", false, false, false, 4096);
				//PartDiv merge = new PartDiv("./test0.part.txt", false, false, false, 6);
				DimDiv merge = new DimDiv("./test0.dim.txt.zip", false, false, true);

				try {
					merge.MergeFile();
					System.out.println("Finito !!");
				}catch(IOException e) {
					e.printStackTrace();
				}
				break;
		}
		
	}

}
