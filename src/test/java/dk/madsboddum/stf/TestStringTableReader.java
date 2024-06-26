package dk.madsboddum.stf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestStringTableReader {
	@Nested
	class Read {
		StringTableReader provider;
		Map<String, String> map;
		
		@BeforeEach
		void setup() throws IOException {
			InputStream stream = TestStringTableReader.class.getResourceAsStream("base_player.stf");
			provider = new StringTableReader();
			map = provider.read(stream);
		}
		
		@Test
		void testAmount() {
			assertEquals(315, map.size(), "base_player.stf contains 315 entries, so there should be 315 entries in the model object");
		}
		
		@Test
		void testNoNullKeys() {
			assertFalse(map.containsKey(null), map.get(null) + " value had null key");
		}
		
		@Test
		void testNoNullValues() {
			assertFalse(map.containsValue(null));
		}
		
		@Test
		void testEscapeLineBreaks() {
			String text = map.get("sui_bind_prompt_cash");	// Known value that has line breaks
			
			assertFalse(text.contains("\n"));	// Should be escaped to \\n
		}
		
	}
	
}
