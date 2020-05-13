package GUI;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.artattack.DivisionHandler;
import com.artattack.FileDiv;
import com.artattack.PartDiv;

public class SplitPanel extends AppPanel{
	
	DivisionHandler splitQueue = new DivisionHandler();

	public SplitPanel(int w, int h) {
		super(w, h);
	}
	
	@SuppressWarnings("unchecked")
	public SplitPanel(int w, int h, Color c) {
		super(w, h, c);
		jobQueue = new ArrayList<SplitJobUI>();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void addElementToContainer(String fname) {
		SplitJobUI job = new SplitJobUI(fname);
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
			String JobType = getJob(i).getJobType().getSelectedItem().toString();
			String fname = getJob(i).getFileName().getText();
			
			FileDiv job = null;
			
			switch(JobType) {
				case "FrameDiv" :
					//ask for buffer size
					job = new FileDiv(fname, true, getJob(i).toCrypt(), getJob(i).toZip(), 4096);
					System.out.println("ADDED");
					break;
					
				case "PartDiv" :
					//ask for num parts
					job = new PartDiv(fname, true, getJob(i).toCrypt(), getJob(i).toZip(), 5);
					System.out.println("ADDED");
					break;
					
				case "DimDiv" :
					//ask for dimension of each part
					
					break;
			}
			
			if(getJob(i).toCrypt()) {
				job.setPsw(getJob(i).getPswField().getText());
			}
			
			splitQueue.addFile(job);
			
			
		}
		
		splitQueue.split();
		
		splitQueue.clear();
		clearContainer();
		
		//System.out.println(splitQueue.toString());
	}

	@Override
	protected void fileBtnAction() {
		//addElementToContainer();
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        	addElementToContainer(file.getAbsolutePath());
        }
	}

	@Override
	protected SplitJobUI getJob(int index) {
		return (SplitJobUI)jobQueue.get(index);
	}

	
}