package us.soundulo.soundulous;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	
	private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
        FrameLayout frame = (FrameLayout) findViewById(R.id.touchable_holder);  
        TouchableView touchable = new TouchableView(this);
        frame.addView(touchable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.start_session:
        		return true;
        	case R.id.join_session:
        		return true;
            case R.id.menu_settings:
            	// launch menu settings
            	Intent launcherIntent = new Intent(this, SettingsActivity.class);
            	startActivity(launcherIntent);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
