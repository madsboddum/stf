package dk.madsboddum.stf;

import java.util.Arrays;

public class Application {
	public static void main(String[] args) {
		Package p = Application.class.getPackage();
		String version = p.getImplementationVersion();
		
		if (version == null) {
			version = "DEVELOPMENT VERSION";	// If we're not running from a JAR, then we're in development
		}

		CLI cli = new CLI(version, System.out, System.err);

		int exitCode = cli.execute(Arrays.asList(args));
		System.exit(exitCode);
	}
}
