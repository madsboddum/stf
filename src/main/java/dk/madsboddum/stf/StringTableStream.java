package dk.madsboddum.stf;

import java.io.InputStream;

public class StringTableStream {
	private final String name;
	private final InputStream inputStream;
	
	public StringTableStream(String name, InputStream inputStream) {
		this.name = name;
		this.inputStream = inputStream;
	}
	
	String getName() {
		return name;
	}
	
	InputStream getInputStream() {
		return inputStream;
	}
}
