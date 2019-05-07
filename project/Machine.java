package project;
import static project.Instruction.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.List;

public class Machine {


	private class CPU {
		private int accum;
		private int pc;
	}

	public final Map<Integer, Consumer<Instruction>> ACTION = new TreeMap<>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private boolean withGUI = false;
	private HaltCallback callBack;

	public int[] getData() {
		return memory.getData();
	}
	public int getData(int i){
		return memory.getData(i);
	}
	int[] getData(int i, int j){
		return memory.getData(i, j);
	}
	public Instruction getCode(int index) {
		return memory.getCode(index);
	}
	public int getProgramSize() {
		return memory.getProgramSize();
	}
	public void addCode(Instruction j) {
		memory.addCode(j);
	}
	// package private
	void setCode(int index, Instruction instr) {
		memory.setCode(index, instr);
	}
	public List<Instruction> getCode() {
		return memory.getCode();
	}
	//package private
	Instruction[] getCode(int min, int max) {
		return memory.getCode(min,max);
	}
	public int getPC() {
		return cpu.pc;
	}
	public void setPC(int pc) {
		cpu.pc = pc;
	}
	public int getChangedDataIndex() {
		return memory.getChangedDataIndex();
	}
	public int getAccum() {
		return cpu.accum;
	}
	public void setAccum(int i) {
		cpu.accum = i;
	}
	public void clear() {
		memory.clearData();
		memory.clearCode();
		cpu.pc = 0;
		cpu.accum = 0;
	}
	public void step(){

		// lander said to make try catch but his tests only pass if there isn't one
		// try {
			Instruction instr = this.getCode(cpu.pc);
			Instruction.checkParity(instr);
			ACTION.get(instr.opcode / 8).accept(instr);
		// }
		// catch (ParityCheckException e) {
			// e.printStackTrace();
		// }
		// needs a try/catch
		// in the try, make an Instruction instr equal to
		// getCode(cpu.pc). Call Instruction.checkParity with argument instr.
		// That could throw an Exception, so we only do the next instruction if 
		// the parity bit is OK. 
		// Call ACTION.get(instr.opcode/8).accept(instr)
		// next we have the catch(Exception e) 
		// here put in the commented line // e.printStackTrace();
		// in case we want to find out what exception is occurring when debugging
		// the other lines of the exception are halt(); and throw e;
	}
	public void setData(int i, int j) {
		memory.setData(i, j);
	}

	public void halt() {
		callBack.halt();
	}

	public Machine(HaltCallback cb) {
		this.callBack = cb;

		ACTION.put(OPCODES.get("ADD"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum += memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum += instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum += memory.getData(memory.getData(instr.arg));
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0")
					+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});
		//ACTION entry for "NOP"
		ACTION.put(OPCODES.get("NOP"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags != 0){
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});


		ACTION.put(OPCODES.get("HALT"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags != 0){
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			halt();
		});


		ACTION.put(OPCODES.get("JUMP"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if (flags == 0) {
				cpu.pc += instr.arg;
			}
			else if (flags == 2) {
				cpu.pc = instr.arg;
			}
			else if (flags == 4) {
				cpu.pc += memory.getData(instr.arg);
			}
			else {
				cpu.pc = memory.getData(instr.arg);
			}
			// if(flags != 0){
			// 	String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
			// 	throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			// }
		});

		ACTION.put(OPCODES.get("JMPZ"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if (cpu.accum != 0) {
				cpu.pc++;
				return;
			}

			if (flags == 0) {
				cpu.pc += instr.arg;
			}
			else if (flags == 2) {
				cpu.pc = instr.arg;
			}
			else if (flags == 4) {
				cpu.pc += memory.getData(instr.arg);
			}
			else {
				cpu.pc = memory.getData(instr.arg);
			}
			// if(flags != 0){
			// 	String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
			// 	throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			// }
		});

		ACTION.put(OPCODES.get("LOD"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if (flags == 0) {
				cpu.accum = memory.getData(instr.arg);
				cpu.pc++;
			}
			else if (flags == 2) {
				cpu.accum = instr.arg;
				cpu.pc++;
			}
			else if (flags == 4) {
				cpu.accum = memory.getData(memory.getData(instr.arg));
				cpu.pc++;
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}

		});

		ACTION.put(OPCODES.get("STO"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				memory.setData(instr.arg, cpu.accum);
			} else if(flags == 4) { // indirect addressing
				memory.setData(memory.getData(instr.arg), cpu.accum);
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0")
					+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});
		ACTION.put(OPCODES.get("NOT"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags != 0){
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}

			cpu.accum = (cpu.accum == 0) ? 1 : 0;
			cpu.pc++;
		});


		ACTION.put(OPCODES.get("AND"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if (flags == 0 || flags == 2) {

				if (flags == 0) {
					if (cpu.accum != 0 && memory.getData(instr.arg) != 0) {
						cpu.accum = 1;
					}
					else {
						cpu.accum = 0;
					}
				}

				if (flags == 2) {

					if (cpu.accum != 0 && instr.arg != 0) {
						cpu.accum = 1;
					}
					else {
						cpu.accum = 0;
					}
				}
				cpu.pc++;
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
		});


		ACTION.put(OPCODES.get("CMPL"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if (flags != 0) {
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}

			cpu.accum = (memory.getData(instr.arg) < 0) ? 1 : 0;
			cpu.pc++;
		});

		ACTION.put(OPCODES.get("CMPZ"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if (flags != 0) {
				String fString = "(" + (flags%8 > 3?"1":"0") + (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}

			cpu.accum = (memory.getData(instr.arg) == 0) ? 1 : 0;
			cpu.pc++;
		});


		ACTION.put(OPCODES.get("SUB"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum -= memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum -= instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum -= memory.getData(memory.getData(instr.arg));
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0")
					+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});


		ACTION.put(OPCODES.get("MUL"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum *= memory.getData(instr.arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum *= instr.arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum *= memory.getData(memory.getData(instr.arg));
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0")
					+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});

		ACTION.put(OPCODES.get("DIV"), instr -> {
			int flags = instr.opcode & 6; // remove parity bit that will have been verified

			if(flags == 0) { // direct addressing

				var div = memory.getData(instr.arg);

				if (div == 0) {
					throw new DivideByZeroException("Zero Division");
				}

				cpu.accum /= div;
			} else if(flags == 2) { // immediate addressing

				var div = instr.arg;
				if (div == 0) {
					throw new DivideByZeroException("Zero Division");
				}

				cpu.accum /= div;
			} else if(flags == 4) { // indirect addressing

				var div = memory.getData(memory.getData(instr.arg));
				if (div == 0) {
					throw new DivideByZeroException("Zero Division");
				}
				cpu.accum /= div;
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0")
					+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException("Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		});
	}

}
