package GUI;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class SplitJobUI extends JobUI{

	protected JCheckBox zipBtn;
	protected JCheckBox cryptBtn;
	
	protected JComboBox<String> jobType;
	
	protected JLabel txtFieldLabel;
	protected JFormattedTextField numPartField;
	
	protected ImageIcon settingsIcon = new ImageIcon("src/main/resources/settings.png");
	protected JButton dimdivButton;
	
	protected JTextField pswField;
	
	protected String jobCategories[] = {"FrameDiv", "PartDiv", "DimDiv"};
	
	/**
	 * ArrayList buffer for the DimDiv class
	 */
	protected ArrayList<Long> division = new ArrayList<Long>();

	public SplitJobUI(String fname, int index) {
		super(fname, index);
		
		zipBtn = new JCheckBox("Zip");
		cryptBtn = new JCheckBox("Crypt");
		
		jobType = new JComboBox<String>(jobCategories);
		
		//label + textfield per inserimento parti
		txtFieldLabel = new JLabel("N.Parti");
		txtFieldLabel.setVisible(false);
		try {
			numPartField = new JFormattedTextField(new MaskFormatter("#"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		numPartField.setVisible(false);
		numPartField.setPreferredSize(new Dimension(30,20));
		
		Image buff = settingsIcon.getImage();
		buff = buff.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
		settingsIcon = new ImageIcon(buff);
		//dimdivIcon.setPreferredSize(new Dimension(20,20));
		dimdivButton = new JButton(settingsIcon);
		dimdivButton.setPreferredSize(new Dimension(40,30));
		dimdivButton.setVisible(false);

		//textfield per inserimento password
		pswField = new JTextField("inserire psw per archivio");
		pswField.setPreferredSize(new Dimension(140, 20));
		pswField.setVisible(false);
		
		
		this.add(zipBtn);
		this.add(cryptBtn);
		this.add(jobType);
		this.add(txtFieldLabel);
		this.add(numPartField);
		this.add(dimdivButton);
		this.add(pswField);
		
		checkBoxListener();
		comboBoxListener();
		settingsButtonListener();
		
	}
	
	/**
	 * show/hide the numPartFiled if "PartDiv" is selected
	 * show/hide the settings icon if "DimDiv" is selected
	 */
	private void comboBoxListener() {
		jobType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jobType.getSelectedItem().toString() == "PartDiv") {
					numPartField.setVisible(true); 
					txtFieldLabel.setVisible(true);
				} else {
					numPartField.setVisible(false); 
					txtFieldLabel.setVisible(false);
				}
				
				if(jobType.getSelectedItem().toString() == "DimDiv") {
					dimdivButton.setVisible(true);
				}else
					dimdivButton.setVisible(false);
				
				revalidate();
				repaint();
				
			}
		});
	}

	/**
	 * Show/hide pswField if cryptBtn is selected 
	 */
	private void checkBoxListener() {
		zipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(zipBtn.isSelected()) {
					pswField.setVisible(false);
					cryptBtn.setSelected(false);
				}
				revalidate();
				repaint();
			}
		});
		
		cryptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cryptBtn.isSelected()) {
					pswField.setVisible(true);
				 	zipBtn.setSelected(false);
				} else {
					pswField.setVisible(false);
				}
				revalidate();
				repaint();
			}
		});
	}
	
	private void settingsButtonListener() {
		dimdivButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
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
	public JCheckBox getZipBtn() {
		return zipBtn;
	}

	/**
	 * @param zipBtn the zipBtn to set
	 */
	public void setZipBtn(JCheckBox zipBtn) {
		this.zipBtn = zipBtn;
	}

	/**
	 * @return the cryptBtn
	 */
	public JCheckBox getCryptBtn() {
		return cryptBtn;
	}

	/**
	 * @param cryptBtn the cryptBtn to set
	 */
	public void setCryptBtn(JCheckBox cryptBtn) {
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
	 * @return the numPartField
	 */
	public JFormattedTextField getNumPartField() {
		return numPartField;
	}

	/**
	 * @param numPartField the numPartField to set
	 */
	public void setNumPartField(JFormattedTextField numPartField) {
		this.numPartField = numPartField;
	}

	/**
	 * @return the pswField
	 */
	public JTextField getPswField() {
		return pswField;
	}

	/**
	 * @param pswField the pswField to set
	 */
	public void setPswField(JTextField pswField) {
		this.pswField = pswField;
	}

	/**
	 * @return the division
	 */
	public ArrayList<Long> getDivision() {
		return division;
	}

	/**
	 * @param division the division to set
	 */
	public void setDivision(ArrayList<Long> division) {
		this.division = division;
	}
	
	
	
}
