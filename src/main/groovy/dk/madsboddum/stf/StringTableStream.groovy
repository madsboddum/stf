package dk.madsboddum.stf

class StringTableStream {
	private final String name
	private final InputStream inputStream
	
	StringTableStream(String name, InputStream inputStream) {
		this.name = name
		this.inputStream = inputStream
	}
	
	String getName() {
		return name
	}
	
	InputStream getInputStream() {
		return inputStream
	}
}
