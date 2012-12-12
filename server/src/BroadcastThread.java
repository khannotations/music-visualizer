import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

class BroadcastThread extends Thread {
  
	boolean running, available;			// Available is "data is available"
	int width, height;
	int[] pixels;
  
	byte[] data;
  
  	public BroadcastThread(int width, int height) {
  		running = false;
  		this.width = width;
  		this.height = height;
  		data = null;
  	}
    
    public void updatePixels(int[] pixels) {
      this.pixels = pixels;
    }
    
    @Override
    public void start() {
    	running = true;
    	super.start();
    }
    @Override
  	public void run() {
    	while(running) {
    		if(pixels == null) {
        		System.out.println("no pixels...");
        	}
        	else {
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
	      		data = baStream.toByteArray();
	      		available = true;
	    	}
    	}
  	}
  	public boolean available() {
  		return available;
  	}
    public byte[] getData() {
    	available = false;
    	return data;
    }
}
