import ddf.minim.AudioInput;
import ddf.minim.AudioSource;
import ddf.minim.analysis.FFT;

public class WellRenderer extends AudioRenderer {
	
	AudioInput in;
	FFT fft;
	double aura = 0.3, orbit = 0.35;
	int delay = 2, imgOffset;
	float red, green, blue, r3, rotations;
	
	MovingThreshold thrsh;
	float touchMultiplier, threshold;
	int startingFrameForTap, shiftDirection, shiftStart, addToRight, addUp, shakeMagnitude;
	
	private VisualizationManager vm;
	
	public WellRenderer(VisualizationManager vm, AudioSource source) {
		rotations =  (int) source.sampleRate() / source.bufferSize();
		fft = new FFT(source.bufferSize(), source.sampleRate());
		this.vm = vm;
	}
	
	@Override
	public void setup() {
		vm.colorMode(RGB, 255, 255, 255);
	    vm.background(0);
	    width = vm.width;
	    height = vm.height;
	    red = 35;
	    green = 255;
	    blue = 185;
	    // Lots of potential for changing visualization with this!!
	    imgOffset = -1;			// Must be negative
	    
	    thrsh = new MovingThreshold();
	    threshold= (float).2;
	    startingFrameForTap = shiftStart = shiftDirection = 0;
	    touchMultiplier = 1;
	    shakeMagnitude = 50;
	}
	
	@Override
	public synchronized void draw() {
	    if(left != null) {
	      float t = map(vm.millis(),0, delay * 1000, 0, PI);   
	      int n = left.length;
	      // Calculate center 
	      float w = (float) (width/2 + cos(t) * width * orbit);
	      float h = (float) (height/2 + sin(t) * height * orbit); 
	      // Make size of the aura
	      float w2 = (float) (width * aura), h2 = (float) (height * aura);
	      // Create smoke effect
	      if(vm.frameCount % delay == 0) {
	    	  vm.image(vm.g, imgOffset, imgOffset, width-2*imgOffset, height-2*imgOffset);
	      }
	      
	      if(startingFrameForTap > 0 && vm.frameCount > startingFrameForTap + 10) {
	          touchMultiplier = 1;
	          startingFrameForTap = 0;
	      }
	      if(startingFrameForTap > 0 && vm.frameCount == startingFrameForTap + 1) { 
	          red = random(255);
	          green = random(255);
	          blue = random(255);
	      }
	      if(vm.frameCount % 45 == 0)
	          threshold = thrsh.getThresh();

	      float r1=0, a1=0,  r2=0, a2=0, x1=0, y1=0, x2=0, y2=0;
	      for(int i=0; i <= n; i++) {
	    	  float multiplier = 1;
	          if(abs(r2) > threshold || abs(r3) > threshold) {
	        	  multiplier += max(r2, r3)*100;
	          }
	          if(abs(r2) > 2*threshold || abs(r3) >2*threshold) {   
	        	  red = random(255);
	        	  green = random(255);
	        	  blue = random(255);
	          }
	          thrsh.addValue(abs(r2));
	          addToRight = addUp = 0;
	          if(shiftStart!=0 && vm.frameCount<shiftStart+60) {
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
	    	  x2 = (float) (w + cos(a2) * r2*multiplier * w2);
	    	  y2 = (float) (h + sin(a2) * r2*multiplier * h2);
	        
	    	  vm.stroke(red, green, blue, 10);
	    	  vm.strokeWeight(1);
	    	  if(i>0) vm.line(x1+ addToRight*shakeMagnitude, y1+ addUp*shakeMagnitude,
	    			  x2+ addToRight*shakeMagnitude, y2+ addUp*shakeMagnitude);
	      }
	      vm.strokeWeight(1);
	      vm.stroke(255);
	      vm.line(w, height - 20, w, height - 20 + r2*100);
	    }
	}
	
	@Override
	public void touchEvent(String touchEvent) {
		String[] event = touchEvent.split(":");
		//tap
		if(keyPress==10) {
			touchMultiplier = 75;
			startingFrameForTap = vm.frameCount;
	    	}
		//Direction of swipes
		//left
		if(keyPress==37) {
			shiftDirection=1;
			shiftStart = vm.frameCount;
		}
		//up
		else if(keyPress==38) {
			shiftDirection=2;
			shiftStart = vm.frameCount;
		}
		//right
		else if(keyPress==39) {
			shiftDirection=3;
			shiftStart = vm.frameCount;
		}
		//down
		else if(keyPress==40) {
			shiftDirection=4;
			shiftStart = vm.frameCount;
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
	    list[currentIndex] = value; 
	}
}
