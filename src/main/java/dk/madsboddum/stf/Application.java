package dk.madsboddum.stf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;

public class Application {
	public static void main(String[] args) {
		Package p = Application.class.getPackage();
		String version = p.getImplementationVersion();
		
		if (version == null) {
			version = "DEVELOPMENT VERSION";	// If we're not running from a JAR, then we're in development
		}

		CLI cli = new CLI(version, System.out, System.err, (String path) -> {
			File file = new File(path);

			InputStream stream;
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			return new StringTableStream(Paths.get(path).getFileName().toString(), stream);
		});

		int exitCode = cli.execute(Arrays.asList(args));
		System.exit(exitCode);
	}
}
