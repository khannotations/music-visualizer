import processing.core.PApplet;
import ddf.minim.AudioListener;


public abstract class AudioRenderer extends PApplet implements AudioListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	float[] left;
	float[] right;
	
	@Override
	public synchronized void samples(float[] samp) {
		left = samp;
	}
	@Override
	public synchronized void samples(float[] sampL, float[] sampR) {
		left = sampL; right = sampR;
	}
	
	public abstract void setup();
	public abstract void draw();
}
