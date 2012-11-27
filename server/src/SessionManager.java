/*
 * 
 */
import java.io.*;
import java.util.*;
import java.net.*;
import org.json.simple.JSONValue;

public class SessionManager {
  /* Static variables */
  private static Map<String, SessionManager> sessionsMap = new HashMap<String, SessionManager>();
  
  private Queue<TouchEvent> queueOfEvents;

  private int[][] bitmap;
  // TODO: NOT RIGHT HEIGHT OR WIDTH DUH
  private final int height = 9;
  private final int width = 16;
  private String sessionId;

  public SessionManager() {
    queueOfEvents = new LinkedList<TouchEvent>();
    bitmap = new int[height][width];
    sessionId = generateId();
    sessionsMap.put(sessionId, this);
    initBitmap();
  }
  // Still to do
  public boolean joinSession(String sessionId) {
    return true;
  }

  public static SessionManager getSession(String sessionId) {
    return sessionsMap.get(sessionId);
  }



  private void initBitmap() {
    for(int i=0; i<height; i++) {
      for(int j=0; j<width; j++) {
        bitmap[i][j] = 0;
      }
    }
  }
  // TODO: MAKE A REAL GENERATOR
  private String generateId() {
    return "1";
  }
}