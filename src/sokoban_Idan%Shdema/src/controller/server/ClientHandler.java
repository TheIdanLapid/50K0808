package controller.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

import commons.Level;
/**
 * The generic Handler interface.
 */
public interface ClientHandler {
	void handleClient(BufferedReader inFromClient, PrintWriter outToClient);
	void feedback(String command);
	void setLevel(Level l);
	List<String> getParams();
}
