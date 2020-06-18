package GUI;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.artattack.FileDiv;

/**
 * 
 * Classe concreta che implementa il pannello di unione dei file, utilizza l'oggetto JobUI
 * 
 * Con il bottone Start, metodo {@link MergePanel#fileBtnAction()}, si sceglie il file dal quale effettuare l'unione, 
 * questo file deve essere il primo (indice = 0) generato dalle precedenti operazioni di divisione. <br>
 * 
 * Il metodo {@link MergePanel#startBtnAction()} riempe l'oggetto {@link AppPanel#mainQueue} e esegue le varie unioni
 * 
 * @author marco
 *
 */

public class MergePanel extends AppPanel<JobUI>{

	private static final long serialVersionUID = 1L;

	public MergePanel(int w, int h, String textType) {
		super(w, h, textType);
	}
	public MergePanel(int w, int h, Color c, String textType) {
		super(w, h, c, textType);
	}
	
	/**
	 * Per ogni file controlla se è criptato e in caso, tramite un input, chiede di inserire la password. <br>
	 * Se si insersice la password sbagliata, l'applicativo non restitusice nessun messaggio di errore ma il file decifrato sarà inconsistente.
	 */
	@Override
	protected void startBtnAction() {
		for(int i = 0; i < jobQueue.size(); i++) {
			/*System.out.print(getJob(i).getJobType().getSelectedItem());
			System.out.print(getJob(i).toZip());
			System.out.println(getJob(i).toCrypt());
			*/
			String fname = getJob(i).getFileName().getText();
			
			mainQueue.addFile(fname, false);
			
			FileDiv lastInserted = mainQueue.getFile(i);
			
			if(lastInserted.isEncrypted()) {
				String psw = null;
				do {
					psw = (String)JOptionPane.showInputDialog(
		                    container,
		                    "Inserirsci password per file " + fname,
		                    "Customized Dialog",
		                    JOptionPane.PLAIN_MESSAGE
		                    );
				}while(psw == null || psw.equals(""));
				
				lastInserted.setPsw(psw);	
			}
			
			
			System.out.println("ADDED");
		}
		
		mainQueue.execute();
		
		mainQueue.clear();
		clearContainer();


	}
	
	/**
	 * Apre un JFileChooser e chiama il metodo {@link AppPanel#addElementToContainer(JobUI)} passando un oggetto di tipo {@link JobUI} 
	 */
	@Override
	protected void fileBtnAction() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        	addElementToContainer(new JobUI(file.getAbsolutePath(), this.mainQueue.getLength()));
        }

	}
	
	@Override
	protected JobUI getJob(int index) {
		return (JobUI)jobQueue.get(index);
	}
	
	
}
