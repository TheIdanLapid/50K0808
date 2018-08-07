package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SokobanClient {
	private boolean exit;
	
	private void readInputsAndSend(BufferedReader in, PrintWriter out, String exitStr){
		exit=false;
		try {
			while (!exit)
			{
				String line = in.readLine();
				if (line!=null) //if there is no input, don't send it to the server (null exception)
				{
					if(line.equals(exitStr))
					{
						exit = true;
						System.out.println("Bye bye!");
					}
						out.println(line);
						out.flush();
				}
			}
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	private Thread aSyncReadInputsAndSend(BufferedReader in, PrintWriter out, String exitStr){
		Thread t = new Thread(new Runnable() {
			public void run() { readInputsAndSend(in, out, exitStr);}
		});
	t.start();
	return t;
	}
	
	public void start(String ip, int port){
		try
		{
			Socket theServer=new Socket(ip, port);
			System.out.println("Connected!");
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
			PrintWriter outToServer = new PrintWriter(theServer.getOutputStream());
			PrintWriter outToScreen = new PrintWriter(System.out);
			Thread t1 = aSyncReadInputsAndSend(userInput,outToServer,"Exit");
			Thread t2 = aSyncReadInputsAndSend(serverInput,outToScreen,"BYE!");
			t1.join(); t2.join();
			userInput.close();
			serverInput.close();
			outToServer.close();
			outToScreen.close();
			theServer.close();
		} 
		catch(UnknownHostException e) {System.out.println(e);}
		catch(IOException e) {System.out.println(e);}
		catch(InterruptedException e) {System.out.println(e);}
	}
}

