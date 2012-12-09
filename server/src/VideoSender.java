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
import java.net.InetAddress;
import java.net.SocketException;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class VideoSender extends PApplet{
	private final int cWidth = 320;
	private final int cHeight = 240;
	private int port;
	//private Server myServer;			// For receiving touch events
	//private Client myClient;
	int dataIn;
	int val = 0;
	PGraphics img;
	DatagramSocket ds;
	
	public VideoSender(String id) {
		port = Integer.parseInt(id);
		System.out.println("New videosender on port "+port);
	}
	
	public void setup() {
		size(cWidth, cHeight);
		img = createGraphics(cWidth, cHeight);
		try {
		    ds = new DatagramSocket();
		} catch (SocketException e) {
		    e.printStackTrace();
		}
        // img.save("test.jpg");
	}
	public void draw() {
		img.beginDraw();
		img.background(frameCount%255);
        img.endDraw();
        image(img,0,0);
		broadcast(img);
	}

	// Function to broadcast a PImage over UDP
	// Special thanks to: http://ubaa.net/shared/processing/udp/
	// (This example doesn't use the library, but you can!)
	void broadcast(PImage img) {
		// We need a buffered image to do the JPG encoding
		BufferedImage bimg = new BufferedImage( img.width,img.height, BufferedImage.TYPE_INT_RGB );
		// Transfer pixels from localFrame to the BufferedImage
		img.loadPixels();
		bimg.setRGB( 0, 0, img.width, img.height, img.pixels, 0, img.width);
		// Need these output streams to get image as bytes for UDP communication
		ByteArrayOutputStream baStream	= new ByteArrayOutputStream();
		BufferedOutputStream bos		= new BufferedOutputStream(baStream);
		// Turn the BufferedImage into a JPG and put it in the BufferedOutputStream
		// Requires try/catch
		try {
			ImageIO.write(bimg, "jpg", bos);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		// Get the byte array, which we will send out via UDP!
	  	byte[] packet = baStream.toByteArray();
	  	// Send JPEG data as a datagram
	  	// println("Sending datagram with " + packet.length + " bytes");
	  	try {
	  		ds.send(new DatagramPacket(packet,packet.length, InetAddress.getByName("localhost"), port));
	  	} 
	  	catch (Exception e) {
	  		e.printStackTrace();
	  	}
	}
}

