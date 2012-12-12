
/*
 * 
 */

public class SessionManager {
	private int[][] bitmap;
	// TODO: NOT RIGHT HEIGHT OR WIDTH DUH
	private final int height = 90;
	private final int width = 160;
	private String sessionId;
	
	public SessionManager(String id) {
	    bitmap = new int[width][height];
	    sessionId = id;
	    initBitmap();
	}
	// Still to do
	public boolean joinSession() {
		return true;
	}
	public String getId() {
		return sessionId;
	}
	private void initBitmap() {
		for(int i=0; i<width; i++) {
			for(int j=0; j<height; j++) {
				bitmap[i][j] = 0;
			}
		}
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
		int absoluteStartX = (int) (startX * width);
		int absoluteStartY = (int) (startY * height);
		int absoluteEndX = (int) (endX * width);
		int absoluteEndY = (int) (endY * height);
    float deltaX = absoluteStartX - absoluteEndX;
		float deltaY = absoluteStartY - absoluteEndY;
    int currentRoundedX = absoluteStartX;
    int currentRoundedY = absoluteStartY;
    float xStep = 1;
    float yStep = 1;
    // Unrounded values
    float currentRawX = (float) absoluteStartX;
    float currentRawY = (float) absoluteStartY;
    
    if (deltaX == 0.0) {
      xStep = 0;
      yStep = -1 * isPositive(deltaY);
    } else if (deltaY == 0.0) {
      xStep = -1 * isPositive(deltaX);;
      yStep = 0;
    } else if(Math.abs(deltaX) > Math.abs(deltaY)) {
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
    // What is this while loop doing, Mike?
    while(currentRoundedX != absoluteEndX && currentRoundedY != absoluteEndY);
    bitmap[absoluteEndX][absoluteEndY] = 2;
    bitmap[absoluteStartX][absoluteStartY] = 2;
	}
	// For debugging of stage 1
	public String printBitMap() {
	    String map = "";
	    for(int i=0; i<height; i++) {
	    	map += i+": ";
	    	for(int j=0; j<width; j++) {
	    		if(bitmap[j][i] == 0)
	    			map += " ";
	    		else if(bitmap[j][i] == 1)
	    			map += "+";
	    		else
	    			map += "=";
	    	}
	    	map += "\n";
	    }
	    return map;
	}
}