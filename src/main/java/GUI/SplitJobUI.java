package GUI;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class SplitJobUI extends JobUI{

	private JRadioButton zipBtn;
	private JRadioButton cryptBtn;
	private JComboBox<String> jobType;
	
	private ButtonGroup btnGroup;
	
	private String jobCategories[] = {"FrameDiv", "PartDiv", "DimDiv"};
	
	public SplitJobUI(String fname) {
		super(fname);
		
		zipBtn = new JRadioButton("Zip");
		cryptBtn = new JRadioButton("Crypt");
		jobType = new JComboBox<String>(jobCategories);
		
		btnGroup = new ButtonGroup();
		btnGroup.add(cryptBtn);
		btnGroup.add(zipBtn);
		
		this.add(zipBtn);
		this.add(cryptBtn);
		this.add(jobType);
		
	}
	
	/**
	 * 
	 * @return true if {@code zipBtn} is selected
	 */
	public boolean toZip() {
		return (zipBtn.isSelected()) ? true : false;
	}
	
	/**
	 * 
	 * @return true if {@code cryptBtn} is selected
	 */
	public boolean toCrypt() {
		return (cryptBtn.isSelected()) ? true : false;
	}

	/**
	 * @return the zipBtn
	 */
	public JRadioButton getZipBtn() {
		return zipBtn;
	}

	/**
	 * @param zipBtn the zipBtn to set
	 */
	public void setZipBtn(JRadioButton zipBtn) {
		this.zipBtn = zipBtn;
	}

	/**
	 * @return the cryptBtn
	 */
	public JRadioButton getCryptBtn() {
		return cryptBtn;
	}

	/**
	 * @param cryptBtn the cryptBtn to set
	 */
	public void setCryptBtn(JRadioButton cryptBtn) {
		this.cryptBtn = cryptBtn;
	}

	/**
	 * @return the jobType
	 */
	public JComboBox getJobType() {
		return jobType;
	}

	/**
	 * @param jobType the jobType to set
	 */
	public void setJobType(JComboBox jobType) {
		this.jobType = jobType;
	}

	/**
	 * @return the jobCategories
	 */
	public String[] getJobCategories() {
		return jobCategories;
	}

	/**
	 * @param jobCategories the jobCategories to set
	 */
	public void setJobCategories(String[] jobCategories) {
		this.jobCategories = jobCategories;
	}

	/**
	 * @return the grp
	 */
	public ButtonGroup getGroup() {
		return btnGroup;
	}

	/**
	 * @param grp the grp to set
	 */
	public void setGroup(ButtonGroup grp) {
		this.btnGroup = grp;
	}
	
	
	
}
