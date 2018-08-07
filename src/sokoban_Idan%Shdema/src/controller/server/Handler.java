package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import commons.Level;
import view.PrintCLI;
import view.View;
/**
 * Observed by SokobanController.
 * Handles the server I/O according to the our game logic.
 * It's the implementation to view when on server.
 */
public class Handler extends Observable implements View {
	
	private List<String> params;
	private BufferedReader inFromClient;
	private PrintWriter outToClient;
	private Socket socket;
	

	public void handleClient(Socket socket) {
		String str="";
		this.socket = socket;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new PrintWriter(socket.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Error opening socket");
			e1.printStackTrace();
		}
		
		setChanged();
		notifyObservers(this);
		
		while (!str.equals("Exit"))
		{
			try 
			{
			
				str = this.inFromClient.readLine();
				String[] wordsArr = str.split(" ");
				params = new LinkedList<String>();
				for (String param : wordsArr)
					params.add(param);

				setChanged();
				notifyObservers(params);
	
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void feedback(String command)
	{
		switch (command)
		{
		case "notLoaded":
			this.outToClient.print("Load a level first!\n");
			this.outToClient.flush();
			break;
		case "noCommand":
			this.outToClient.print("No such command!\n");
			this.outToClient.flush();
			break;
		case "Saved":
			this.outToClient.print("Level saved!\n");
			this.outToClient.flush();
			break;
		case "Exit":
			this.outToClient.print("BYE!\n");
			this.outToClient.flush();
			break;
		}
	}
	
	//gets and sets
	public List<String> getParams() {
		return params;
	}


	public void setParams(List<String> params) {
		this.params = params;
	}

	@Override
	public void display(Level l) {
		PrintCLI p = new PrintCLI();
		p.print(l,outToClient);
	}

	public void displayWin(Level l) {
		PrintCLI p = new PrintCLI();
		p.printWin(l,outToClient);
	}

	public BufferedReader getInFromClient() {
		return inFromClient;
	}

	public void setInFromClient(BufferedReader inFromClient) {
		this.inFromClient = inFromClient;
	}

	public PrintWriter getOutToClient() {
		return outToClient;
	}

	public void setOutToClient(PrintWriter outToClient) {
		this.outToClient = outToClient;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void closeSocket() {
		try {
			System.out.println("Client has disconnected!");
			socket.close();
		} catch (IOException e) {
			System.out.println("Error closing socket!");
			e.printStackTrace();
		}
	}
	
	
}
