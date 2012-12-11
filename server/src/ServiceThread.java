import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;

public class ServiceThread extends Thread {
	private ServerSocket welcomeSocket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private SessionManager mySession;
	
	/* Static variables */
	private static Map<String, SessionManager> sessionMap = 
			Collections.synchronizedMap(new HashMap<String, SessionManager>());
	private static int nextPort = 10000;
	
	public ServiceThread(ServerSocket welcomeSocket, 
			Map<String, String> updateTable /*, Map<String, SessionManager> sessionMap */ ) {
		this.welcomeSocket = welcomeSocket;
		initializeProcessing("10001");
		// this.sessionMap = sessionMap;
	}
	public void run() {
		System.out.println(this + " started.");
		if(sessionMap.isEmpty()) {
			System.out.println("No current sessions.");
		} else {
			for(String key : sessionMap.keySet()) {
				System.out.println("Starting session with ID "+key);
			}
		}
		while (true) {
			// Get a new request connection
			Socket s = null;
			synchronized (welcomeSocket) {
				try {
					s = welcomeSocket.accept();
					System.out.println("Thread "+this+" process request "+s);
				} catch (IOException e) {
				}
			}
			processRequest(s);
		}
	}
	private void processRequest(Socket connSock) {
		try {
			// Create read stream to get input
			inFromClient = new BufferedReader(new InputStreamReader(connSock.getInputStream()));
			outToClient = new DataOutputStream(connSock.getOutputStream());
			// Map to store parameters
			Map<String, String> map = new HashMap<String, String>();
			
		    String query = inFromClient.readLine();
		    System.out.println("Query: "+query);
		    String[] request = query.split("\\s");
		    if (request.length < 2 || !request[0].equals("GET")) {
			    outputError(500, "Bad request");
			    connSock.close();
			    return;
		    }
		    String[] path = request[1].split("\\?");
		    String action = path[0];
		    if (path.length > 1) {
			    String[] params = path[1].split("&");
			    for (String param : params) {
			    	String name = param.split("=")[0];
			    	String value = param.split("=")[1];
			    	map.put(name, value);
			    } 
		    }
		    // To get a URL variable: 
		    // String variable = map.get("varName");
		    mySession = sessionMap.get(map.get("id"));
		    String outputString = "Nothing happened!";
		    if(mySession == null && !action.equals("/new")) {
		    	outputError(404, "No such session found");
	    		connSock.close();
	    		return;
		    }
		    if(action.equals("/new")) {
		    	String id = generateId();
		    	mySession = new SessionManager(id);
		    	sessionMap.put(id, mySession);
		    	outputString = id;
		    	initializeProcessing(id);
		    } else if (action.equals("/join")) {
		    	mySession.joinSession();
		    } /* else if (action.equals("/touch")) {
		    	float startX = Float.parseFloat(map.get("startX"));
		    	float startY = Float.parseFloat(map.get("startY"));
		    	float endX = Float.parseFloat(map.get("endX"));
		    	float endY = Float.parseFloat(map.get("endY"));
		    	//mySession.updateBitMap(startX, startY, endX, endY);
		    	outputString = "BitMap for "+mySession.getId()+" updated.";
		    } */ else {
		    	outputError(404, "No such path.");
		    	connSock.close();
		    	return;
		    }
		    outputResponseHeader();
		    outputResponseBody(outputString);
		    connSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static String generateId() {
		nextPort++;
		return nextPort + "";
	}
	private void initializeProcessing(String id) {
		// PApplet.main(new String[] { "--present", "VideoSender" });
		VisualizationManager vs = new VisualizationManager(id);
		PApplet.runSketch(new String[]{ "" }, vs);
	}

	private void outputResponseHeader() throws Exception {
		outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
	}
	private void outputResponseBody(String out) throws Exception {
		outToClient.writeBytes("Content-Length: " + out.length() + "\r\n");
		outToClient.writeBytes("\r\n");
		outToClient.writeBytes(out);
	}
	void outputError(int errCode, String errMsg) {
		try {
			outToClient.writeBytes("HTTP/1.0 " + errCode + " " + errMsg
					+ "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

