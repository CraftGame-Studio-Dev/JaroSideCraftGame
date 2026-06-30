package io.github.javaherobrine.format;
import java.io.*;
public class JSONWriter extends FilterWriter implements ObjectOutput {
	public JSONWriter(Writer out) {
		super(out);
	}
	public JSONWriter(OutputStream out) {
		super(new BufferedWriter(new OutputStreamWriter(out)));
	}
	@Override
	public void writeBoolean(boolean v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeByte(int v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeShort(int v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeChar(int v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeInt(int v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeLong(long v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeFloat(float v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeDouble(double v) throws IOException {
		out.write(JSONFormatter.format(v));
	}
	@Override
	public void writeBytes(String s) throws IOException {
		out.write(JSONFormatter.format(s));
	}
	@Override
	public void writeChars(String s) throws IOException {
		out.write(JSONFormatter.format(s));
	}
	@Override
	public void writeUTF(String s) throws IOException {
		out.write(JSONFormatter.format(s));
	}
	@Override
	public void writeObject(Object obj) throws IOException {
		out.write(JSONFormatter.format(obj));
	}
	@Override
	public void write(byte[] b) throws IOException {
		out.write(JSONFormatter.format(b));
	}
	@Override
	@Deprecated
	public void write(byte[] b, int off, int len) throws IOException {
	}
}
