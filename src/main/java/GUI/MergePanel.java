package GUI;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.artattack.DivisionHandler;
import com.artattack.FileDiv;
import com.artattack.PartDiv;

public class MergePanel extends AppPanel{
	
	DivisionHandler mergeQueue = new DivisionHandler();
	
	public MergePanel(int w, int h) {
		super(w, h);
	}
	public MergePanel(int w, int h, Color c) {
		super(w, h, c);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void addElementToContainer(String fname) {
		JobUI job = new JobUI(fname);
		container.add(job);
		addJob(job);
		revalidate();
	}
	
	@Override
	protected void startBtnAction() {
		for(int i = 0; i < jobQueue.size(); i++) {
			/*System.out.print(getJob(i).getJobType().getSelectedItem());
			System.out.print(getJob(i).toZip());
			System.out.println(getJob(i).toCrypt());
			*/
			String fname = getJob(i).getFileName().getText();
			
			mergeQueue.addFile(fname);
			
			System.out.println("ADDED");
		}
		
		try {
			mergeQueue.merge();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mergeQueue.clear();
		clearContainer();

	}
	
	@Override
	protected void fileBtnAction() {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        	addElementToContainer(file.getAbsolutePath());
        }

	}
	
	@Override
	protected JobUI getJob(int index) {
		return (JobUI)jobQueue.get(index);
	}
	
	
}
