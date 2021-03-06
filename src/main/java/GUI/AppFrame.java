package GUI;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Imposta l'interfaccia generale dell'applicativo. <br>
 * 
 * L'area viene divisa in due sottoaree mediante l'oggetto {@link JSplitPane}, con a destra e sinistra rispettivamente le aree di divisione e unione. <br>
 * 
 * Tramite i metodi {@link AppFrame#setJpl} e {@link AppFrame#setJpr} si impostano gli oggetti {@link MergePanel} e {@link SplitPanel}, 
 * entrambi i pannelli ereditano da {@link AppPanel} che imposta l'interfaccia base e funge da {@link AppPanel#actionPerformed(java.awt.event.ActionEvent) Listener} per gli eventi
 * 
 * @author marco
 *
 */
public class AppFrame extends JFrame{
	
	private int width = 1920;
	private int height = 1080;
	
	private int startX = 50;
	private int startY = 50;
	
	private JPanel jpl;
	private JPanel jpr;
	
	public AppFrame(String titolo, Color c){ 
		super(titolo); 
		setBounds(startX,startY, width, height);
		//this.getContentPane().setBackground(new Color(255,255,255));
		
		setJpl(getWidth()/2, getHeight(), c);
        setJpr(getWidth()/2, getHeight(), c);
        setSplitPane();
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setVisible(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * L'interfaccia è impostata mediane un JSplitPane, che divide l'area in due parti uguali che si possono ridimensionare. <br>
	 * Il metodo aggiunge lo JSplitPane al container
	 * @return
	 */
	public JSplitPane setSplitPane() {
		JSplitPane splitPane = new JSplitPane();
        splitPane.setSize(getWidth(), getHeight());
        splitPane.setDividerSize(5);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(getJpl());
        splitPane.setRightComponent(getJpr());
        splitPane.setResizeWeight(0.5f);
        splitPane.setBackground(new Color(255, 255, 255));
        setPadding(splitPane, 0);
        
        this.add(splitPane);
        
        return splitPane;
	}
	
	public void setPadding(JComponent comp, int padding) {
		Border border = comp.getBorder();
		Border margin = new EmptyBorder(padding, padding, padding, padding);
		comp.setBorder(new CompoundBorder(border, margin));
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * @param startX the startX to set
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

	/**
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * @param startY the startY to set
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}

	/**
	 * @return the jpl
	 */
	public JPanel getJpl() {
		return jpl;
	}

	/**
	 * Viene impostato con un oggetto di tipo {@link MergePanel}
	 * @param w
	 */
	public void setJpl(int w, int h, Color c) {
		this.jpl = new MergePanel(w, h, c, "Unisci File");
	}

	/**
	 * @return the jpr
	 */
	public JPanel getJpr() {
		return jpr;
	}

	/**
	 * Viene impostato con un oggetto di tipo {@link SplitPanel}
	 * @param w
	 */
	public void setJpr(int w, int h, Color c) {
		this.jpr =  new SplitPanel(w, h, c, "Dividi File");
	}
}
