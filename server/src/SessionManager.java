import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/*
 * 
 */

public class SessionManager {
  /* Static variables */
  private static Map<String, SessionManager> sessionsMap = new HashMap<String, SessionManager>();
  
  private Queue<TouchEvent> queueOfEvents;

  private int[][] bitmap;
  // TODO: NOT RIGHT HEIGHT OR WIDTH DUH
  private final int height = 900;
  private final int width = 1600;
  private String sessionId;

  public SessionManager() {
    queueOfEvents = new LinkedList<TouchEvent>();
    bitmap = new int[width][height];
    sessionId = generateId();
    sessionsMap.put(sessionId, this);
    initBitmap();
  }
  
  // Still to do
  public boolean joinSession(String sessionId) {
    return true;
  }
  public String getSessionId() {
    return sessionId;
  }

  public static SessionManager getSession(String sessionId) {
    return sessionsMap.get(sessionId);
  }

  private void initBitmap() {
    for(int i=0; i<width; i++) {
      for(int j=0; j<height; j++) {
        bitmap[i][j] = 0;
      }
    }
  }
  
  // TODO: MAKE A REAL GENERATOR
  private String generateId() {
    return "1";
  }
  
  //for stage 1, a function to draw the lines
  public void updateBitMap(float startX, float startY, float endX, float endY) {
    System.out.println("YO");
    float deltaX = startX - endX;
    float deltaY = startY - endY;
    
    int absoluteStartX = (int) (startX * width);
    int absoluteStartY = (int) (startY * height);
    int absoluteEndX = (int) (endX * width);
    int absoluteEndY = (int) (endY * height);
    int currentRoundedX = absoluteStartX;
    int currentRoundedY = absoluteStartY;
    float xStep = 1;
    float yStep = 1;
    
    //unrounded values
    float currentRawX = (float) absoluteStartX;
    float currentRawY = (float) absoluteStartY;
    
    if(deltaX > deltaY) {
      xStep = deltaX /deltaY;
      yStep = 1 * Math.abs(deltaY);      
    } else {
      yStep = deltaY /deltaX;
      xStep = 1 * Math.abs(deltaX); 
    }
    
    do {
      bitmap[currentRoundedX][currentRoundedY] = 1;
      currentRawX += xStep;
      currentRawY += yStep;
      currentRoundedX = (int) Math.round(currentRawX);
      currentRoundedY = (int) Math.round(currentRawY);
    }
    while(currentRoundedX != absoluteEndX && currentRoundedY != absoluteEndY);
    bitmap[absoluteEndX][absoluteEndY] = 1;
  }
  
  //for debugging of stage 1
  public String printBitMap() {
    String map = "";
    for(int i=0; i<width; i++) {
      for(int j=0; j<height; j++) {
        if(bitmap[i][j] == 0)
          map += " ";
        else
          map += ".";          
      }
      map += "\n";
    }
    return map;
  }

}