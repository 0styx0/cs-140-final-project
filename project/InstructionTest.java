package project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InstructionTest {

	@Test
	void testCorrectCheckParity() {

		var instr = new Instruction((byte) 0111, 0);

		assertThrows(ParityCheckException.class, () -> Instruction.checkParity(instr));
	}

	@Test
	void testCorrectOddParityPasses() {

		var instr = new Instruction((byte) 0101, 0);

		Instruction.checkParity(instr);
	}
}
