package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.commons.CommandString;

public class CallCommandIT {
	private static final String TEST_DIR = System.getProperty("user.dir") + File.separator + "test_system";
	private static final String LS_TEST_DIR = TEST_DIR + File.separator + "ls_test_system";
	private static final String CAT_TEST_DIR = TEST_DIR + File.separator + "cat_test_system";
	private static final String MKDIR_TEST_DIR = TEST_DIR + File.separator + "mkdir_test_system";
	private static final String PASTE_TEST_DIR = TEST_DIR + File.separator + "paste_test_system";
	private static final String DIFF_TEST_DIR = TEST_DIR + File.separator + "diff_test_system";
	private static final String CD_TEST_DIR = TEST_DIR + File.separator + "cd_test_system";
	private static final String SPLIT_TEST_DIR = TEST_DIR + File.separator + "split_test_system";
	private static final String CMP_TEST_DIR = TEST_DIR + File.separator + "cmp_test_system";
	private static final String GREP_TEST_DIR = TEST_DIR + File.separator + "grep_test_system";
	private static final String SED_TEST_DIR = TEST_DIR + File.separator + "sed_test_system";

	private CallCommand callCmd;
	private CommandString cmdLine;
	private String expected;
	private ByteArrayOutputStream output;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Environment.currentDirectory = TEST_DIR;
		cmdLine = new CommandString();
		expected = "";
		output = new ByteArrayOutputStream();
	}

	@Test
	public void testInvalidEmptyCmd() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("");
		callCmd = new CallCommand(new ShellImpl(), cmdLine);

		thrown.expect(ShellException.class);
		thrown.expectMessage("shell: : Invalid app");

		callCmd.parse();
		callCmd.evaluate(System.in, output);
	}

	@Test
	public void testAppNameWithOneSpace() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString(" echo abc");
		expected = "abc";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testAppNameWithMultipleSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("    echo abc");
		expected = "abc";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgs() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc def");
		expected = "abc def";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testArgsWithMultipleSpace() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo  abc     def");
		expected = "abc def";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateEcho() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo abc");
		expected = "abc";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCat() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = CAT_TEST_DIR;
		cmdLine = new CommandString("cat file1.txt");
		expected = "asdf";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateExit() throws ShellException, AbstractApplicationException {
		Thread thread1 = new Thread() {
			public void run() {
				CallCommand newCallCmd = new CallCommand(new ShellImpl(), new CommandString("exit"));
				try {
					newCallCmd.parse();
					newCallCmd.evaluate(System.in, new ByteArrayOutputStream());
				} catch (AbstractApplicationException | ShellException e) {
				}
			}
		};
		assertTrue(!thread1.isAlive());
	}

	@Test
	public void testEvaluateLs() throws ShellException, AbstractApplicationException {
		Environment.currentDirectory = LS_TEST_DIR;
		cmdLine = new CommandString("ls");
		expected = "'file name with space.txt'  file1.txt  file2.txt  'folder name with space'  folder1  folder2";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateMkDir() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = MKDIR_TEST_DIR;
		cmdLine = new CommandString("mkdir folder1");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		Path dirPath = Paths.get(Environment.currentDirectory).resolve("folder1");
		assertTrue(Files.exists(dirPath));
		assertTrue(Files.isDirectory(dirPath));
		Files.delete(dirPath);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluatePaste() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = PASTE_TEST_DIR;
		cmdLine = new CommandString("paste file1.txt file2.txt");
		expected = "asdfgh\tqwerty";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateDiff() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = DIFF_TEST_DIR;
		cmdLine = new CommandString("diff file1.txt file2.txt");
		expected = "< line2" + System.getProperty("line.separator") + "> line6";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCd() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CD_TEST_DIR;
		cmdLine = new CommandString("cd folder1");
		String directory = CD_TEST_DIR + File.separator + "folder1";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(directory, Environment.currentDirectory);
		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateSplit() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = SPLIT_TEST_DIR;
		cmdLine = new CommandString("split file1.txt -l 6");

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		BufferedReader reader = new BufferedReader(new FileReader(new File(Environment.currentDirectory + File.separator + "xaa")));
		assertEquals("asdfgh", reader.readLine());
		assertEquals("qwerty", reader.readLine());
		assertEquals("zxcvbn", reader.readLine());
		reader.close();
		Files.delete(Paths.get(Environment.currentDirectory + File.separator + "xaa"));

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateCmp() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = CMP_TEST_DIR;
		cmdLine = new CommandString("cmp file1.txt file2.txt -csl");

		expected = "Files differ";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateGrep() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = GREP_TEST_DIR;
		cmdLine = new CommandString("grep file file1.txt");

		expected = "line 1: This file is named 'file1'.\n" + 
				"line 2: It is a Text file.";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}

	@Test
	public void testEvaluateSed() throws ShellException, AbstractApplicationException, IOException {
		Environment.currentDirectory = SED_TEST_DIR;
		cmdLine = new CommandString("sed s/test/replaced/ sedTestFile1.txt");

		expected = "This is Sed Test File 1.\n" + 
				"1. replaced\n" + 
				"2. replaced test\n" + 
				"3. replaced test test\n" + 
				"4. replaced test test test\n" + 
				"5. replacedestestest\n" + 
				"6. replacedestestestestest";
		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testEvaluateWithGlob() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo *");
		expected = "cat_test_system cd_test_system cmd_test_system cmp_test_system "
				+ "diff_test_system glob_test_system grep_test_system ioRedir_test_system "
				+ "ls_test_system mkdir_test_system paste_test_system sed_test_system "
				+ "split_test_system";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}
	
	@Test
	public void testEvaluateWithCmdSub() throws ShellException, AbstractApplicationException {
		cmdLine = new CommandString("echo `echo test command substitution`");
		expected = "test command substitution";

		callCmd = new CallCommand(new ShellImpl(), cmdLine);
		callCmd.parse();
		callCmd.evaluate(System.in, output);

		assertEquals(expected, output.toString());
	}
}
