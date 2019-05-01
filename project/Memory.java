package project;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class Memory {

	public static final int DATA_SIZE = 512;
	private int[] data = new int[Memory.DATA_SIZE];
	public final int CODE_SIZE = 256;
	private final List<Instruction> code = new ArrayList<>();
	private int changedDataIndex = -1;

	int[] getData(int min, int max) {

		return Arrays.copyOfRange(data, min, max);
	}

	int[] getData() {
		return this.data;
	}

	int getData(int index) {

		return this.data[index];
	}

	int getChangedDataIndex() {
		return this.changedDataIndex;
	}

	List<Instruction> getCode() {
		return this.code;
	}

	void setData(int index, int value) {

		if (index < 0 || index > Memory.DATA_SIZE - 1) {
			throw new DataAccessException("Off limit");
		}

		this.changedDataIndex = index;

		this.data[index] = value;
	}

	void clearData() {

		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = 0;
		}

		this.changedDataIndex = -1;
	}

	Instruction getCode(int index) {

		System.out.println(index);
		System.out.println(code.size());
		// 0 <= index < code.size()
		if (0 <= index && index < code.size()) {
			return this.code.get(index);
		}

		throw new CodeAccessException("Illegal access to code");
	}

	public Instruction[] getCode(int min, int max) {

		if (!(0 <= min && min <= max && max < code.size())) {
			throw new CodeAccessException("Illegal access to code");
		}

		Instruction[] temp = {};
		temp = code.toArray(temp);
		return Arrays.copyOfRange(temp, min, max);
	}

	void addCode(Instruction value) {

		if (code.size() < CODE_SIZE) {
			code.add(value);
		}
	}

	void setCode(int index, Instruction instr) {

		if (!(0 <= index && index < code.size())) {
			throw new CodeAccessException("Illegal access to code");
		}

		// System.out.println(index + " " + instr.opcode + " : " + instr.arg);
		code.set(index, instr);
	}

	void clearCode() {
		code.clear();
	}

	int getProgramSize() {

		return code.size();
	}
}
