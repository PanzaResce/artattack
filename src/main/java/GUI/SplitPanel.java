package GUI;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.artattack.DimDiv;
import com.artattack.FileDiv;
import com.artattack.PartDiv;

/**
 * Classe concreta che implementa il pannello di divisione dei file, utilizza l'oggetto SplitJobUI. <br>
 * 
 * Il metodo {@link SplitPanel#startBtnAction()} scorre l'oggetto {@link AppPanel#jobQueue} e riempe l'oggetto {@link AppPanel#mainQueue}
 * impostando tutti i valori immessi tramite l'interfaccia grafica (password, numero di parti, ecc.)
 * 
 * @author marco
 *
 */

public class SplitPanel extends AppPanel<SplitJobUI>{

	public SplitPanel(int w, int h, String textType) {
		super(w, h, textType);
	}
	
	@SuppressWarnings("unchecked")
	public SplitPanel(int w, int h, Color c, String textType) {
		super(w, h, c, textType);
		jobQueue = new ArrayList<SplitJobUI>();
	}
	
	/**
	 * Scorre l'oggetto {@link AppPanel#jobQueue} e riempe {@link AppPanel#mainQueue} con oggetti di tipo {@link com.artattack.FileDiv},
	 * {@link com.artattack.PartDiv} o {@link com.artattack.DimDiv} in base alla tipologia di divisione scelta
	 */
	@Override
	protected void startBtnAction() {
		
		for(int i = 0; i < jobQueue.size(); i++) {
			/*System.out.print(getJob(i).getJobType().getSelectedItem());
			System.out.print(getJob(i).toZip());
			System.out.println(getJob(i).toCrypt());
			*/
			String JobType = getJob(i).getJobType().getSelectedItem().toString();
			String fname = getJob(i).getFileName().getText();
			
			FileDiv job = null;
			
			switch(JobType) {
				case "FrameDiv" :
					//ask for buffer size
					job = new FileDiv(fname, true, getJob(i).toCrypt(), getJob(i).toZip(), getJob(i).getBufferField()*1024);
					System.out.println("ADDED");
					break;
					
				case "PartDiv" :
					//ask for num parts
					job = new PartDiv(fname, true, getJob(i).toCrypt(), getJob(i).toZip(), getJob(i).getNumPartField());
					System.out.println("ADDED");
					break;
					
				case "DimDiv" :
					//ask for dimension of each part
					job = new DimDiv(fname, true, getJob(i).toCrypt(), getJob(i).toZip(), getJob(i).getDivision());
					break;
			}
			
			if(getJob(i).toCrypt()) {
				job.setPsw(getJob(i).getPswField().getText());
			}
			
			
			mainQueue.addFile(job);
			
		}
	
		//start jprogressbar here

		
		//System.out.println(splitQueue.toString());
	}
	
	
	/**
	 * Apre un JFileChooser e chiama il metodo {@link AppPanel#addElementToContainer(JobUI)} passando un oggetto di tipo {@link SplitJobUI}
	 */
	@Override
	protected void fileBtnAction() {
		//addElementToContainer();
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        	addElementToContainer(new SplitJobUI(file.getAbsolutePath(), this.mainQueue.getLength()));
        }
	}

	@Override
	protected SplitJobUI getJob(int index) {
		return (SplitJobUI)jobQueue.get(index);
	}

	
}
