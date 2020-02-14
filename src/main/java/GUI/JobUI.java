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

	public JobUI() {
		
		setLayout(new FlowLayout(FlowLayout.CENTER, jobPadding, jobPadding));
		
		fileName = new JLabel("Nome File");
		this.add(fileName);
		
	}
	
}
