package dk.madsboddum.stf;

import dk.madsboddum.stf.model.StringTable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class ModelProvider {
	private static final int STF_HEADER = 0x0000ABCD;
	
	StringTable get(InputStream inputStream) throws IOException {
		StringTable stringTable = new StringTable();
		
		ByteBuffer buffer = ByteBuffer.wrap(inputStream.readAllBytes())
			.order(ByteOrder.LITTLE_ENDIAN);
		
		int header = buffer.getInt();
		
		if (header != STF_HEADER) {
			throw new UnsupportedOperationException("Unknown header " + header + ". Expected " + STF_HEADER + ".");
		}
		
		buffer.get();	// Flag or some sort?
		buffer.getInt();	// Next free index, in case we want to insert something
		
		int itemCount = buffer.getInt();
		
		// Read values
		Map<Integer, String> values = new HashMap<>(itemCount);
		
		for (int i = 0; i < itemCount; i++) {
			int index = buffer.getInt();
			buffer.getInt();	// Unsure what this is
			int length = buffer.getInt();	// Length of the unicode string to decode
			byte[] unicodeData = new byte[length * 2];	// Two bytes per character in a unicode string
			buffer.get(unicodeData);
			
			String string = new String(unicodeData, StandardCharsets.UTF_16LE);
			
			values.put(index - 1, string);	// Index we read isn't zero-indexed. The first index is 1.
		}
		
		// Read keys
		Map<Integer, String> keys = new HashMap<>(itemCount);
		
		for (int i = 0; i < itemCount; i++) {
			int index = buffer.getInt();
			int length = buffer.getInt();	// Length of the unicode string to decode
			byte[] unicodeData = new byte[length];	// One byte per character in an ASCII string
			buffer.get(unicodeData);
			
			String string = new String(unicodeData, StandardCharsets.US_ASCII);
			
			keys.put(index - 1, string);	// Index we read isn't zero-indexed. The first index is 1.
		}
		
		Collection<Integer> indices = new ArrayList<>();
		indices.addAll(keys.keySet());
		indices.addAll(values.keySet());
		
		for (int i : indices) {
			String key = keys.get(i);
			String value = values.get(i);
			
			stringTable.put(key.replace("\n", "\\n"), value.replace("\n", "\\n"));	// We don't want newlines displayed, so escape them
		}
		
		return stringTable;
	}
	
}