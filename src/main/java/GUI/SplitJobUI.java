package GUI;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class SplitJobUI extends JobUI{

	private JRadioButton zipBtn;
	private JRadioButton cryptBtn;
	private JComboBox jobType;
	
	private String jobCategories[] = {"FrameDiv", "PartDiv", "DimDiv"};
	
	public SplitJobUI() {
		super();
		
		zipBtn = new JRadioButton("Zip");
		cryptBtn = new JRadioButton("Crypt");
		jobType = new JComboBox(jobCategories);
		
		this.add(zipBtn);
		this.add(cryptBtn);
		this.add(jobType);
		
	}
	
}
