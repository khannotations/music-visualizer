

class RadarRenderer extends AudioRenderer {
  
  float aura = .3;
  float orbit = .35;
  int delay = 2;
  float currentR;
  float currentG;
  float currentB;
  BeatDetect bd; 
  AudioInput in;
  //BeatListener bl;
  int rotations;
  int Kcount, snareCount;
  float r3;
  FFT fft;
  MovingThreshold thrsh;
  float threshold;
  
  
  
  RadarRenderer(AudioSource source) {
    rotations =  (int) source.sampleRate() / source.bufferSize();
  }
  
  void setup() {
    colorMode(RGB, TWO_PI * rotations, 1, 1);
    background(0);
    frameRate(30);
    currentR = 35;
    currentB = 185;
    currentG = 255;
    thrsh = new MovingThreshold();
    threshold=.2;
    in = minim.getLineIn();
    bd = new BeatDetect();
    Kcount = 0;
    snareCount = 0;
    //bl = new BeatListener(beat, in);
    fft = new FFT(in.bufferSize(), in.sampleRate());

  }
 
  
  synchronized void draw()
  {
    bd.detect(in.mix);
    fft.forward(in.mix);
    int maxF=0;
    float maxval=0.0;
    for(int i=0; i<25600; i++) {
      float value = fft.getBand(i);
      if(maxval < value) {
        maxF = i;
        maxval = value;
      }
    } 
    //println(maxF + " " + maxval);
    
    colorMode(RGB, 255, 255, 255);
    if(left != null && frameCount < 5000) {
   
      float t = map(millis(),0, delay * 1000, 0, PI);   
      int n = left.length;
      
      // center 
      float w = width/2 + cos(t) * width * orbit;
      float h = height/2 + sin(t) * height * orbit; 
      
      // size of the aura
      float w2 = width * aura, h2 = height * aura;
      
      // smoke effect
      if(frameCount % delay == 0 ) image(g,0,0, width+1, height+1); 
      
      // draw polar curve 
      float r1=0, a1=0, x1=0, y1=0, r2=0, a2=0, x2=0, y2=0; 
      
      if(bd.isKick()) {
        //currentG = (currentG + 92)%255;
        //println("kick");
      } 
      //if(bd.isHat())println("onset" + snareCount++);
      
      if(frameCount%45 == 0)
        threshold=thrsh.getThresh();
      
      for(int i=0; i <= n; i++)
      {
        float multiplier = 1;
         if(abs(r2) > threshold || abs(r3) > threshold) {
          multiplier += max(r2, r3)*100;
        }
        if(abs(r2) > 2*threshold || abs(r3) >2*threshold) {   
          currentG = random(255);
          currentR = random(255);
          currentB = random(255);
       }
       thrsh.addValue(abs(r2));
        
        r1 = r2; a1 = a2; x1 = x2; y1 = y2;
        r2 = left[i % n] ;
        r3 = right[i % n] ;
        
        a2 = map(i,0, n, 0, TWO_PI * rotations);
        x2 = w + cos(a2) * r2*multiplier * w2;
        y2 = h + sin(a2) * r2*multiplier * h2;
        
        
        stroke(currentR, currentG, currentB, 10);
        strokeWeight(1);
        // strokeWeight(dist(x1,y1,x2,y2) / 4);
        if(i>0) line(x1, y1, x2, y2);
        
        
        
      }
      
      strokeWeight(2);
        stroke(255);
        line(w, height - 20, w, height - 20 + r2*100);
    }
  }
}


class MovingThreshold {
  public static final int Length = 1024;
  float list[];
  int currentIndex;
  
  public MovingThreshold() {
    list = new float[Length];
    currentIndex=0;
  }
  
  private float calculateAverage() {
    float sum=0;
    for(int i=0; i<list.length; i++) {
      sum+=list[i];
    }
    return sum/list.length;
  }
  
  public float getThresh() {
    float doubleAverage = 2*calculateAverage();
    println(doubleAverage);
    if(doubleAverage<.2)
     return .2;
    else if (doubleAverage>.55)
     return .55;
    return doubleAverage;
  }
  
  public void addValue(float value) {
    if(++currentIndex==Length-1)
      currentIndex=0;
    list[currentIndex] = value; 
  }
}
