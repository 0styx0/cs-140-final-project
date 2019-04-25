package project;

import java.util.Set;


public interface Assembler{
	Set<String> noArgument = Set.of("HALT", "NOP", "NOT");

	int assemble(String inputFileName, String outputFileName, StringBuilder error);

}
