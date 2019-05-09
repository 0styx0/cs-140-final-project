package projectview;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel{
	private Mediator mediator;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton = new JButton("Clear");
	private JButton runButton = new JButton("Run/Pause");
	private JButton reloadButton = new JButton("Reload");
	
	public ControlPanel(Mediator med) {
		this.mediator = med;
	}
	
	public JComponent createControlDisplay() {
		JPanel panel = new JPanel(new GridLayout(1,0));
		
		stepButton.setBackground(Color.WHITE);
		stepButton.addActionListener(e -> mediator.step());
		panel.add(stepButton);
		
		clearButton.setBackground(Color.white);
		clearButton.addActionListener(e -> mediator.clear());
		panel.add(clearButton);
		
		runButton.setBackground(Color.white);
		runButton.addActionListener(e -> mediator.toggleAutoStep());
		panel.add(runButton);
		
		reloadButton.setBackground(Color.white);
		reloadButton.addActionListener(e -> mediator.reload());
		panel.add(reloadButton);
		
		JSlider slider = new JSlider(5,1000);
		slider.addChangeListener(e -> mediator.setPeriod(slider.getValue())); 
		// put a void method setPeriod(int value) in Mediator, we will complete it later
		panel.add(slider);
		
		return panel;
	}

	// @Override
	public void update() {
		System.out.println("here");
		System.out.println(mediator.getCurrentState());
		runButton.setEnabled(mediator.getCurrentState().getRunPauseActive());
		stepButton.setEnabled(mediator.getCurrentState().getStepActive());
		clearButton.setEnabled(mediator.getCurrentState().getClearActive());
		reloadButton.setEnabled(mediator.getCurrentState().getReloadActive());		
	}
}
