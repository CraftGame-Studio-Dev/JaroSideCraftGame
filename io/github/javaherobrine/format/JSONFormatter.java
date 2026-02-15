package io.github.javaherobrine.format;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
public final class JSONFormatter {
	private JSONFormatter() {
	}
	public static String format(CharSequence raw) {
		final StringBuilder sb = new StringBuilder(raw.length() << 1 + 2);
		sb.append('\"');
		raw.chars().forEach(ch -> {
			switch (ch) {
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case ' ':
				sb.append("\\s");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\'':
				sb.append("\\\'");
				break;
			case '\"':
				sb.append("\\\"");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			default:
				sb.append((char) ch);
			}
		});
		return sb.append('\"').toString();
	}
	public static String format(Number num) {
		return num.toString();
	}
	public static String format(boolean b) {
		return Boolean.toString(b);
	}
	public static String format(byte b) {
		return Byte.toString(b);
	}
	public static String format(short s) {
		return Short.toString(s);
	}
	public static String format(int i) {
		return Integer.toString(i);
	}
	public static String format(long l) {
		return Long.toString(l);
	}
	public static String format(float f) {
		return Float.toString(f);
	}
	public static String format(double d) {
		return Double.toString(d);
	}
	public static String format(char d) {
		return Character.toString(d);
	}
	public static String format(boolean[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(byte[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(short[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(int[] obj) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(long[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(float[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(double[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(char[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(obj[i]);
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	public static String format(Object[] obj) {
		if (obj.length == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < obj.length; ++i) {
			sb.append(format(obj[i]));
			sb.append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append(']').toString();
	}
	static String format(Object obj) {
		if (obj == null) {
			return "null";
		}
		if (obj instanceof JSONSerializable) {
			return format((JSONSerializable) obj);
		} else if (obj instanceof Number) {
			return format((Number) obj);
		} else if (obj instanceof CharSequence) {
			return format((CharSequence) obj);
		} else if (obj instanceof JSONString str) {
			return format(str.toString());
		} else if (obj instanceof boolean[]) {
			return format((boolean[]) obj);
		} else if (obj instanceof short[]) {
			return format((short[]) obj);
		} else if (obj instanceof int[]) {
			return format((int[]) obj);
		} else if (obj instanceof long[]) {
			return format((long[]) obj);
		} else if (obj instanceof float[]) {
			return format((float[]) obj);
		} else if (obj instanceof double[]) {
			return format((double[]) obj);
		} else if (obj instanceof char[]) {
			return format((char[]) obj);
		} else if (obj instanceof Object[]) {
			return format((Object[]) obj);
		} else if (obj instanceof Map) {
			return format((Map<?, ?>) obj);
		} else if (obj instanceof Collection) {
			return format((Collection<?>) obj);
		} else {
			throw new IllegalArgumentException("Can't serialize this object" + obj);
		}
	}
	public static String format(JSONSerializable obj) {
		if (obj == null) {
			return "{}";
		}
		AbstractMap.SimpleEntry<String, Object>[] values = obj.values();
		if (values.length == 0) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder("{");
		for (int i = 0; i < values.length; i++) {
			AbstractMap.SimpleEntry<String, Object> current = values[i];
			sb.append(format(current.getKey())).append(':').append(format(current.getValue())).append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).append('}').toString();
	}
	public static String format(Collection<? extends Object> obj) {
		if (obj.isEmpty()) {
			return "[]";
		}
		return format(obj.toArray());
	}
	public static String format(Map<String, ? extends Object> pair) {
		if (pair.isEmpty()) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder("{");
		pair.entrySet().stream().forEach(n -> {
			sb.append(format(n.getKey()));
			sb.append(':');
			sb.append(format(n.getValue()));
			sb.append(',');
		});
		return sb.deleteCharAt(sb.length() - 1).append('}').toString();
	}
}
