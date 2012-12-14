

public class WellRenderer extends AudioRenderer {
  AudioInput in;
  FFT fft;
  double aura = 0.5, orbit = 0.35;
  int delay = 2, imgOffset, smokeDelay = 3;
  float mRed, mGreen, mBlue, r3, rotations;
  
  MovingThreshold thrsh;
  float touchMultiplier, threshold;
  int startingFrameForTap, shiftDirection, shiftStart, addToRight, addUp, shakeMagnitude, currentIndex;
  
  public WellRenderer(AudioSource source) {
    rotations =  (int) source.sampleRate() / source.bufferSize();
    fft = new FFT(source.bufferSize(), source.sampleRate());
  }
  
  public void setup() {
      colorMode(RGB, 255, 255, 255);
      background(0);
      mRed = 35;
      mGreen = 255;
      mBlue = 185;
      currentIndex=0;
      // Lots of potential for changing visualization with this!!
      imgOffset = -1;      // Must be negative
      
      thrsh = new MovingThreshold();
      threshold= (float).2;
      startingFrameForTap = shiftStart = shiftDirection = 0;
      touchMultiplier = 1;
      shakeMagnitude = 50;
  }
  
  public synchronized void draw() {
      if(left != null) {
        float t = map(millis(),0, delay * 1000, 0, PI);   
        int n = left.length;
        // Calculate center 
        float w = (float) (width/2 + cos(t) * width * orbit);
        float h = (float) (height/2 + sin(t) * height * orbit); 
        // Make size of the aura
        float w2 = (float) (width * aura), h2 = (float) (height * aura);
        // Create smoke effect
        if(frameCount % smokeDelay == 0) {
          image(get(), imgOffset, imgOffset, width-2*imgOffset, height-2*imgOffset);
        }
        
        if(startingFrameForTap > 0 && frameCount > startingFrameForTap + 10) {
            touchMultiplier = 1;
            startingFrameForTap = 0;
        }
        if(startingFrameForTap > 0 && frameCount == startingFrameForTap + 1) { 
            mRed = random(255);
            mGreen = random(255);
            mBlue = random(255);
        }
        if(frameCount % 45 == 0)
            threshold = thrsh.getThresh();
        
        addToRight = addUp = 0;
        if(shiftStart!=0 && frameCount<shiftStart+60) {
            if(shiftDirection==1) {
                addToRight = -1;
            } if (shiftDirection==2) {
                addUp = -1;
            } if(shiftDirection==3) {
                addToRight = 1;
            } if(shiftDirection==4) {
                addUp = 1;
              }
        } else {
            shiftStart=0;
            shiftDirection=0;
        }
        
        float r1=0, a1=0,  r2=0, a2=0, x1=0, y1=0, x2=0, y2=0;
        for(int i=0; i <= n; i++) {
            float multiplier = 1;
            if(abs(r2) > threshold || abs(r3) > threshold) {
              multiplier += max(r2, r3)*100;
            }
            if(abs(r2) > 2*threshold || abs(r3) >2*threshold) {   
              mRed = random(255);
              mGreen = random(255);
              mBlue = random(255);
            }
            thrsh.addValue(abs(r2));
          
            r1 = r2; a1 = a2; x1 = x2; y1 = y2;
            r2 = left[i % n] ;
            r3 = right[i % n] ;
          
            a2 = map(i,0, n, 0, TWO_PI * rotations);
            x2 = (float) (w + cos(a2) * r2*multiplier * w2);
            y2 = (float) (h + sin(a2) * r2*multiplier * h2);
          
            stroke(mRed, mGreen, mBlue, 10);
            strokeWeight(1);
            if(i>0) 
                line(x1+ addToRight*shakeMagnitude, y1+ addUp*shakeMagnitude,
                    x2+ addToRight*shakeMagnitude, y2+ addUp*shakeMagnitude);
        }
        strokeWeight(1);
        stroke(255);
        line(w, height - 20, w, height - 20 + r2*100);
      }
  }

  public void keyPress() {
       int keyPress = keyCode;
       //enter which will simulate a tap
       if(keyPress==10) {
         background(0);
         touchMultiplier = 75;
         startingFrameForTap = frameCount;
        }
       
       //Arrow keys which will simulate swipes
       //left
       if(keyPress==37) {
         shiftDirection=1;
         shiftStart=frameCount;
       }
       
       //up
       else if(keyPress==38) {
         shiftDirection=2;
         shiftStart=frameCount;
       }
       
       //right
       else if(keyPress==39) {
         shiftDirection=3;
         shiftStart=frameCount;
       }
       
       //down
       else if(keyPress==40) {
         shiftDirection=4;
         shiftStart=frameCount;
       }
       println("SS "+shiftStart);
    }
}
