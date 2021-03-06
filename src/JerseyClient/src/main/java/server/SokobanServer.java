package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A generic server for our game. 
 * Opens sockets for himself and runs a thread pool for clients.
 * Contains the handlers that interact with the clients.
 * @author Eon
 *
 */
	
public class SokobanServer {
	private int port;
	private Handler ch;
	private boolean stop = false;
	private ExecutorService threadPool;
	private static final int THREADS_NUM = 30;

	public SokobanServer(int port, Handler ch) {
		this.port = port;
		this.ch = ch;
		threadPool = Executors.newFixedThreadPool(THREADS_NUM);
	}

	private void runServer() throws Exception {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
		while (!stop) {
			try {
				Socket aClient = server.accept(); // blocking call
				System.out.println("A new client has connected! "+aClient.getPort());
				threadPool.execute(new Runnable() { 
					public void run() {
							ch.handleClient(aClient);
					}
				});
			} catch (SocketTimeoutException e) {}
		}
		server.close(); 
	}
	
	public void start(){
		new Thread(new Runnable() {
			public void run() {
				try{runServer();} catch (Exception e) {}
			}
		}).start();
	}
	
	public void stopServer() {		
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(5, TimeUnit.SECONDS); //wait for all threads to die	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stop = true;
		}
		
	}
	
	//gets and sets
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Handler getCh() {
		return ch;
	}

	public void setCh(Handler ch) {
		this.ch = ch;
	}
	
}
