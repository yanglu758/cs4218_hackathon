package sg.edu.nus.comp.cs4218.impl.cmd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.ShellImpl;
import sg.edu.nus.comp.cs4218.impl.ShellStub;

public class CallCommandTest {

	private static final String ARGS_ARRAY = "argsArray";
	private static final String APP = "app";
	private static final String ECHO = "echo";
	private static final String DEF = "def";
	private static final String ABC = "abc";
	private String expected, cmdLine;

	@Before
	public void setUp() throws Exception {
		cmdLine = expected = "";
	}

	@Test
	public void testParseAppName() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseAppNameWithOneSpace() throws ShellException, AbstractApplicationException {
		cmdLine = " echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseAppNameWithMultipleSpaces() throws ShellException, AbstractApplicationException {
		cmdLine = "    echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseAppNameWithOneTab() throws ShellException, AbstractApplicationException {
		cmdLine = "\techo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseAppNameWithMultipleTabs() throws ShellException, AbstractApplicationException {
		cmdLine = "\t\t\techo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseAppNameWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "  \t\t   \t  echo abc";
		expected = ECHO;
		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();
		assertEquals(expected, (String) Whitebox.getInternalState(callCommand, APP));
	}

	@Test
	public void testParseArgs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo abc def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

	@Test
	public void testParseArgsWithMultipleSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "echo  abc     def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

	@Test
	public void testParseArgsWithOneTab() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\tabc\tdef";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

	@Test
	public void testParseArgsWithMultipleTabs() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\t\tabc\t\t\tdef";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

	@Test
	public void testParseArgsWithMultipleTabsSpace() throws ShellException, AbstractApplicationException {
		cmdLine = "echo\t    \tabc   \t\t\t     def";
		String[] expectedArgs = { ABC, DEF };

		CallCommand callCommand = new CallCommand(new ShellImpl(), cmdLine);
		callCommand.parse();

		assertArrayEquals(expectedArgs, (String[]) Whitebox.getInternalState(callCommand, ARGS_ARRAY));
	}

}