package us.soundulo.soundulous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

// simple UDP client taken from 
// http://systembash.com/content/a-simple-java-udp-server-and-udp-client/
public class UDPClient extends AsyncTask<String, Void, String> {
	private final static int portNumber = 6799;
	private Context context;
	
	/*public static void main(String input) throws Exception
	   {
	      //BufferedReader inFromUser =
	      //   new BufferedReader(new InputStreamReader(System.in));
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName("localhost");
	      byte[] sendData = new byte[1024];
	      byte[] receiveData = new byte[1024];
	      //String sentence = inFromUser.readLine();
	      //Toast.makeText(null, input, Toast.LENGTH_SHORT).show();
	      sendData = input.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
	      clientSocket.send(sendPacket);
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      clientSocket.receive(receivePacket);
	      //String modifiedSentence = new String(receivePacket.getData());
	      //System.out.println("FROM SERVER:" + modifiedSentence);
	      clientSocket.close();
	   }*/
	
	public UDPClient(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... text) {
		// TODO Auto-generated method stub
		String input = text[0].toString();
		Log.d("UDP", input);
		//Toast.makeText(this.context, input, Toast.LENGTH_SHORT).show();
		//BufferedReader inFromUser =
	      //   new BufferedReader(new InputStreamReader(System.in));
	      DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      InetAddress IPAddress = null;
		try {
			//IPAddress = InetAddress.getByName("localhost");
			IPAddress = InetAddress.getByName(text[1].toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      //byte[] sendData = new byte[1024];
	     // byte[] receiveData = new byte[1024];
	      //String sentence = inFromUser.readLine();
	      //Toast.makeText(null, input, Toast.LENGTH_SHORT).show();
	      byte[] sendData = input.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
	      try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      //DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      //try {
			//clientSocket.receive(receivePacket);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	      //String modifiedSentence = new String(receivePacket.getData());
	      //System.out.println("FROM SERVER:" + modifiedSentence);
	    // clientSocket.close();
		return input;
	}
	
	@Override
    protected void onPreExecute() {
	
    }
	
	@Override
    protected void onPostExecute(String result) {
		//Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
		Log.d("UDP", result);
    }
}
