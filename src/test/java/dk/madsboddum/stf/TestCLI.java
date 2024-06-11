package dk.madsboddum.stf;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCLI {
	
	@Nested
	class Print {
		
		@Test
		void testWithoutInput() {
			OutputStream out = new ByteArrayOutputStream();
			OutputStream err = new ByteArrayOutputStream();
			CLI cli = new CLI("1.2.3", out, err);
			int actualExitCode = cli.execute(Collections.emptyList());
			int expectedExitCode = 1;
			
			assertEquals(expectedExitCode, actualExitCode, "If input is not specified, then the application should fail");
		}
		
		@Test
		void testWithSingleFile() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayOutputStream err = new ByteArrayOutputStream();
			CLI cli = new CLI("1.2.3", out, err);
			int actualExitCode = cli.execute(List.of(TestCLI.class.getResource("base_player.stf").getFile()));
			int expectedExitCode = 0;
			
			assertEquals(expectedExitCode, actualExitCode, err.toString(StandardCharsets.UTF_8));
		}
		
		@Test
		void testWithMultipleFiles() {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayOutputStream err = new ByteArrayOutputStream();
			CLI cli = new CLI("1.2.3", out, err);
			int actualExitCode = cli.execute(List.of(TestCLI.class.getResource("base_player.stf").getFile(), TestCLI.class.getResource("base_player2.stf").getFile()));
			int expectedExitCode = 0;
			
			assertEquals(expectedExitCode, actualExitCode, err.toString(StandardCharsets.UTF_8));
		}
	}
	
	@Nested
	class Version {
		
		@Test
		void testMessageContainsVersion() {
			String version = "1.2.3";
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CLI cli = new CLI(version, out, System.err);
			
			cli.execute(List.of("--version"));
			String message = out.toString(StandardCharsets.UTF_8);
			
			assertTrue(message.contains(version), "The printed message should contain the version. Message was: $message");
		}
	}
	
}
