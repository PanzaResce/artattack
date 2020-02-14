package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class AppPanel extends JPanel{
	
	
	protected JPanel container;
	
	protected JButton startBtn;
	protected JButton fileBtn;
	protected JProgressBar pBar;
	
	protected int fontSize = 16;
	
	
	public AppPanel(int w, int h) {
		this.setSize(w, h);
	}
	
	public AppPanel(int w, int h, Color c) {
		this(w, h);
		this.setBackground(c);
		this.setLayout(new BorderLayout());
		
		/*
		jobList = new DefaultListModel<>();
		jobList.addElement(new GeneralJob("John Doe"));
		//jobList.addElement(new GeneralJob("John Smith"));
		//jobList.addElement(new GeneralJob("Kathy Green"));
		
		
		list = new JList<>(jobList);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setCellRenderer(new JobRenderer(c));
		list.setEnabled(true);
		*/
		//JScrollPane listScroller = new JScrollPane(list);
		//listScroller.setEnabled(true);
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		setPadding(container, 60, 60, 0, 0);
		addElementToContainer();
		addElementToContainer();
		addElementToContainer();
		
		JScrollPane jobList = new JScrollPane(container);
		
		//Costruzione Bottom Panel
		JPanel bottomPnl = new JPanel();
		bottomPnl.setLayout(new GridBagLayout());
		GridBagConstraints gbc= new GridBagConstraints();
		
		//Costruzione bottone Start
		startBtn = new JButton("Start");
		startBtn.setPreferredSize(new Dimension(120, 30));
		startBtn.setMinimumSize(new Dimension(50, 30));

		//Costruzione bottone File
		fileBtn = new JButton("File");
		fileBtn.setPreferredSize(new Dimension(120, 30));
		fileBtn.setMinimumSize(new Dimension(50, 30));

		//Costruzione Progress Bar
		pBar = new JProgressBar(0, 100);
		pBar.setValue(50);
		pBar.setStringPainted(true);
		pBar.setPreferredSize(new Dimension(500, 30));
		pBar.setMinimumSize(new Dimension(200, 30));

		//Styling joblist
		/*setPadding(list, 60, 60, 0, 60);
		list.setFont(new Font(list.getFont().getName(), Font.BOLD, fontSize));
		list.setFixedCellHeight(jobPadding);
		list.setBackground(c);
		*/
		//Styling bottone Start
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		bottomPnl.add(startBtn, gbc);

		//Styling bottone File
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		bottomPnl.add(fileBtn, gbc);
		
		//Styling Progress Bar
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		bottomPnl.add(pBar, gbc);
		bottomPnl.setBackground(c);
		

		//Aggiunta elementi a main panel
		this.add(jobList, BorderLayout.CENTER);
		this.add(bottomPnl, BorderLayout.SOUTH);		
		
		this.setVisible(true);
	
	}
	
	/*
	 * Set padding between component and its container
	 */
	public void setPadding(JComponent comp, int top, int left, int bottom, int right) {
		Border border = comp.getBorder();
		Border margin = new EmptyBorder(top, left, bottom, left);
		comp.setBorder(new CompoundBorder(border, margin));
	}
	
	protected void addElementToContainer() {
		String s[] = {"FrameDiv", "PartDiv", "DimDiv"};
		
		/*
		JComboBox jobType = new JComboBox(s);

		JLabel fileName = new JLabel("Nome File");
		
		JRadioButton zipBtn = new JRadioButton("Zip");

		JRadioButton cryptBtn = new JRadioButton("Crypt");

		JPanel job = new JPanel(new FlowLayout(FlowLayout.CENTER, jobPadding, jobPadding));
		job.add(fileName);
		job.add(zipBtn);
		job.add(cryptBtn);
		job.add(jobType);
		*/
		
		JobUI job = new JobUI();
		
		container.add(job);
	
	}
	
}
