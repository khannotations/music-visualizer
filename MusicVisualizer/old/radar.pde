public float touchMultiplier;
public int startingFrameForTap;
int shiftDirection;
int shiftStart;
  
class RadarRenderer extends AudioRenderer {
  
  float aura = .3;
  float orbit = .35;
  int delay = 5;
  float currentR;
  float currentG;
  float currentB;
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
    colorMode(RGB, 255, 255, 255);
    background(0);
    
    currentR = 35;
    currentB = 185;
    currentG = 255;
    
    thrsh = new MovingThreshold();
    threshold=.2;
    startingFrameForTap=0;
    touchMultiplier=1;
    shiftDirection=0;
    shiftStart=0;
  }
 
  
  synchronized void draw() {
    if(left != null) {
      float t = map(millis(), 0, delay * 1000, 0, PI);   
      int n = left.length;
      // center 
      float w = width/2 + cos(t) * width * orbit;
      float h = height/2 + sin(t) * height * orbit; 
      // size of the aura
      float w2 = width * aura, h2 = height * aura;
      // smoke effect
      if(frameCount % delay == 0 ) image(g, 0, 0, width+1, height+1); 

      //if 10 frames have passed, end the tap multiplier
      if(startingFrameForTap>0 && frameCount>startingFrameForTap+10) {
        touchMultiplier=1;
        startingFrameForTap=0;
      }
      //change color during tap
      if(startingFrameForTap>0 && frameCount==startingFrameForTap+1) { 
        currentG = random(255);
        currentR = random(255);
        currentB = random(255);
      }
      
      // draw polar curve 
      float r1=0, a1=0, x1=0, y1=0, r2=0, a2=0, x2=0, y2=0; 
      
      if(frameCount % 45 == 0)
        threshold=thrsh.getThresh();
      
      for(int i=0; i <= n; i++) {
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
        
        int addToRight=0, addUp=0;
        
        if(shiftStart!=0 && frameCount<shiftStart+60) {
          if(shiftDirection==1) {
            addToRight = -1;
          } else if (shiftDirection==2) {
            addUp = -1;
          } else if(shiftDirection==3) {
            addToRight = 1;
          } else if(shiftDirection==4) {
            addUp = 1;
          }
        } else {
          shiftStart=0;
          shiftDirection=0;
        }
        
        r1 = r2; a1 = a2; x1 = x2; y1 = y2;
        r2 = left[i % n] ;
        r3 = right[i % n] ;
        
        a2 = map(i,0, n, 0, TWO_PI * rotations);
        x2 = (w + cos(a2) * r2*multiplier * w2 * touchMultiplier);
        y2 = (h + sin(a2) * r2*multiplier * h2 * touchMultiplier);
        
        stroke(currentR, currentG, currentB, 10);
        strokeWeight(1);
        // strokeWeight(dist(x1,y1,x2,y2) / 4);
        if(i>0) line(x1+ addToRight*75, y1+ addUp*75, x2+ addToRight*75, y2+ addUp*75);
      }
      //strokeWeight(1);
      //stroke(255);
      //line(w, height - 20, w, height - 20 + r2*100);
    }
  }
}
