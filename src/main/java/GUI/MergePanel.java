package GUI;

import java.awt.Color;

public class MergePanel extends AppPanel{

	public MergePanel(int w, int h) {
		super(w, h);
	}
	public MergePanel(int w, int h, Color c) {
		super(w, h, c);
	}
	
	
	@Override
	protected void addElementToContainer() {
		JobUI job = new JobUI();
		container.add(job);

	}

}
