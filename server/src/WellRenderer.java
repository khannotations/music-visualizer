import processing.core.PGraphics;
import processing.core.PImage;
import ddf.minim.AudioInput;
import ddf.minim.AudioSource;
import ddf.minim.analysis.FFT;

public class WellRenderer extends AudioRenderer {
	
	AudioInput in;
	FFT fft;
	public PGraphics img;
	private PImage g;
	double aura = 0.3, orbit = 0.35;
	int delay = 2;
	float red, green, blue, r3, rotations, width, height;
	
	public WellRenderer(AudioSource source) {
		rotations =  (int) source.sampleRate() / source.bufferSize();
		fft = new FFT(source.bufferSize(), source.sampleRate());  
	}
	
	public void setup() {
		colorMode(RGB, 255, 255, 255);
	    background(0);
	    red = 35;
	    green = 255;
	    blue = 185;
	}

	public void draw() {
		img.beginDraw();
	    if(left != null) {
	      float t = map(millis(),0, delay * 1000, 0, PI);   
	      int n = left.length;
	      // center 
	      float w = (float) (width/2 + cos(t) * width * orbit);
	      float h = (float) (height/2 + sin(t) * height * orbit); 
	      // size of the aura
	      float w2 = (float) (width * aura), h2 = (float) (height * aura);
	      // smoke effect
	      if(frameCount % delay == 0 ) 
	    	  img.image(g, 0, 0, width+1, height+1); 

	      float r1=0, a1=0,  r2=0, a2=0, x1=0, y1=0, x2=0, y2=0;
	      for(int i=0; i <= n; i++)
	      {
	    	  float multiplier = 1;
	    	  if(abs(r2) > 0.25 || abs(r3) >.2) {
	    		  multiplier += max(r2, r3)*100;
	    	  }
	        
	    	  r1 = r2; a1 = a2; x1 = x2; y1 = y2;
	    	  r2 = left[i % n] ;
	    	  r3 = right[i % n] ;
	        
	    	  a2 = map(i,0, n, 0, TWO_PI * rotations);
	    	  x2 = (float) (w + cos(a2) * r2*multiplier * w2);
	    	  y2 = (float) (h + sin(a2) * r2*multiplier * h2);
	        
	    	  img.stroke(red, green, blue, 10);
	    	  img.strokeWeight(1);
	    	  // strokeWeight(dist(x1,y1,x2,y2) / 4);
	    	  if(i>0) img.line(x1, y1, x2, y2);
	      }
	      img.strokeWeight(1);
	      img.stroke(255);
	      img.line(w, height - 20, w, height - 20 + r2*100);
	    }
	    else
	    	println("left is null");
	    img.endDraw();
	}
	
	public void setImg(PGraphics img) {
		this.img = img;
	}
	public PGraphics getImg() {
		return img;
	}
	public void setG(PImage g) {
		this.g = g;
	}
}
