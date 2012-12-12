import java.net.*;
import java.io.IOException;

class UDPServerThread extends Thread
{
  int[][] recentCoords;
  int currentIndex;
  
  public UDPServerThread() {
    recentCoords = new int[200][2];
    currentIndex=0;
  }
  
  public void run()
  {
    DatagramSocket serverSocket = null;
    try {
      serverSocket = new DatagramSocket(6799);
    } catch (Exception e) {
      System.out.println("error");
    }
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    while(true)
    {
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      System.out.println("about to receive packet");
      try {
        serverSocket.receive(receivePacket);
      } catch(IOException e) {
        e.printStackTrace();
      }
      System.out.println("just received packet");
      String[] vals = receivePacket.getData().split(":");
      recentCoords[currentIndex][0] = vals[0];
      recentCoords[currentIndex++][1] = vals[1];
      String sentence = new String( receivePacket.getData());
      System.out.println("RECEIVED: " + sentence);
      InetAddress IPAddress = receivePacket.getAddress();
      int port = receivePacket.getPort();
    }
  }
}