package GUI;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Define the UI of a single Job
 * @author marco
 *
 */
public class JobUI extends JPanel{
	
	protected JLabel fileName;
	
	/**
	 * Padding between each element of the job
	 */
	protected int jobPadding = 45;

	public JobUI(String fname) {
		
		setLayout(new FlowLayout(FlowLayout.CENTER, jobPadding, jobPadding));
		
		fileName = new JLabel(fname);
		this.add(fileName);
		
	}

	/**
	 * @return the fileName
	 */
	public JLabel getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(JLabel fileName) {
		this.fileName = fileName;
	}
	
	
}
