import java.util.LinkedList;
import java.util.Queue;

/*
 * 
 */

public class SessionManager {
	private Queue<TouchEvent> queueOfEvents;
	private int[][] bitmap;
	// TODO: NOT RIGHT HEIGHT OR WIDTH DUH
	private final int height = 100;
	private final int width = 100;
	private String sessionId;
	
	public SessionManager() {
		queueOfEvents = new LinkedList<TouchEvent>();
	    bitmap = new int[width][height];
	    sessionId = generateId();
	    initBitmap();
	}
	// Still to do
	public boolean joinSession() {
		return true;
	}
	public String getSessionId() {
		return sessionId;
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
	private int isPositive(float x) {
		if(x > 0)
			return 1;
		else if (x < 0)
			return -1;
		else
			return 0;
	}
	//for stage 1, a function to draw the lines
	public void updateBitMap(float startX, float startY, float endX, float endY) {
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
	    
	    if(Math.abs(deltaX) > Math.abs(deltaY)) {
	    	xStep = deltaX /Math.abs(deltaY) * -1;
	    	yStep = -1 * isPositive(deltaY);      
	    } else {
	    	yStep = deltaY /Math.abs(deltaX) * -1;
	    	xStep = -1 * isPositive(deltaX);
	    } 
    
	    do {
	    	currentRoundedX = (int) Math.round(currentRawX);
	    	currentRoundedY = (int) Math.round(currentRawY);
	    	bitmap[currentRoundedX][currentRoundedY] = 1;
	    	currentRawX += xStep;
	    	currentRawY += yStep;
	    }
	    while(currentRoundedX != absoluteEndX && currentRoundedY != absoluteEndY);
	    bitmap[absoluteEndX][absoluteEndY] = 2;
	    bitmap[absoluteStartX][absoluteStartY] = 2;
	}
	//for debugging of stage 1
	public String printBitMap() {
	    String map = "";
	    for(int i=0; i<height; i++) {
	    	for(int j=0; j<width; j++) {
	    		if(bitmap[j][i] == 0)
	    			map += " ";
	    		else if(bitmap[j][i] == 1)
	    			map += "+";
	    		else
	    			map += "=";
	    	}
	    	map += "\n";
	    } System.out.println("map " + map);
	    return map;
	}
}