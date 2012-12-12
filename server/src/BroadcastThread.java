import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

class BroadcastThread extends Thread {
  
	boolean running;
	int width, height;
	int[] pixels;
  
	byte[] toReturn;
  
  	public BroadcastThread(int width, int height) {
  		running = false;
  		this.width = width;
  		this.height = height;
  		toReturn = null;
  	}
    
    public void updatePixels(int[] pixels) {
      this.pixels = pixels;
    }
    
    @Override
    public void start() {
    	running = true;
    }
    @Override
  	public void run() {
    	while(running) {
    		BufferedImage bimg = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
      		// Transfer pixels from localFrame to the BufferedImage
      		bimg.setRGB( 0, 0, width, height, pixels, 0, width);
      		ByteArrayOutputStream baStream	= new ByteArrayOutputStream();
      		BufferedOutputStream bos		= new BufferedOutputStream(baStream);
      		try {
      			ImageIO.write(bimg, "jpg", bos);
      		} 
      		catch (IOException e) {
      			e.printStackTrace();
      		}
      		toReturn = baStream.toByteArray();
    	}
  	}
  	public boolean isAvailable() {
  		return !running;
  	}
    public byte[] getByteArray() {
      return toReturn;
    }
}
