/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/5989*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */

 //////////////////////////////////////////////////////////////
 //                                                          //
 //  Music Visualizer                                        //
 //                                                          //
 //  a quick sketch to do WimAmp-style music visualization   //
 //  using Processing and the Minim Library ...              //
 //                                                          //
 //  (c) Martin Schneider 2009                               //
 //                                                          //
 //  Tweeked and adopted for the Android mobile platform     //
 //  by Rafi Khan, Mike Levine, Hari Ganesan,                //
 //     and Jacob Metrick                                    // 
 //  December 12, 2012 for CS 434 at Yale University         //
 //                                                          //
 //                                                          //
 //                                                          //
 //////////////////////////////////////////////////////////////


import ddf.minim.*;

Minim minim;

AudioPlayer song;
// AudioRenderer radar, vortex, iso, graph;
// AudioRenderer[] visuals;
AudioRenderer well;

int select;
 
void setup()
{
   // setup graphics
   size(640, 480, P2D);
   size(320, 240, P2D);
   frameRate(30);
   // setup player
   minim = new Minim(this);
   song = minim.loadFile("beat.mp3", 1024);
   song.loop(); 
   well = new WellRenderer(song);
   song.addListener(well);
    well.setup(); 

  // setup renderers
  /*
  vortex = new VortexRenderer(groove);
  radar = new RadarRenderer(groove);
  iso = new IsometricRenderer(groove);
  
  graph = new VortexRenderer(groove);
  visuals = new AudioRenderer[] {radar, vortex,  iso};
  // activate first renderer in list
  select = 0;
  groove.addListener(visuals[select]);
  visuals[select].setup();
  */
}
 
void draw()
{
  well.draw();
  // visuals[select].draw();
}
void keyPressed() {
  well.keyPress();
}
 
/*void keyPressed() {
   groove.removeListener(visuals[select]);
   select++;
   select %= visuals.length;
   groove.addListener(visuals[select]);
   visuals[select].setup();
}*/

void stop()
{
  song.close();
  minim.stop();
  super.stop();
}


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

/// abstract class for audio visualization

abstract class AudioRenderer implements AudioListener {
  float[] left;
  float[] right;
  synchronized void samples(float[] samp) { left = samp; }
  synchronized void samples(float[] sampL, float[] sampR) { left = sampL; right = sampR; }
  abstract void setup();
  abstract void draw(); 
  abstract void keyPress();
}


// abstract class for FFT visualization

abstract class FourierRenderer extends AudioRenderer {
  FFT fft; 
  float maxFFT;
  float[] leftFFT;
  float[] rightFFT;
  FourierRenderer(AudioSource source) {
    float gain = .125;
    fft = new FFT(source.bufferSize(), source.sampleRate());
    maxFFT =  source.sampleRate() / source.bufferSize() * gain;
    fft.window(FFT.HAMMING);
  }
  
  void calc(int bands) {
    if(left != null) {
      leftFFT = new float[bands];
      fft.linAverages(bands);
      fft.forward(left);
      for(int i = 0; i < bands; i++) leftFFT[i] = fft.getAvg(i);   
    }
  }
}



