package GUI;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Define the UI of a single Job
 * @author marco
 *
 */
public class JobUI extends JPanel{
	
	protected JButton delBtn = new JButton("x");
	protected JLabel fileName;
	
	/**
	 * Padding between each element of the job
	 */
	protected int jobPadding = 20;

	public JobUI(String fname) {
		
		setLayout(new FlowLayout(FlowLayout.CENTER, jobPadding, jobPadding));
		
		this.add(delBtn);
		
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
