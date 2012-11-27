/*
 * 
 */
import java.io.*;
import java.util.*;
import java.net.*;
import org.json.simple.JSONValue;

public class ServiceThread extends Thread {
  private ServerSocket welcomeSocket;
  private Map<String, String> updateTable;
  private BufferedReader inFromClient;
  private DataOutputStream outToClient;
  private SessionManager mySession;
  public ServiceThread(ServerSocket welcomeSocket, 
    Map<String, String> updateTable) {

    this.welcomeSocket = welcomeSocket;
  }
  public void run() {
    System.out.println("Thread " + this + " started.");
  	while (true) {
	    // get a new request connection
	    Socket s = null;
	    synchronized (welcomeSocket) {
    		try {
    		    s = welcomeSocket.accept();
    		    System.out.println("Thread "+this+" process request "+s);
    		} catch (IOException e) {
    		}
      } // end of extract a request
      processRequest(s);
  	} // end while
  } // end run
  private void processRequest(Socket connSock) {
    try {
	    // create read stream to get input
	    inFromClient = new BufferedReader(new InputStreamReader(connSock.getInputStream()));
	    outToClient = new DataOutputStream(connSock.getOutputStream());
	    String query = inFromClient.readLine();
      String[] request = query.split("\\s");
	    if (request.length < 2 || !request[0].equals("GET")) {
		    outputError(500, "Bad request");
		    connSock.close();
		    return;
	    }    
	    //path
	    String[] path = request[1].split("\\?");      
      //http parameters
	    String[] params = path[1].split("&");
	    Map<String, String> map = new HashMap<String, String>();
	    for (String param : params) {
        String name = param.split("=")[0];
        String value = param.split("=")[1];
        map.put(name, value);
	    } 
      //Leaving this for reference on how to access http parameters
	    /*String me = map.get("me");
	    String meV = map.get("meV");*/
	    String action = path[0];
      if(action.equals("/new")) {
        mySession = new SessionManager();
      } else if (action.equals("/join")) {
        mySession.joinSession(map.get("sessionCode"));
      } else if (action.equals("/touch")) {
          //TODO
      } else {
        outputError(500, "Bad request");
        connSock.close();
        return;
      }
	    outputResponseHeader();
	    outputResponseBody(mySession.getSessionId());
	    connSock.close();
    } catch (Exception e) {
    }
  } // end of serveARequest
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
  	}
  }
} // end ServiceThread
