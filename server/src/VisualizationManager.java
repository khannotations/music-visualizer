/*
 * http://www.ctctlabs.com/index.php/blog/detail/how_to_use_processing_from_the_command_line_to_generate_images/
 * 
 */
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

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
	BroadcastThread broadcast; //thread for the broadcasting
	ReceiveThread receive;  //thread to receive touch events
	
	private ArrayList<SocketAddress> addresses; 
	private DatagramSocket ds; 
	byte[] buffer;					// Buffer into which to read client data
	
	public VisualizationManager(String id) {
		port = Integer.parseInt(id);
		addresses = new ArrayList<SocketAddress>();
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("New VisualizationManager on port "+port);
	}
	
	@Override
	public void setup() {
		size(640, 480);				// Standard phone size (landscape)
		frameRate(30);				// As good as we'll ever need
		
		js = new JSMinim(this);
		minim = new Minim(js);
		song = minim.loadFile("beat.mp3", 1024);
		song.loop();
		
		well = new WellRenderer(this, song);
		song.addListener(well);
		well.setup();

		server = new Server(this, port);
		buffer = new byte[1024];

		broadcast = new BroadcastThread(width, height);
		receive = new ReceiveThread(port);
    	try {
    		broadcast.start();
    		receive.start();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
	@Override
	public void draw() {
		if(receive.available()) {
			try {
				well.touchEvent(receive.getData());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		well.draw();
		loadPixels();
		broadcast.updatePixels(pixels);
		if(broadcast.available()) {
			server.write(broadcast.getData());
		}
	}
	
	@Override
	public void keyPressed() {
		well.keyPressed();
	}
	
	public void stop() {
		song.close();
		minim.stop();
		super.stop();
	}
	
	void receive() {
		
	}

	// Function to broadcast a PImage over the Server (UDP probably)
	// Special thanks to: http://ubaa.net/shared/processing/udp/
	// (This example doesn't use the library, but you can!)
	public void addSockAddress(SocketAddress address) {
		addresses.add(address);
	}
}

