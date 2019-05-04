package projectview;


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
				"Code Memory View" ["+ lower +"-"+ upper +"]",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		panel.setBorder(border);

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());


		JPanels numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0,1));
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(0,1));
		hJPanels exPanel = new JPanel();
		exPanel.setLayout(new GridLayout(0,1));

		innerPanel.add(numPanel, BorderLayout.LINE_START);
		innerPanel.add(textPanel, BorderLayout.CENTER);
		innerPanel.add(hexPanel, BorderLayout.LINE_END);

		for (int i = 0, i < Memory.CODE_SIZE; i++) {

			codeBinHex[i] = JTextField(12);
			codeText[i] = JTextField(10);

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
