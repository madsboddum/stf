package dk.madsboddum.stf

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

import java.util.function.Function

class CLI {
	private final String version;
	private final OutputStream out
	private final OutputStream err
	private final Function<String, StringTableStream> streamCreator
	
	CLI(String version, OutputStream out, OutputStream err, Function<String, StringTableStream> streamCreator) {
		this.version = version;
		this.out = out
		this.err = err
		this.streamCreator = streamCreator
	}
	
	def execute(List<String> args) {
		def options = new Options()
		
		// Options
		def print = new Option("p", "print", false, "print entries in the STF file specified by --input")
		print.setRequired(false)
		options.addOption(print)
		
		def input = new Option("i", "input", true, "specifies STF file(s) to work on")
		input.setRequired(false)
		input.setArgName("stf files")
		input.setArgs(Option.UNLIMITED_VALUES);
		input.setValueSeparator(',' as char)
		options.addOption(input)
		
		def help = new Option("h", "help", false, "lists possible options (what you're seeing now)")
		help.setRequired(false)
		options.addOption(help)
		
		def version = new Option("v", "version", false, "displays version of the tool")
		version.setRequired(false)
		options.addOption(version)
		
		def parser = new DefaultParser()
		def cmd
		
		try {
			cmd = parser.parse(options, args.toArray() as String[], true)
		} catch (ParseException e) {
			// Invalid syntax
			def pw = new PrintWriter(out)
			pw.println(e.getMessage())
			pw.close()
			printHelp(err, options)	// Let's try to help the user along
			
			return 1
		}
		
		if (cmd.hasOption("input")) {
			def inputValues = cmd.getOptionValues("input")
			
			def pw = new PrintWriter(out)
			
			for (def inputValue : inputValues) {
				def stringTableStreams = streamCreator.apply(inputValue)
				
				for (def stringTableStream : stringTableStreams) {
					def inputStream = stringTableStream.getInputStream()
					
					try {
						def modelProvider = new ModelProvider(inputStream)
						def stringTable = modelProvider.get()
						
						if (cmd.hasOption("print")) {
							def map = stringTable.getMap()
							
							
							map.forEach((key, value) -> {
								def stringTableName = stringTableStream.getName()
								
								pw.println("@" + stringTableName + ":" + key + "|" + value)    // Example: @base_player:attribmod_apply|You suddenly feel different.
							})
						}
					} finally {
						inputStream.close()	// Release stream when we're done with it
					}
				}
			}
			
			pw.close()
			
			return 0
		} else if (cmd.hasOption("version")) {
			// Print message with the version
			def pw = new PrintWriter(out)
			pw.println("stf version " + this.version)	// TODO a bit ugly, but "version" exists in two scopes with different meanings
			pw.close()
			
			return 0
		} else if (cmd.hasOption("help")) {
			// They explicitly asked for help
			printHelp(out, options)
			return 0
		}
		
		return 1
	}
	
	private static def printHelp(OutputStream out, Options options) {
		def formatter = new HelpFormatter()
		def writer = new PrintWriter(out)
		formatter.printHelp(writer, formatter.getWidth(), "stf", null, options, formatter.getLeftPadding(), formatter.getDescPadding(), null, false)
		writer.flush()
	}
}
