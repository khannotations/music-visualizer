package us.soundulo.soundulous;

import java.io.BufferedInputStream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;

public class TouchableView extends ImageView {	
	View.OnTouchListener gestureListener;
	private int port = -1;
	private String url = "perch.zoo.cs.yale.edu";
	private Handler handler;
	private Thread receiveThread;
	
	public TouchableView(Context context) {
	    super(context);

	    //client = new UDPClient();
	    
	    
	    //gestureDetector = new GestureDetector(getContext(),
	   //         new GestureListener(this));
	    gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				sendCoordinates(event.getX(), event.getY());
				return true;
			}
		};
		this.setOnTouchListener(gestureListener);
		handler = new Handler();
	    startReceiveThread(this);

	}
	
	private void sendCoordinates(float x, float y) {
		//Toast.makeText(this.getContext(), "x is "+x+", y is "+y, Toast.LENGTH_SHORT).show();
		new UDPClient(this.getContext()).execute(x+":"+y, url);
		//Toast.makeText(this.getContext(), "UDP sent", Toast.LENGTH_SHORT).show();
	}
	
	/*
//	public boolean onTouchEvent(MotionEvent event) {
//	    return gestureDetector.onTouchEvent(event);
//	}
	// taken mostly from http://stackoverflow.com/questions/937313/android-basic-gesture-detection
	private class GestureListener extends SimpleOnGestureListener {
		private TouchableView view;

		private GestureListener(TouchableView view) {
			this.view = view;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Toast.makeText(getContext(), "Fling received", Toast.LENGTH_SHORT).show();
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(getContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                    view.setBackgroundColor(Color.parseColor("black"));
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(getContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                    view.setBackgroundColor(Color.parseColor("red"));
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
	} */
	
	public int getPort() {
		return port;
	}
	
	public void setConnection(String urlText, int newPort) {
		receiveThread.interrupt();
		System.out.println("Stopping thread listening to port "+url+port);
		url = urlText;
		port = newPort;
		System.out.println("Starting thread listening to port "+url+port);
		startReceiveThread(this);		
	}
	
	private void startReceiveThread(final ImageView view) {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					URL urlObj = new URL("http://"+url+":"+port);
					URLConnection conn = urlObj.openConnection();
			        conn.connect();
					while (true) {
						System.out.println("in loop");
				        InputStream is = conn.getInputStream();
				        // got input stream
						System.out.println("got input stream");
						BufferedInputStream bis = new BufferedInputStream(is);
						// decoding stream
				        final Bitmap bm = BitmapFactory.decodeStream(bis);
						System.out.println("decoding stream");
				        // decoded stream
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
