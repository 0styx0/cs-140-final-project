package projectview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import project.Instruction;
import project.Loader;
import project.Machine;
import project.Memory;

class CodeViewPanel {

	Machine machine;
	Instruction instr;
	JScrollPane scroller;
	JTextField[] codeText = new JTextField[Memory.CODE_SIZE];
	JTextField[] codeBinHex = new JTextField[Memory.CODE_SIZE];
	int previousColor = -1;

	public CodeViewPanel(Machine m) {

		this.machine = m;
	}

	public static void main(String[] args) throws Exception {
		Machine machine = new Machine(()->System.exit(0));
		CodeViewPanel panel = new CodeViewPanel(machine);
		JFrame frame = new JFrame("TEST");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 700);
		frame.setLocationRelativeTo(null);
		frame.add(panel.createCodeDisplay());
		frame.setVisible(true);
		System.out.println(Loader.load(machine, new File("factorial.pexe")));
		panel.update("Load Code");
	}
	public void update(String arg) {
		if("Load Code".equals(arg)) {
			for(int i = 0; i < machine.getProgramSize(); i++) {
				instr = machine.getCode(i);
				codeText[i].setText(instr.getText());
				codeBinHex[i].setText(instr.getBinHex());
			}	
			previousColor = machine.getPC();			
			codeBinHex[previousColor].setBackground(Color.YELLOW);
			codeText[previousColor].setBackground(Color.YELLOW);
		} else if("Clear".equals(arg)) {
			for(int i = 0; i < Memory.CODE_SIZE; i++) {
				codeText[i].setText("");
				codeBinHex[i].setText("");
			}	
			if(previousColor >= 0 && previousColor < Memory.CODE_SIZE) {
				codeText[previousColor].setBackground(Color.WHITE);
				codeBinHex[previousColor].setBackground(Color.WHITE);
			}
			previousColor = -1;
		}		
		if(this.previousColor >= 0 && previousColor < Memory.CODE_SIZE) {
			codeText[previousColor].setBackground(Color.WHITE);
			codeBinHex[previousColor].setBackground(Color.WHITE);
		}
		previousColor = machine.getPC();
		if(this.previousColor >= 0 && previousColor < Memory.CODE_SIZE) {
			codeText[previousColor].setBackground(Color.YELLOW);
			codeBinHex[previousColor].setBackground(Color.YELLOW);
		} 
		if(scroller != null && instr != null && machine!= null) {
			JScrollBar bar= scroller.getVerticalScrollBar();
			int pc = machine.getPC();
			if(pc >= 0 && pc < Memory.CODE_SIZE && codeBinHex[pc] != null) { 
				Rectangle bounds = codeBinHex[pc].getBounds();
				bar.setValue(Math.max(0, bounds.y - 15*bounds.height));
			}
		}
	}
	public JComponent createCodeDisplay() {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				"Code Memory View",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		panel.setBorder(border);

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());


		JPanel numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0,1));
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(0,1));
		JPanel hexPanel = new JPanel();
		hexPanel.setLayout(new GridLayout(0,1));

		innerPanel.add(numPanel, BorderLayout.LINE_START);
		innerPanel.add(textPanel, BorderLayout.CENTER);
		innerPanel.add(hexPanel, BorderLayout.LINE_END);


		for(int i = 0; i < Memory.CODE_SIZE; i++) {
			numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
			codeText[i] = new JTextField(10);
			codeBinHex[i] = new JTextField(12);
			textPanel.add(codeText[i]);
			hexPanel.add(codeBinHex[i]);
		}


		// textPanel is added to innerPanel at BorderLayout.CENTER and hexPanel is added at BorderLayout.LINE_END
		innerPanel.add(BorderLayout.CENTER, textPanel);
		innerPanel.add(BorderLayout.LINE_END, hexPanel);

		scroller = new JScrollPane(innerPanel);
		panel.add(scroller);
		return panel;
	}
}
