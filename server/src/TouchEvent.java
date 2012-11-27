/*
 * 
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import org.json.simple.JSONValue;

public class TouchEvent {
  private String type;
  private float[] coords;
  public TouchEvent(String type, float[] coords) {
    type = type;
    coords = coords;
  }

  public float[] getCoords() {
    return coords;
  }
  public String getType() {
    return type;
  }

}