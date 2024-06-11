package dk.madsboddum.stf

import java.nio.file.Paths

class Application {
	static void main(String[] args) {
		def p = getClass().getPackage()
		def version = p.getImplementationVersion() ?: "DEVELOPMENT VERSION"	// If we're not running from a JAR, then we're in development
		
		def cli = new CLI(version, System.out, System.err, (String path) -> {
			def workingDirectory = Paths.get(System.getProperty("user.dir"))	// user.dir is the directory from where the JVM was launched
			def file = new File(workingDirectory.toFile(), path)
			
			def stream = new FileInputStream(file)
			def stringTableStream = new StringTableStream(Paths.get(path).getFileName().toString(), stream)
			
			return stringTableStream
		})
		
		def exitCode = cli.execute(args.toList())
		
		System.exit(exitCode)
	}
}
