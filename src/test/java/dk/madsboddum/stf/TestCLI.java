package dk.madsboddum.stf;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
			CLI cli = new CLI("1.2.3", out, err, (path) -> new StringTableStream("test", new ByteArrayInputStream(new byte[0])));
			int actualExitCode = cli.execute(List.of("--print"));
			int expectedExitCode = 1;
			
			assertEquals(expectedExitCode, actualExitCode, "If input are not specified, then the application should fail");
		}
		
		@Test
		void testWithInput() {
			OutputStream out = new ByteArrayOutputStream();
			OutputStream err = new ByteArrayOutputStream();
			CLI cli = new CLI("1.2.3", out, err, (String path) -> new StringTableStream("base_player.stf", TestCLI.class.getResourceAsStream(path)));
			int actualExitCode = cli.execute(List.of("--print", "base_player.stf"));
			int expectedExitCode = 0;
			
			assertEquals(expectedExitCode, actualExitCode, "If input files are specified, then the application should run successfully");
		}
	}
	
	@Nested
	class Version {
		
		@Test
		void testMessageContainsVersion() {
			String version = "1.2.3";
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CLI cli = new CLI(version, out, System.err, null);
			
			cli.execute(List.of("--version"));
			String message = out.toString(StandardCharsets.UTF_8);
			
			assertTrue(message.contains(version), "The printed message should contain the version. Message was: $message");
		}
	}
	
}
