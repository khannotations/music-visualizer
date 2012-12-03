package us.soundulo.soundulous;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
        FrameLayout frame = (FrameLayout) findViewById(R.id.touchable_holder);  
        TouchableView touchable = new TouchableView(this);
        frame.addView(touchable);
    }
    
    protected void onResume() {
    	super.onResume();
    }
    
    protected void onPause() {
        super.onPause();
    	//sharedPref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        switch (item.getItemId()) {
        	case R.id.start_session:
        		new Thread(new StartSessionRunnable()).start();
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

    private class StartSessionRunnable implements Runnable {
    	public void run() {
    		String url = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
    				getString("server_url", "");
    		
    	}
    }
}
