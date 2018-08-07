package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mvvm.model.AdminModel;
import sokobanSolver.SokobanPlannableAdapter;
import stripsLib.Action;
import stripsLib.Plannable;
import stripsLib.Planner;
import stripsLib.Strips;

/**
 * Handles the server I/O according to the our game logic.
 * Talks with the client using the agreed protocol.
 */
//implements View
public class Handler extends Observable {
	
	private List<String> params;
	private BufferedReader inFromClient;
	private PrintWriter outToClient;
	private Socket socket;
	private String levelName;
	private boolean zeroMoves;
	

	public void handleClient(Socket socket) {
		this.socket = socket;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new PrintWriter(socket.getOutputStream());
		} catch (IOException e1) {
			System.out.println("Error opening socket");
			e1.printStackTrace();
		}
		
		String userName = "Client" + socket.getPort();
		
		AdminModel.getInstance().addClient(userName, this);
		
		//the client is connected and about to send the protocol
		
		String line = "";
		try {
			line = inFromClient.readLine();
			System.out.println(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //first, is it the first move or not?
		if (line.equals("zeroMoves"))
		{
			zeroMoves = true;
		}
		else if (line.equals("notZeroMoves"))
		{
			zeroMoves = false;
		}
		
		line = "";
		line = readFromClient(); //second, hint or full solution?
		System.out.println(line);
		switch (line) {
		case "Hint":
			sendSolOrHint("Hint");
			break;
		case "Sol":
			sendSolOrHint("Sol");
			break;
		}
		
	}
	
	public void sendSolOrHint(String hintOrSol) 
	{
		String iStr = "";
		String jStr = "";
		levelName = "";
		
		levelName = readFromClient();
		System.out.println(levelName);
		iStr = readFromClient();

		jStr = readFromClient();


		
		int iBound = Integer.parseInt(iStr);
		System.out.println(iStr);
		int jBound = Integer.parseInt(jStr);
		System.out.println(jStr);
		char[][] sb = new char[iBound][jBound];
		int i=0;
		String line = "";
		do 
		{
			line = readFromClient();
			System.out.println(line);
			for (int j=0; j<jBound; j++)
			{
				sb[i][j] = line.charAt(j);
			}
			i++;
		}
		while (i<iBound); //the client will send the current state of the board
		
		if (zeroMoves)
		{
			String solFromService = getSolutionFromService(levelName);
	
			if (solFromService!=null)
			{
				if (hintOrSol.equals("Hint"))
				{
					outToClient.println(solFromService.charAt(0));
					outToClient.flush();
				}
				else if (hintOrSol.equals("Sol"))
				{
					outToClient.println(solFromService);
					outToClient.flush();
				}
			}

			else
			{
				String solutionString = SolveLevel(sb);
				addSolutionToDB(levelName, solutionString);
				if (hintOrSol.equals("Hint"))
				{
					outToClient.println(solutionString.charAt(0));
					outToClient.flush();
				}
				else if (hintOrSol.equals("Sol"))
				{
					outToClient.println(solutionString);
					outToClient.flush();
				}
	
			}
		}
		else //not first step - don't save solution
		{
			String solutionString = SolveLevel(sb);
			if (hintOrSol.equals("Hint"))
			{
				outToClient.println(solutionString.charAt(0));
				outToClient.flush();
			}
			else if (hintOrSol.equals("Sol"))
			{
				outToClient.println(solutionString);
				outToClient.flush();
			}
		}
	}
	
	private String readFromClient()
	{
		try {
			if (inFromClient!=null && inFromClient.ready())
			{
				try {
					String line = "";
					line = inFromClient.readLine();
					if (line.equals("BYE"))
					{
						System.out.println("The client has disconnected!");
						return null;
					}
					else
					{
						return line;
					}
				} catch (IOException e) {
					System.out.println("Couldn't read from client!");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println("Wasn't ready");
			e.printStackTrace();
		}
		return null;
	}
	
	
	private String SolveLevel(char[][] sb) {
		
		Planner strips = new Strips();
		Plannable adapter = new SokobanPlannableAdapter(sb);
		List<Action> list = new LinkedList<>();
		list = strips.plan(adapter);
		StringBuilder plan = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getSearchActions().size(); j++) {
				plan.append((list.get(i).getSearchActions().get(j).getName().charAt(5)));
			}
		}
		if (plan.length()==0)
		{
			outToClient.print("Couldn't solve level");
			outToClient.flush();
		}
		return plan.toString();
	}
	
	//gets and sets
	public List<String> getParams() {
		return params;
	}


	public void setParams(List<String> params) {
		this.params = params;
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
			System.out.println("A client has disconnected!");
			socket.close();
		} catch (IOException e) {
			System.out.println("Error closing socket!");
			e.printStackTrace();
		}
	}
	
	private String getSolutionFromService(String name)
	{
				String url = "http://localhost:8080/Jersey/webapi/solutions/" + name;
				Client client = ClientBuilder.newClient();
				WebTarget webTarget = client.target(url);
				Response response = webTarget.request(MediaType.TEXT_PLAIN).get(Response.class);
				if (response.getStatus() == 200) {
					String solution = response.readEntity(new GenericType<String>() {
					});
					return solution;
				}
				System.out.println(response.getHeaderString("errorResponse"));
				return null;
	}
	
	private static void addSolutionToDB(String name, String solution) {
		if (solution.length()>0)
		{
			String url = "http://localhost:8080/Jersey/webapi/solutions/";
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(url);
			
			Form form = new Form();
			form.param("name", name);
			form.param("solution", solution);
			
			Response response = webTarget.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
		
			if (response.getStatus() == 204) {
				System.out.println("Solution added successfully");
			}
			else {
				System.out.println(response.getHeaderString("errorResponse"));
			}
		}
	}
	
	
}
