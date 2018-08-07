package controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import controller.commands.Command;
import controller.server.Handler;
/**
 * General class, holds a blockingqueue of commands.
 * Runs a poll function on the queue in a new thread. 
 * @author Eon
 *
 */
public class Controller {
	private BlockingQueue<Command> queue; //queue that holds the commands to execute
	private boolean stop = false; //boolean that stops the run() loop
	private Handler handler;
	
	
	public Controller(){
		handler = null;
		queue = new ArrayBlockingQueue<Command>(10);
	}
	
	//the function gets command and adds it to the end of the queue
	public void insertCommand(Command com){
		try {
			queue.put(com);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//open a new thread initialized with anonymous Runnable 
	//- in fact an anonymous class that implements Runnable and override run()
	public void start(){
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(!stop){
						try {
							//dequeue the first command in the queue
							//if the queue is empty, wait for 1 second for a command to enter
							Command com = queue.poll(1, TimeUnit.SECONDS);
							if(com!=null)
							{
								com.execute();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}					
					}
				}
			});
			thread.start(); //here we start the thread we just defined
	}

	public void stop(){
		stop = true;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;		
	}

	public Handler getHandler() {
		return handler;
	}
}
