package us.soundulo.soundulous;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TouchableView extends ImageView {

	private class GestureListener extends SimpleOnGestureListener {
		private TouchableView view;
		
		public GestureListener (TouchableView view) {
			this.view = view;
		}
	}

	private int port = -1;
	private GestureDetector gestures;
	private Handler handler;
	private Thread receiveThread;
	
	public TouchableView(Context context) {
	    super(context);
	    gestures = new GestureDetector(getContext(),
	            new GestureListener(this));
	    handler = new Handler();
	    startReceiveThread(this);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	    return gestures.onTouchEvent(event);
	}
	
	public int getPort() {
		return port;
	}
	
	public int setPort(int newPort) {
		receiveThread.interrupt(); // interrupt the running thread
		System.out.println("Stopping thread listening to port "+port);
		port = newPort;
		System.out.println("Starting thread listening to port "+port);
		startReceiveThread(this);
		return port;
	}
	
	private void startReceiveThread(final ImageView view) {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://rattlesnake.zoo.cs.yale.edu:10001");
					URLConnection conn = url.openConnection();
			        conn.connect();
					while (true) {
				        InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
				        final Bitmap bm = BitmapFactory.decodeStream(bis);
				        handler.post(new Runnable() { // post to UI thread
				        	public void run() {
				        		view.setImageBitmap(bm);
				        	}
				        });
					}
				}
				catch (Exception e) {
					System.out.println("Exception (LISTENING THREAD DEAD):" + e.getMessage());
	    		}
			}
		};
		receiveThread = new Thread(runnable);
		receiveThread.start();
	}
	
	static class FlushedInputStream extends FilterInputStream {
		 
	    /**
	     * The constructor that takes in the InputStream reference.
	     *
	     * @param inputStream the input stream reference.
	     */
	    public FlushedInputStream(final InputStream inputStream) {
	        super(inputStream);
	    }
	 
	    /**
	     * Overriding the skip method to actually skip n bytes.
	     * This implementation makes sure that we actually skip 
	     * the n bytes no matter what.
	     * {@inheritDoc}
	     */
	    @Override
	    public long skip(final long n) throws IOException {
	        long totalBytesSkipped = 0L;
	        //If totalBytesSkipped is equal to the required number 
	        //of bytes to be skipped i.e. "n"
	        //then come out of the loop.
	        while (totalBytesSkipped < n) {
	            //Skipping the left out bytes.
	            long bytesSkipped = in.skip(n - totalBytesSkipped);
	            //If number of bytes skipped is zero then 
	            //we need to check if we have reached the EOF
	            if (bytesSkipped == 0L) {
	                //Reading the next byte to find out whether we have reached EOF.
	                int bytesRead = read();
	                //If bytes read count is less than zero (-1) we have reached EOF.
	                //Cant skip any more bytes.
	                if (bytesRead < 0) {
	                    break;  // we reached EOF
	                } else {
	                    //Since we read one byte we have actually 
	                    //skipped that byte hence bytesSkipped = 1
	                    bytesSkipped = 1; // we read one byte
	                }
	            }
	            //Adding the bytesSkipped to totalBytesSkipped
	            totalBytesSkipped += bytesSkipped;
	        }        
	        return totalBytesSkipped;
	    }
	}
}
