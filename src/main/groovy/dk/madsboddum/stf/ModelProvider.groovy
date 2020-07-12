package dk.madsboddum.stf

import dk.madsboddum.stf.model.StringTable

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class ModelProvider {
	private static final int STF_HEADER = 0x0000ABCD
	
	private final InputStream inputStream
	
	ModelProvider(InputStream inputStream) {
		this.inputStream = inputStream
	}
	
	StringTable get() {
		def stringTable = new StringTable()
		
		def buffer = ByteBuffer.wrap(inputStream.readAllBytes())
			.order(ByteOrder.LITTLE_ENDIAN)
		
		def header = buffer.getInt()
		
		if (header != STF_HEADER) {
			throw new UnsupportedOperationException("Unknown header " + header + ". Expected " + STF_HEADER + ".")
		}
		
		buffer.get()	// Flag or some sort?
		buffer.getInt()	// Next free index, in case we want to insert something
		
		def itemCount = buffer.getInt()
		
		// Read values
		def values = new HashMap<Integer, String>(itemCount)
		
		for (def i = 0; i < itemCount; i++) {
			def index = buffer.getInt()
			buffer.getInt()	// Unsure what this is
			def length = buffer.getInt()	// Length of the unicode string to decode
			def unicodeData = new byte[length * 2]	// Two bytes per character in a unicode string
			buffer.get(unicodeData)
			
			def string = new String(unicodeData, StandardCharsets.UTF_16LE)
			
			values[index - 1] = string	// Index we read isn't zero-indexed. The first index is 1.
		}
		
		// Read keys
		def keys = new HashMap<Integer, String>(itemCount)
		
		for (def i = 0; i < itemCount; i++) {
			def index = buffer.getInt()
			def length = buffer.getInt()	// Length of the unicode string to decode
			def unicodeData = new byte[length]	// One byte per character in an ASCII string
			buffer.get(unicodeData)
			
			def string = new String(unicodeData, StandardCharsets.US_ASCII)
			
			keys[index - 1] = string	// Index we read isn't zero-indexed. The first index is 1.
		}
		
		def indices = keys.keySet() + values.keySet()
		
		for (def i : indices) {
			def key = keys[i]
			def value = values[i]
			
			stringTable.put(key.replace("\n", "\\n"), value.replace("\n", "\\n"))	// We don't want newlines displayed, so escape them
		}
		
		return stringTable
	}
	
}