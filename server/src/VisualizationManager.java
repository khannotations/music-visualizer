/*
 * http://www.ctctlabs.com/index.php/blog/detail/how_to_use_processing_from_the_command_line_to_generate_images/
 * 
 */
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;
// import processing.core.PGraphics;
import processing.net.Client;
import processing.net.Server;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.javasound.JSMinim;
import ddf.minim.spi.MinimServiceProvider;
import org.tritonus.share.sampled.file.TAudioFileReader;
import javazoom.spi.mpeg.sampled.file.tag.TagParseListener;

public class VisualizationManager extends PApplet {
	/**
	 * Visualization Manager exists to launch different flavors of visualizations
	 * It also handles the broadcasting of the images, launching different threads
	 * to do so on the specified port. 
	 */
	private static final long serialVersionUID = 1L;
	Minim minim;					// Sound manager
	MinimServiceProvider js;
	private AudioRenderer well;		// The renderers
	private AudioPlayer song;		// The song
	private int port;				// Port on which the server listens
	private Server server;			// For receiving touch events
  BroadcastThread thread; //thread for the broadcasting
	
	byte[] buffer;					// Buffer into which to read client data
	
	public VisualizationManager(String id) {
		port = Integer.parseInt(id);
		System.out.println("New VisualizationManager on port "+port);
	}
	
	@Override
	public void setup() {
		size(320, 240);				// Standard phone size (landscape)
		frameRate(30);				// As good as we'll ever need
		
		js = new JSMinim(this);
		minim = new Minim(js);
		song = minim.loadFile("paris.mp3", 1024);
		song.play();
		
		well = new WellRenderer(this, song);
		song.addListener(well);
		well.setup();

		server = new Server(this, port);
		buffer = new byte[16];
    thread = new BroadcastThread();
	}
	
	@Override
	public void draw() {
		well.draw();
		Client client = server.available();
		if(client != null) {
			// Launch new thread to read bytes and process here
			client.readBytes(buffer);
			String input = "<buffer error!>";
			try {
				input = new String(buffer, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			println("Read from "+client.ip()+": "+input);
		}
		// Launch new thread to do the broadcasting here
        //broadcast();
    if(thread.isAvailable()) {
      loadPixels();
      server.write(thread.run(pixels, width, height));
    }
	}
	
	public void keyPressed() {
		well.keyPressed();
	}
	
	public void stop() {
		song.close();
		minim.stop();
		super.stop();
	}

	// Function to broadcast a PImage over the Server (UDP probably)
	// Special thanks to: http://ubaa.net/shared/processing/udp/
	// (This example doesn't use the library, but you can!)
	void broadcast() {
		loadPixels();
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
	  	byte[] packet = baStream.toByteArray();
	  	server.write(packet);
	}
}

