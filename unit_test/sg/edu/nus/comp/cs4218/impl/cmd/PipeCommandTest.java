package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.cmd.PipeCommand;

public class PipeCommandTest {

	PipeCommand pipeCommand;
	Vector<String> expected,actual;
	
	@Before
	public void setup() {
		pipeCommand = new PipeCommand(); 
		expected = new Vector<String>();
		actual = new Vector<String>();
	}
	
	@Test
	public void testPipeTwoCommandsNoArgs() {
		pipeCommand = new PipeCommand("");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}		
		assertEquals(pipeCommand.argsArray.size(), 0);
		assertEquals(pipeCommand.argsArray, expected);
	}
	
	@Test
	public void testPipeTwoCommandsWithValidArgs() {
		pipeCommand = new PipeCommand("cat text.txt | sed s/pains/pain/");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}		
		
		expected.add("cat text.txt ");
		expected.add(" sed s/pains/pain/");

		assertEquals(pipeCommand.argsArray.size(), 2);
		assertEquals(pipeCommand.argsArray, expected);
	}
	
	@Test
	public void testPipeMultipleCommandsWithValidArgs() {
		pipeCommand = new PipeCommand("cat text.txt | sed s/pains/pain/ | sed s/pleasures/pleasuring/");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}		
		
		expected.add("cat text.txt ");
		expected.add(" sed s/pains/pain/ ");
		expected.add(" sed s/pleasures/pleasuring/");

		assertEquals(pipeCommand.argsArray.size(), 3);
		assertEquals(pipeCommand.argsArray, expected);
	}
	
	@Test
	public void testPipeTwoCommandsWithValidArgsDQ() {
		pipeCommand = new PipeCommand("cat \"text.txt\" | sed s/pains/paining/");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}		
		
		expected.add("cat \"text.txt\" ");
		expected.add(" sed s/pains/paining/");

		assertEquals(pipeCommand.argsArray.size(), 2);
		assertEquals(pipeCommand.argsArray, expected);
	}
	
	@Test
	public void testPipeTwoCommandsWithValidArgsBQ() {
		pipeCommand = new PipeCommand("cat `echo text.txt` | sed s/pains/paining/");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}		
		
		expected.add("cat `echo text.txt` ");
		expected.add(" sed s/pains/paining/");

		assertEquals(pipeCommand.argsArray.size(), 2);
		assertEquals(pipeCommand.argsArray, expected);
	}
	
	@Test
	public void testPipeTwoCommandsWithValidArgsSQ() {
		pipeCommand = new PipeCommand("cat 'text.txt' | sed s/pains/paining/");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 e.printStackTrace();
		}		
		
		expected.add("cat 'text.txt' ");
		expected.add(" sed s/pains/paining/");

		assertEquals(pipeCommand.argsArray.size(), 2);
		assertEquals(pipeCommand.argsArray, expected);
	}
	
	
	@Test
	public void testInvalidPipeOperatorsFront() {
		String expected = "shell: Invalid pipe operator/s";
		String actual = "";
		pipeCommand = new PipeCommand("| cat text.txt | sed s/pains/paining/");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 actual = e.getMessage();
		}		
		
		assertEquals(actual, expected); 
		
	}
	 
	@Test
	public void testInvalidPipeOperatorsBack() {
		String expected = "shell: Invalid pipe operator/s";
		String actual = "";
		pipeCommand = new PipeCommand("cat text.txt | sed s/pains/paining/ |");
		try {
			pipeCommand.parse();
		} catch (ShellException e) {
			 actual = e.getMessage();
		}		
		
		assertEquals(actual, expected);
		
	}
	
}
