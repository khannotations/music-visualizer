package us.soundulo.soundulous;

import android.content.Context;
import android.graphics.Color;
//import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TouchableView extends View {	
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	//UDPClient client;
	//Context context = this.context;
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	public TouchableView(Context context) {
	    super(context);

	    //client = new UDPClient();
	    
	    
	    //gestureDetector = new GestureDetector(getContext(),
	   //         new GestureListener(this));
	    gestureListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				sendCoordinates(event.getX(), event.getY());
				return true;
			}
		};
		this.setOnTouchListener(gestureListener);
	}
	
	private void sendCoordinates(float x, float y) {
		//Toast.makeText(this.getContext(), "x is "+x+", y is "+y, Toast.LENGTH_SHORT).show();
		try {
			new UDPClient(this.getContext()).execute("x:"+x+"y:"+y);
			//Toast.makeText(this.getContext(), "trying UDP", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
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
	}
}
