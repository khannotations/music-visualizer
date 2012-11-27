// simple Java server


import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
	
<<<<<<< HEAD
    private ServerSocket welcomeSocket;

    public final static int THREAD_COUNT = 3;
    
    private ServiceThread[] threads;
    private Map<String, String> updateTable;
	
    /* Constructor: First Run of the server*/
    public Server(int serverPort) {

	try {
	    // create server socket
	    welcomeSocket = new ServerSocket(serverPort);
	    System.out.println("Server at" + welcomeSocket);
	    
	    // create thread pool
	    threads = new ServiceThread[THREAD_COUNT];
	    
	    // start all threads
	    for (int i = 0; i < threads.length; i++) {
		threads[i] = new ServiceThread(welcomeSocket, updateTable);
		threads[i].start();
	    }
	} catch (Exception e) {
	    System.out.println("WBServer construction failed.");
	}
	
    } // end of Server
    
    public static void main(String[] args) {
	// see if we do not use default server port
	int serverPort = 6789;
	if (args.length >= 1)
	    serverPort = Integer.parseInt(args[0]);
	
	Server server = new Server(serverPort);
	server.run();
	
    } // end of main

    // Infinite loop to process each connection
    public void run() {

	try {
	    for (int i = 0; i < threads.length; i++) {
		threads[i].join();
	    }
	    System.out.println("All threads finished. Exit");
	    
	} catch (Exception e) {
	    System.out.println("Join errors");
	}
	
    } // end of run
}
=======
	public static void main(String[] getBitches) { // fuck yeah!
    for(int i=0; i<getBitches.length(); i++) {
      System.out.println("Mo money-"+i+": "+getBitches[i])
    }
  }
}
>>>>>>> f9874b040c586937ada7ba753b1efff68bb22825
