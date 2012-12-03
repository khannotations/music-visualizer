import processing.net.*;
Server myServer;
Client myClient;
int dataIn;
int val = 0;

void setup() {
  size(200, 200);
  // Starts a myServer on port 5204
  myServer = new Server(this, 5204); 
  myClient = new Client(this, "127.0.0.1", 5204);
}

void draw() {
  if(frameCount % 20 == 0) {
    val = (val + 1) % 255;
    // background(val);
    println("Server writes: "+ val);
    myServer.write(val);
  }
}

// ClientEvent message is generated when the server 
// sends data to an existing client.
void clientEvent(Client someClient) {
  dataIn = myClient.read();
  if(dataIn > -1) {
    print("Server receives:  ");
    println(dataIn);
    background(dataIn);
  }
}
