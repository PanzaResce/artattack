package GUI;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.artattack.DivisionHandler;
import com.artattack.FileDiv;
import com.artattack.PartDiv;

public class MergePanel extends AppPanel{
		
	public MergePanel(int w, int h) {
		super(w, h);
	}
	public MergePanel(int w, int h, Color c) {
		super(w, h, c);
	}
	
	
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
		
		try {
			mainQueue.merge();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainQueue.clear();
		clearContainer();

	}
	
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
