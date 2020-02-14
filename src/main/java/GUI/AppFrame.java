package GUI;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class AppFrame extends JFrame{
	
	private int width = 1280;
	private int height = 720;
	
	private int startX = 100;
	private int startY = 100;
	
	private JPanel jpl;
	private JPanel jpr;
	
	public AppFrame(String titolo, Color c){ 
		super(titolo); 
		setBounds(startX,startY, width, height);
		//this.getContentPane().setBackground(new Color(255,255,255));
		
		setJpl(getWidth()/2, getHeight(), c);
        setJpr(getWidth()/2, getHeight(), c);
        setSplitPane();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	public JSplitPane setSplitPane() {
		JSplitPane splitPane = new JSplitPane();
        splitPane.setSize(getWidth(), getHeight());
        splitPane.setDividerSize(5);
        splitPane.setDividerLocation(getWidth()/2);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(getJpl());
        splitPane.setRightComponent(getJpr());
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
	 * @param jpl the jpl to set
	 */
	public void setJpl(int w, int h, Color c) {
		this.jpl = new AppPanel(w,h,c);
	}

	/**
	 * @return the jpr
	 */
	public JPanel getJpr() {
		return jpr;
	}

	/**
	 * @param jpr the jpr to set
	 */
	public void setJpr(int w, int h, Color c) {
		this.jpr =  new SplitPanel(w,h,c);
	}
}
