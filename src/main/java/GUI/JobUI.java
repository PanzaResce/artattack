package GUI;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Definisce l'interfaccia grafica del singolo job che di base prevede:
 * <ul>
 * 		<li>una label contenente la path del file</li>
 * 		<li>un bottone X per l'eliminazione del Job dall'interfaccia</li>
 * </ul>
 * 
 * Utilizza un FlowLayout
 * @author marco
 *
 */
public class JobUI extends JPanel{
	
	protected int index = -1;
	
	protected JButton delBtn = new JButton("x");
	protected JLabel fileName;
	
	/**
	 * Padding tra i vari elementi
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
	 * @param index 
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
	 * @param fileName 
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
	 * @param delBtn 
	 */
	public void setDelBtn(JButton delBtn) {
		this.delBtn = delBtn;
	}
	
	
}
