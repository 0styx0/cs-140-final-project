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
	private IOUnit ioUnit;
	private MenuBarBuilder menuBuilder;


	public void step() {
		if (currentState != States.PROGRAM_HALTED &&
				currentState != States.NOTHING_LOADED) {
			try {
				machine.step();
			} catch (CodeAccessException e) {
				JOptionPane.showMessageDialog(frame,
						"Illegal access to code from line " + model.getPC() + "\n"
						+ "Exception message: " + e.getMessage(),
						"Run time error",
						JOptionPane.OK_OPTION);
				System.out.println("Illegal access to code from line " + model.getPC()); // just for debugging
				System.out.println("Exception message: " + e.getMessage()); // just for debugging
			} catch(ArrayIndexOutOfBoundsException e) {
				// similar JOPtionPane
				// YOU HAVE TO FILL OUT ALL THESE CATCH BLOCKS WITH DIFFERENT MESSAGES
			} catch(NullPointerException e) {
				// similar JOPtionPane
			} catch(ParityCheckException e) {
				// similar JOPtionPane
			} catch(IllegalInstructionException e) {
				// similar JOPtionPane
			} catch(IllegalArgumentException e) {
				// similar JOPtionPane
			} catch(DivideByZeroException e) {
				// similar JOPtionPane
			}
			notify("");
				}
	}

	public void execute() {

			while (currentState != States.PROGRAM_HALTED &&
					currentState != States.NOTHING_LOADED) {
				try {
					machine.step();
				} catch (CodeAccessException e) {
					JOptionPane.showMessageDialog(frame,
							"Illegal access to code from line " + model.getPC() + "\n"
							+ "Exception message: " + e.getMessage(),
							"Run time error",
							JOptionPane.OK_OPTION);
					System.out.println("Illegal access to code from line " + model.getPC()); // just for debugging
					System.out.println("Exception message: " + e.getMessage()); // just for debugging
				} catch(ArrayIndexOutOfBoundsException e) {
					// similar JOPtionPane
					// YOU HAVE TO FILL OUT ALL THESE CATCH BLOCKS WITH DIFFERENT MESSAGES
				} catch(NullPointerException e) {
					// similar JOPtionPane
				} catch(ParityCheckException e) {
					// similar JOPtionPane
				} catch(IllegalInstructionException e) {
					// similar JOPtionPane
				} catch(IllegalArgumentException e) {
					// similar JOPtionPane
				} catch(DivideByZeroException e) {
					// similar JOPtionPane
				}
					}
			notify("");
	}
	public Machine getMachine() {
		return this.machine;
	}

	public void setMachine(Machine temp) {
		this.machine = temp;
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public void toggleAutoStep() {
		tUnit.toggleAutoStep();

		if (tUnit.isAutoStepOn()) {
			setCurrentState(States.AUTO_STEPPING);
		} else {
			setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		}
	}

	public void reload() {
		tUnit.setAutoStepOn(false);
		clear();
		ioUnit.finalLoad_ReloadStep();
	}

	public void assembleFile() {

		ioUnit.assembleFile();
	}

	public void loadFile() {
		ioUnit.loadFile();
	}

	public void setPeriod(int value) {
		tUnit.setPeriod(value);
	}

	public void update() {

	}

	private void notify(String str) {
		codeViewPanel.update(str);
	}

	public void clear() {
		machine.clear();
		setCurrentState(States.NOTHING_LOADED);
		currentState.enter();
		notify("Clear");
	}

	public void makeReady(String s) {
		tUnit.setAutoStepOn(false);
		setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
		currentState.enter();
		notify(s);
	}

	public void exit() { // method executed when user exits the program
		int decision = JOptionPane.showConfirmDialog(
				frame, "Do you really wish to exit?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		if (decision == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
