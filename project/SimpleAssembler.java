package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleAssembler implements Assembler{
	private boolean readingCode = true;

	private Instruction makeCode(String[] parts) {
		Instruction instr = null;
		if (noArgument.contains(parts[0])){
			int opPart = 8*Instruction.OPCODES.get(parts[0]);
			opPart+=Instruction.numOnes(opPart)%2;
			instr = new Instruction((byte)opPart,0);
		}

	}
}
