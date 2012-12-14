class MovingThreshold {
  public static final int Length = 1024;
  float avgList[];
  int currentIndex;
    
  public MovingThreshold() {
    avgList = new float[Length];
    currentIndex=0;
  }
    
  private float calculateAverage() {
      float sum=0;
      for(int i=0; i<avgList.length; i++) {
        sum+=avgList[i];
      }
      return sum/avgList.length;
  }
    
  public float getThresh() {
      float doubleAverage = 2*calculateAverage();
      //println(doubleAverage);
      if(doubleAverage<.2)
        return (float) .2;
      else if (doubleAverage>.55)
        return (float) .55;
      return doubleAverage;
  }
    
  public void addValue(float value) {
      if(++currentIndex==Length-1)
        currentIndex=0;
      avgList[currentIndex] = value; 
  }
}

