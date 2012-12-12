package us.soundulo.soundulous;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.widget.Toast;

// simple UDP client taken from 
// http://systembash.com/content/a-simple-java-udp-server-and-udp-client/
public class UDPClient {
	private final static int portNumber = 6789;
	
	public static void main(String input) throws Exception
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
	      String modifiedSentence = new String(receivePacket.getData());
	      //System.out.println("FROM SERVER:" + modifiedSentence);
	      clientSocket.close();
	   }
}
