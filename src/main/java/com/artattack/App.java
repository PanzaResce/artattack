package com.artattack;

import java.awt.Color;

import GUI.AppFrame;

/**
* 
* 
* <p>Il programma usa la classe {@link GUI.AppFrame} per comporre due panel dove rispettivamente si possono unire 
* e dividere dei file</p>
* <p>Ogni panel eredita dalla classe padre astratta {@link GUI.AppPanel} che imposta la parte di interfaccia comune, ogni pannello
* poi si specializza nella classe apposita per la divisione e per l'unione, rispettivamente {@link GUI.SplitPanel} e {@link GUI.MergePanel}</p>
* <p>Il programa fa scegliere tra tre tipi di divisioni</p>
* <ul>
* 	<li>Di default si divide il file in parti uguali in base alla grandezza specificata per ogni parte ("FileDiv")</li>
* 	<li>Divide il file in un numero specificabile di parti ("PartDiv")</li>
* 	<li>Divide il file impostando per ogni parte la grandezza desiderata ("DimDiv")</li>
* </ul>
* <p> Ad ogni tipo di divisione è associata  una classe, rispettivamente sono {@link FileDiv}, {@link PartDiv} e {@link DimDiv}. </p>
* <p> Il tipo di divisione rappresenta anche l'estensione del file di output, rispettivamente le estensioni sono .frame, .part e .dim. <br>
* Il nome dell'estensione viene memorizzato nell'attributo {@link FileDiv#EXT} ed è impostato dal costruttore della classe. </p>
* 
* <p> La classe padre è {@link FileDiv} che imposta le primitive per la divisione e l'unione tramite i metodi {@link FileDiv#DivideFile} e 
* {@link FileDiv#MergeFile}, in particolare quest'ultimo non viene sovrascritto dalle classi figlie poichè qualsiasi sia il tipo di 
* divisione effettuata, l'unione corrisponde sempre a prendere un'insieme di file e riunirli in sequenza in un unico file. </p>
* 
* <p> L'approccio cambia per la divisione: nei primi due casi, FileDiv e PartDiv, ogni parte prodotta dall'operazione di divisione ha sempre la stessa
* grandezza e infatti viene usata la funzione {@link FileDiv#DivideFile} in entrambi; nell'ultimo invece si deve poter specificare 
* la grandezza di ogni parte e perciò {@link DimDiv#DivideFile} viene sovrascritta. </p>
* 
* <p> Gli oggetti di tipo {@link FileDiv} sono organizzati mediante la classe {@link DivisionHandler} con una coda, con il metodo
* {@link DivisionHandler#execute} ogni oggetto {@link FileDiv} esegue la sua operazione di divisione o unione in un thread a parte.<br>
* La classe {@link DivisionHandler} non distingue tra oggetti {@link FileDiv} impostati in modalità divisione o unione, poichè quando viene
* eseguito il thread il metodo {@link FileDiv#run()} determina automaticamente l'operazione corretta in base al valore di {@link FileDiv#splitmode} .
* </p>
* 
* 
* 
* @author  Marco Panarelli
* @version 1.0
* @since   2020 
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
