package main.java;
import java.io.IOException;

import main.java.FileDiv;


/**
* <h1>File Splitter</h1>
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

public class artattack {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//FileDiv prova = new FileDiv("./test.txt",true,false,4096);
		/*FileDiv prova = new FileDiv("./prova.PNG", true);
		long numparts = prova.DivideFile();
		
		System.out.println("Finito : " + numparts + " parti");
		*/
		
		FileDiv prova = new FileDiv("./prova0.frame.PNG", false);
		try {
			prova.MergeFile();
		}catch(IOException e) {
			e.printStackTrace();
		}
			
	}

}
