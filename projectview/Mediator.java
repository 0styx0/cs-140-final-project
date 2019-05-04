package projectview;
import project.HaltCallback;
import project.Machine;

import javax.swing.JFrame;

public class Mediator{
	private Machine machine;
	private JFrame frame;
	private TimerUnit tUnit;
	private CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	private ControlPanel controlPanel;
	private ProcessorViewPanel processorPanel;
	private States currentState;
	
	
	public void step() {}
	
	public Machine getMachine() {
		return this.machine;
	}
	
	public void setMachine(Machine temp) {
		this.machine = temp;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void clear() {};
	public void toggleAutoStep() {};
	public void reload() {};
	public void setPeriod(int value) {};
	public void update() {
		
	}
	
	private void notify(String str) {
		codeViewPanel.update(str);
	}
}
