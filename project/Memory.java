package project;
import java.util.Arrays;

class Memory {

	public static final int DATA_SIZE = 512;
	private int[] data = new int[Memory.DATA_SIZE];

	int[] getData(int min, int max) {

		return Arrays.copyOfRange(data, min, max);
	}

	int[] getData() {
		return this.data;
	}

	int getData(int index) {

		return this.data[index];
	}

	void setData(int index, int value) {

		if (index < 0 || index > Memory.DATA_SIZE - 1) {
			throw new DataAccessException("Off limit");
		}

		this.data[index] = value;
	}

	void clearData() {

		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = 0;
		}
	}
}
