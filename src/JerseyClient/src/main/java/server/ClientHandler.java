package server;

import java.net.Socket;
/**
 * The generic Handler interface.
 */
public interface ClientHandler {
	void handleClient(Socket socket);
}
