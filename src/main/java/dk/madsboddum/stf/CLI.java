package dk.madsboddum.stf;

import org.apache.commons.cli.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CLI {
	private final String version;
	private final OutputStream out;
	private final OutputStream err;
	private final Function<String, StringTableStream> streamCreator;
	private final StringTableReader stringTableReader;
	
	CLI(String version, OutputStream out, OutputStream err, Function<String, StringTableStream> streamCreator) {
		this.version = version;
		this.out = out;
		this.err = err;
		this.streamCreator = streamCreator;
		this.stringTableReader = new StringTableReader();
	}
	
	public int execute(List<String> args) {
		try {
			Options options = new Options();

			// Options
			Option help = new Option("h", "help", false, "lists possible options (what you're seeing now)");
			help.setRequired(false);
			options.addOption(help);

			Option version = new Option("v", "version", false, "displays version of the tool");
			version.setRequired(false);
			options.addOption(version);

			CommandLineParser parser = new DefaultParser();
			CommandLine cmd;

			try {
				cmd = parser.parse(options, args.toArray(new String[0]), true);
			} catch (ParseException e) {
				// Invalid syntax
				PrintWriter pw = new PrintWriter(err);
				pw.println(e.getMessage());
				pw.close();
				printHelp(err, options);	// Let's try to help the user along

				return 1;
			}

			if (cmd.hasOption("version")) {
				// Print message with the version
				PrintWriter pw = new PrintWriter(out);
				pw.println("stf version " + this.version);	// TODO a bit ugly, but "version" exists in two scopes with different meanings
				pw.close();

				return 0;
			} else if (cmd.hasOption("help")) {
				// They explicitly asked for help
				printHelp(out, options);
				return 0;
			}

			List<String> inputValues = cmd.getArgList();

			if (inputValues.isEmpty()) {
				// Help them out
				printHelp(out, options);
				return 1;
			}
			
			PrintWriter pw = new PrintWriter(out);

			for (String inputValue : inputValues) {
				StringTableStream stringTableStream = streamCreator.apply(inputValue);

				try (InputStream inputStream = stringTableStream.getInputStream()) {
					Map<String, String> map = stringTableReader.read(inputStream);

					map.forEach((key, value) -> {
						String stringTableName = stringTableStream.getName();

						pw.println("@" + stringTableName + ":" + key + "|" + value);    // Example: @base_player:attribmod_apply|You suddenly feel different.
					});
				}
			}

			pw.close();

			return 0;
		} catch (Throwable t) {
			PrintWriter pw = new PrintWriter(err);
			pw.println("An unexpected error occurred: " + t.getMessage());
			pw.close();

			return 1;
		}
	}
	
	private static void printHelp(OutputStream out, Options options) {
		HelpFormatter formatter = new HelpFormatter();
		PrintWriter writer = new PrintWriter(out);
		formatter.printHelp(writer, formatter.getWidth(), "stf", null, options, formatter.getLeftPadding(), formatter.getDescPadding(), null, true);
		writer.flush();
	}
}
