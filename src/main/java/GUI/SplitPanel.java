package GUI;

import java.awt.Color;

public class SplitPanel extends AppPanel{

	public SplitPanel(int w, int h) {
		super(w, h);
	}
	
	public SplitPanel(int w, int h, Color c) {
		super(w, h, c);
	}

	protected void addElementToContainer() {
		SplitJobUI job = new SplitJobUI();
		container.add(job);
	}	
}
