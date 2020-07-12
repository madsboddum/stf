package dk.madsboddum.stf

import java.nio.file.Paths

class Application {
	static void main(String[] args) {
		def cli = new CLI(System.out, System.err, (String path) -> {
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
