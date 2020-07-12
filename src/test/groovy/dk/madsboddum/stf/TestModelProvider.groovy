package dk.madsboddum.stf

import dk.madsboddum.stf.model.StringTable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class TestModelProvider {
	@Nested
	class Read {
		ModelProvider provider
		StringTable stringTable
		
		@BeforeEach
		void setup() {
			def stream = TestModelProvider.getResourceAsStream("base_player.stf")
			provider = new ModelProvider(stream)
			stringTable = provider.get()
		}
		
		@Test
		void testAmount() {
			assertEquals(315, stringTable.size(), "base_player.stf contains 315 entries, so there should be 315 entries in the model object")
		}
		
		@Test
		void testNoNullKeys() {
			def map = stringTable.getMap()
			
			assertFalse(map.containsKey(null), map.get(null) + " value had null key")
		}
		
		@Test
		void testNoNullValues() {
			def map = stringTable.getMap()
			
			assertFalse(map.containsValue(null))
		}
		
		@Test
		void testEscapeLineBreaks() {
			def text = stringTable.get("sui_bind_prompt_cash")	// Known value that has line breaks
			
			assertFalse(text.contains("\n"))	// Should be escaped to \\n
		}
		
	}
	
}
