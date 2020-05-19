package GUI;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Define the UI of a single Job
 * @author marco
 *
 */
public class JobUI extends JPanel{
	
	/**
	 * Index for insert order in the UI 
	 */
	protected int index = -1;
	
	protected JButton delBtn = new JButton("x");
	protected JLabel fileName;
	/**
	 * Padding between each element of the job
	 */
	protected int jobPadding = 20;

	public JobUI(String fname, int index) {
		
		setLayout(new FlowLayout(FlowLayout.CENTER, jobPadding, jobPadding));
		
		this.add(delBtn);
		
		fileName = new JLabel(fname);
		this.add(fileName);
		
		this.setIndex(index);
		
	}
	
	
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}



	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
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

	/**
	 * @return the delBtn
	 */
	public JButton getDelBtn() {
		return delBtn;
	}

	/**
	 * @param delBtn the delBtn to set
	 */
	public void setDelBtn(JButton delBtn) {
		this.delBtn = delBtn;
	}
	
	
}
