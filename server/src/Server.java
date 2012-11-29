// simple Java server

//to compile: javac -cp json-simple-1.1.1.jar *.java
//to run: java -cp '.:json-simple-1.1.1.jar' Server

import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
	public final static int THREAD_COUNT = 2;
	private ServerSocket welcomeSocket;
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
			Map sessionsMap = Collections.synchronizedMap(new HashMap<String, SessionManager>());
			// start all threads
			for (int i = 0; i < threads.length; i++) {
    		threads[i] = new ServiceThread(welcomeSocket, updateTable, sessionsMap);
    		threads[i].start();
			}
		} catch (Exception e) {
			System.out.println("WBServer construction failed.");
		}
	}
	public static void main(String[] args) {
	    // see if we do not use default server port
	    int serverPort = 6789;
	    if (args.length >= 1)
	    	serverPort = Integer.parseInt(args[0]);
	    Server server = new Server(serverPort);
	    server.run();
	}
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
	}
}

