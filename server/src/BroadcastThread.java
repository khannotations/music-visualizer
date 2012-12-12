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


class BroadcastThread extends Thread {
  
  boolean running;
  
  public BroadcastThread() {
    running = false;
  }
  
  public byte[] run(int[] pixels, int width, int height) {
    running = true;
    //loadPixels();
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
      running = false;
      return baStream.toByteArray();
  }
  
  public boolean isAvailable() {
    return running;
  }
}
