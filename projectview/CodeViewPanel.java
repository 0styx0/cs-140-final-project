package projectview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import project.Instruction;
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

	public JComponent createCodeDisplay() {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK),
				"Code Memory View ["+ lower +"-"+ upper +"]",
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

		for (int i = 0; i < Memory.CODE_SIZE; i++) {

			codeBinHex[i] = new JTextField(12);
			codeText[i] = new JTextField(10);

			numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
			codeText[i - lower] = new JTextField(10);
			codeBinHex[i-lower] = new JTextField(10);
			textPanel.add(codeText[i]);
			hexPanel.add(codeBinHex[i]);
		}

		// textPanel is added to innerPanel at BorderLayout.CENTER and hexPanel is added at BorderLayout.LINE_END
		innerPanel.set(BorderLayout.CENTER, textPanel);
		innerPanel.set(BorderLayout.END, hexPanel);

		scroller = new JScrollPane(innerPanel);
		panel.add(scroller);
		return panel;
	}
}
