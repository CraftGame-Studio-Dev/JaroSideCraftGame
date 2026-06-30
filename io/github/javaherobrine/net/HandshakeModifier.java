package io.github.javaherobrine.net;
import io.github.javaherobrine.*;
import java.io.*;
/**
 * This interface is used to replace default implementation of handshake. Used
 * in protocol
 * 
 * @author javaherobrine
 */
@Modification("Let your protocol class implement this interface")
public interface HandshakeModifier {
	void handshakeClient() throws IOException;
	void handshakeServer() throws IOException;
}
