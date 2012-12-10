import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import ddf.minim.AudioListener;

abstract class AudioRenderer extends PApplet implements AudioListener {
	public PGraphics img;
	float[] left;
	float[] right;
	public synchronized void samples(float[] samp) {
		left = samp;
	}
	public synchronized void samples(float[] sampL, float[] sampR) {
		left = sampL; right = sampR;
	}
	public abstract void setup();
	public abstract void draw(); 
	public abstract void setImg(PGraphics img);
	public abstract PGraphics getImg();
	public abstract void setG(PImage g);
}
