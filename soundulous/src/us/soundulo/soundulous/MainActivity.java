package us.soundulo.soundulous;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends Activity{
	
	private int port = -1; // holds current port; -1 if not connected
	
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
        	case R.id.test_button:
        		System.out.println(port);
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
			StringBuilder response = new StringBuilder();
    		String urlString = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
    				getString("server_url", "");
    		try {
    			URL url = new URL("http", urlString, 6789, "/new"); // make url object
    			HttpURLConnection httpconn = (HttpURLConnection) url.openConnection(); // connect to HTTP
    			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) { // if valid connection
    				BufferedReader input = new BufferedReader(
    						new InputStreamReader(httpconn.getInputStream()), 8192); // read input
    				String strLine = null; 
    				while ((strLine = input.readLine()) != null) {
    					response.append(strLine);
    				}
    				input.close();
    			}
    			httpconn.disconnect();
    			port = Integer.parseInt(response.toString());
    			
    		}
    		catch (Exception e) {
				System.out.println("Exception:" + e.getMessage());
    		}
    	}
    }
}
