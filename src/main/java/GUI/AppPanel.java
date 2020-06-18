package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.artattack.DivisionHandler;

public abstract class AppPanel<Job extends JobUI> extends JPanel implements ActionListener {
	
	
	protected JPanel container;
	
	protected JLabel panelType = new JLabel("");
	protected JButton startBtn = new JButton("Start");
	protected JButton fileBtn = new JButton("File");
	//protected JProgressBar pBar;
	
	protected int fontSize = 16;
	
	
	/**
	 * ArrayList which store the generic Jobs, the effective classes will insert their type of job (SplitPanel -> SplitJobUI)
	 */
	protected ArrayList<Job> jobQueue = new ArrayList<Job>();

	/**
	 * Object which implement the queue for the job that will be split/divided
	 */
	protected DivisionHandler mainQueue = new DivisionHandler();

	
	public AppPanel(int w, int h, String textType) {
		this.setSize(w, h);
		startBtn.addActionListener(this);
		fileBtn.addActionListener(this);	
		panelType.setText(textType);
	}
	
	public AppPanel(int w, int h, Color c ,String textType) {
		this(w, h, textType);
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
		
		JScrollPane jobList = new JScrollPane(container);

		//Costruzione Bottom Panel
		JPanel bottomPnl = new JPanel();
		bottomPnl.setLayout(new GridBagLayout());
		GridBagConstraints gbc= new GridBagConstraints();
		
		//Costruzione bottone Start
		startBtn.setPreferredSize(new Dimension(120, 30));
		startBtn.setMinimumSize(new Dimension(50, 30));

		//Costruzione bottone File
		fileBtn.setPreferredSize(new Dimension(120, 30));
		fileBtn.setMinimumSize(new Dimension(50, 30));

		//Costruzione Progress Bar
		/*pBar = new JProgressBar(0, 100);
		pBar.setValue(0);
		pBar.setStringPainted(true);
		pBar.setPreferredSize(new Dimension(500, 30));
		pBar.setMinimumSize(new Dimension(200, 30));
		 */
		//Styling joblist
		/*setPadding(list, 60, 60, 0, 60);
		list.setFont(new Font(list.getFont().getName(), Font.BOLD, fontSize));
		list.setFixedCellHeight(jobPadding);
		list.setBackground(c);
		*/
		//costruzione label per indicare il tipo di pannello
		panelType.setHorizontalAlignment(JLabel.CENTER);
		panelType.setFont(new Font(panelType.getFont().getName(), Font.BOLD, fontSize));
		
		//Styling bottone Start
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		bottomPnl.add(startBtn, gbc);

		//Styling bottone File
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		bottomPnl.add(fileBtn, gbc);
		
		//Styling Progress Bar
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		//bottomPnl.add(pBar, gbc);
		bottomPnl.add(panelType, gbc);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton buttonClicked = (JButton)e.getSource();
		System.out.println(buttonClicked);
		if(e.getSource() == startBtn) {
			startBtnAction();	
			//Thread pbarThread = new Thread(this.pBar);
			//pbarThread.start();
			
			mainQueue.execute();					
			
			clearContainer();
		}
		else if(e.getSource() == fileBtn) {
			fileBtnAction();
		}else{
			//scorro gli eventi provenienti dagli oggetti Job
			for(Job j : jobQueue) {
				if(e.getSource() == j.delBtn) {
					//System.out.println("yeah");
					//Component c = (Component) e.getSource();
					//if(c.equals())
					removeJob(j);
					break;
				}
			}
			
		}
	}
	
	protected void addJob(Job job) {
		jobQueue.add(job);
		job.delBtn.addActionListener(this);
	}

	protected void removeJob(Job job) {
		container.remove(job);
		jobQueue.remove(job);
		revalidate();
		repaint();
	}
	
	protected void clearContainer() {
		/*
		Component[] componentList = container.getComponents();
		for(Component c : componentList){
			container.remove(c);
		}
		*/
		container.removeAll();
		jobQueue.clear();
		
		revalidate();
		repaint();
	}
	
	/**
	 * Define the type of UI element to be added, the subclass needs to pass down the type of Job (JobUI, SplitJobUI, ...)
	 * This also add the element to the mainQueue
	 */
	protected void addElementToContainer(Job job) {
		container.add(job);
		addJob(job);
		
		revalidate();
		repaint();
	}	
	
	/**
	 * 
	 * @param index
	 * @return the instanced Object for the Job
	 */
	protected abstract Job getJob(int index) ;
	
	/**
	 * The implementation of the action for the start button is delegated to the child class
	 */
	protected abstract void startBtnAction();

	/**
	 * The implementation of the action for the file button is delegated to the child class
	 */
	protected abstract void fileBtnAction();
	
}
