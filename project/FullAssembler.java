package project;
import java.util.ArrayList;
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
import java.util.Scanner;

public class FullAssembler implements Assembler {

	public static void assemble(String inputFileName, String outputFileName, StringBuilder error) {

		if(error == null) throw new IllegalArgumentException("Coding error: the error buffer is null");

		try {
			ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(inputFileName)));


			boolean blankLineFound = false;
			boolean blankErrorFound = false;
			int blankLineNum = -1;
			boolean readingCode = true;

			for (int i = 0; i < lines.size(); i++) {

				String line = lines.get(i);
				int currentLineNum = i + 1;
				boolean currentLineIsBlank = line.trim().length() == 0;

				if (blankLineFound && !currentLineIsBlank && !blankErrorFound) {
					error.append("\nIllegal blank line in the source file: " blankLineNum);
					blankErrorFound = true;
				}

				if (currentLineIsBlank) {
					blankLineFound = true;
					blankLineNum = currentLineNum;
				}

				if (!currentLineIsBlank && (line.charAt(0) == ' ' || line.charAt(0) == '\t')) {
					error.append("\nLine starts with illegal white space: " + currentLineNum);
				}

				boolean moreLinesToCome = lines.size() > i;
				// if found data, set readingCode to false
				// if run into "DATA" when readingCode is false, error duplicate data delim
				if (line.trim().toUpperCase().equals("DATA") && !moreLinesToCome) {
					error.append("DATA but no more lines in file");
					// throw new Exception("DATA but no more lines in file");
				}
				if (!readingCode && line.trim().equals("DATA")) {
					error.append("Duplicate DATA");
				}
				if (line.trim().toUpperCase().equals("DATA") && !line.trim().equals("DATA")) {
					error.append("\nLine does not have DATA in upper case: " + currentLineNum);
				}

				if (line.trim().equals("DATA")) {
					readingCode = false;
				}

				if (readingCode) {

					String[] parts = line.trim().split("\\s+");
					String upperLine = line.trim().toUpperCase();

					if (Instruction.OPCODES.keySet().contains(parts[0])) {
						throw new Exception("parts[0] must be contained in opcodes.keySet()");
					}

					for (String value : Instruction.MNEMONICS.values()) {

						if (upperLine.equals(value)) {

							if (!line.equals(value)) {
								error.append("\nError on line " + (i+1) + ": mnemonic must be upper case");
							}

							if (noArgument.contains(line)) {

								if (parts.length != 1) {
									error.append("\nError on line " + (i+1) + ": this mnemonic cannot take arguments");
									// throw new Exception("parts must have length 1");
								}
							} else {

								if (parts.length != 2) {
									error.append("\nError on line " + (i+1) + ": this mnemonic is missing an argument");
									// throw new Exception("parts must have length 2");
								}

								try {
									// work through the cases we saw in the makeCode method of SimpleAssembler
									int flags = 0;
									if (parts[1].charAt(0) == 'M') {
										flags = 2;
										parts[1] = parts[1].substring(1);
									}
									else if (parts[1].charAt(0) == 'N') {
										flags = 4;
										parts[1] = parts[1].substring(1);
									}
									else if (parts[1].charAt(0) == 'J') {
										flags = 6;
										parts[1] = parts[1].substring(1);
									}
									int arg = Integer.parseInt(parts[1],16);
									//.. the rest of setting up the opPart
									int opPart = 8*Instruction.OPCODES.get(parts[0]) + flags;
									opPart+=Instruction.numOnes(opPart)%2;

								} catch(NumberFormatException e) {
									error.append("\nError on line " + (i+1) +
											": argument is not a hex number");
									retVal = i + 1;
								} catch(NumberFormatException e) {
									error.append("\nError on line " + (i+1) + // he has offset +
											": data has non-numeric memory address");
									retVal = i + 1; // he has offset + i + 1, idk what for
								}

							}
						}
					}

				}
			}
		}
		catch (FileNotFoundException e) {
			error.append("\nUnable to open the source file");
			return -1;
			// Handle a potential exception
		}
		catch (FileNotFoundException e) {
			error.append("\nError: Unable to write the assembled program to the output file");
			retVal = -1;
		} catch (IOException e) {
			error.append("\nUnexplained IO Exception");
			retVal = -1;
		}

		SimpleAssembler.assemble(String inputFileName, String outputFileName, StringBuilder error
	}

}
