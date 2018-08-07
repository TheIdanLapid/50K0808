/**
 * 
 */
package mvvm.model;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import server.Handler;

/**
 * @author שדמה
 * Model for the admin gui
 */
public class AdminModel extends Observable {
	
	private Handler handler;
	
	private Map<String, Socket> connectedClients =
			new HashMap<String, Socket>();
	
	private static final AdminModel instance = new AdminModel();
	private AdminModel() {}
	public static AdminModel getInstance() {
		return instance;
	}
	
	public void addClient(String userName, Handler handler) {
		this.handler = handler;
		connectedClients.put(userName, handler.getSocket());
		setChanged();
		List<String> params = new LinkedList<String>();
		params.add("Add");
		params.add(userName);
		notifyObservers(params);
	}
	
	public void disconnectClient(String userName) {
//		Socket socket = connectedClients.get(userName);
		handler.closeSocket();
		connectedClients.remove(userName);
		setChanged();
		List<String> params = new LinkedList<String>();
		params.add("Remove");
		params.add(userName);
		notifyObservers(params);
	}
}
