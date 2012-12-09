package us.soundulo.soundulous;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
	
	public TouchableView(Context context) {
	    super(context);
	    gestures = new GestureDetector(getContext(),
	            new GestureListener(this));
	    handler = new Handler();
	    startRecieveThread(this);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	    return gestures.onTouchEvent(event);
	}
	
	public int getPort() {
		return port;
	}
	
	public int setPort(int newPort) {
		port = newPort;
		return port;
	}
	
	private void startRecieveThread(final ImageView view) {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					DatagramSocket clientsocket=new DatagramSocket(9876);
					byte[] receivedata = new byte[1024];
					while (true) {
				        DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
				        Log.d("UDP", "S: Receiving...");
				        clientsocket.receive(recv_packet);
				        // some debug code
				        InetAddress ipaddress = recv_packet.getAddress();
				        int port = recv_packet.getPort();
				        Log.d("IPAddress : ",ipaddress.toString());
				        Log.d(" Port : ",Integer.toString(port));
				        // RECEIVE AND HANDLE JPEG
				        ByteArrayInputStream bis = new ByteArrayInputStream(receivedata);
				        final Bitmap bp=BitmapFactory.decodeStream(bis); //decode stream to a bitmap image
				        handler.post(new Runnable() { // post to UI thread
				        	public void run() {
				        		view.setImageBitmap(bp);
				        	}
				        });
					}
				}
				catch (Exception e) {
					System.out.println("Exception (LISTENING THREAD DEAD):" + e.getMessage());
	    		}
			}
		};
		new Thread(runnable).start();
	}
}
