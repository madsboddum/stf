package dk.madsboddum.stf.model

class StringTable {
	private final Map<String, String> map
	
	StringTable() {
		map = new HashMap<>()
	}
	
	def getMap() {
		return new HashMap<>(map)
	}
	
	def put(String key, String value) {
		return map.put(key, value)
	}
	
	def get(String key) {
		return map.get(key)
	}
	
	def size() {
		return map.size()
	}
}
