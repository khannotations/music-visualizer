package us.soundulo.soundulous;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class TouchableView extends View {

	private class GestureListener extends SimpleOnGestureListener {
		private TouchableView view;
		
		public GestureListener (TouchableView view) {
			this.view = view;
		}
	}

	
	private GestureDetector gestures;
	
	public TouchableView(Context context) {
	    super(context);
	    gestures = new GestureDetector(getContext(),
	            new GestureListener(this));
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	    return gestures.onTouchEvent(event);
	}
}
