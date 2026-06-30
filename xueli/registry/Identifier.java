package xueli.registry;
public record Identifier(String namespace, String location) {
	public Identifier(String location) {
		this("default", location);
	}
	public static Identifier serialize(String str) {// actually deserialize
		str = str.trim();
		int separatorIndex = str.indexOf(':');
		if (separatorIndex < 0) {
			return new Identifier(str);
		} else {
			String namespace = str.substring(0, separatorIndex);
			String location = str.substring(separatorIndex);
			if (namespace.isBlank()) return new Identifier(location);
			return new Identifier(namespace, location);
		}
	}
	@Override
	public String toString() {
		return namespace + ":" + location;
	}
	// function hash and equals are removed: let JDK auto generate them
}
