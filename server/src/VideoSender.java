/*
 * http://www.ctctlabs.com/index.php/blog/detail/how_to_use_processing_from_the_command_line_to_generate_images/
 * 
 */
import processing.core.*;
//import processing.net.*;

public class VideoSender extends PApplet{
	private final int cHeight = 320;
	private final int cWidth = 240;
	//private Server myServer;			// For broadcasting events
	//private Client myClient;
	int dataIn;
	int val = 0;
	
	public void setup() {
		PGraphics context = createGraphics(cWidth, cHeight);
		context.beginDraw();
        // perform drawing using normal Processing methods
        context.background(0);
        context.fill(255);
        context.noStroke();
        context.beginShape();
            context.vertex(100,100);
            context.vertex(300,100);
            context.fill(0);
            context.vertex(300,300);
            context.vertex(100,300);
        context.endShape();
        context.save("test.jpg");
		
	}
	public void draw() {
		
	}
}
