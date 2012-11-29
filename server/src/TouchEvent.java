/*
 * 
 */

public class TouchEvent {
  private float startX;
  private float startY;
  private float endX;
  private float endY;
  
  public TouchEvent(float x1, float y1, float x2, float y2) {
    startX = x1;
    startY = y1;
    endX = x2;
    endY = y2;
  }

  public float[] getStartingPoint() {
    float[] start = new float[2];
    start[0] = startX;
    start[1] = startY;
    
    return start;
  }
  
  public float[] getEndingPoint() {
    float[] end = new float[2];
    end[0] = endX;
    end[1] = endY;
    
    return end;
  }

}