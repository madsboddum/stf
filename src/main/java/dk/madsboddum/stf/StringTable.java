package dk.madsboddum.stf;

import java.util.HashMap;
import java.util.Map;

public class StringTable {
	private final Map<String, String> map;
	
	public StringTable() {
		map = new HashMap<>();
	}
	
	public Map<String, String> getMap() {
		return new HashMap<>(map);
	}
	
	public String put(String key, String value) {
		return map.put(key, value);
	}
	
	public String get(String key) {
		return map.get(key);
	}
	
	public int size() {
		return map.size();
	}
}
