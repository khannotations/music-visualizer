/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/5989*@* */
/* !do not delete the line above, required for linking your tweak if you re-upload */

 //////////////////////////////////////////////////////////////
 //                                                          //
 //  Music Visualizer                                        //
 //                                                          //
 //  a quick sketch to do WimAmp-style music visualization   //
 //  using Processing and the Minim Library ...              //
 //                                                          //
 //  (c) Martin Schneider 2009                               //
 //                                                          //
 //////////////////////////////////////////////////////////////


import ddf.minim.*;

Minim minim;

AudioPlayer groove;
AudioRenderer radar, vortex, iso, graph;
AudioRenderer[] visuals; 

int select;
 
void setup()
{
  // setup graphics
  size(640, 480, P3D);
    
  // setup player
  minim = new Minim(this);
  groove = minim.loadFile("paris.mp3", 1024);
  groove.loop();  

  // setup renderers
  vortex = new VortexRenderer(groove);
  radar = new RadarRenderer(groove);
  iso = new IsometricRenderer(groove);
  
  graph = new VortexRenderer(groove);
  
  visuals = new AudioRenderer[] {radar, vortex,  iso};
  
  // activate first renderer in list
  select = 0;
  groove.addListener(visuals[select]);
  visuals[select].setup();
}
 
void draw()
{
  visuals[select].draw();
}
 
/*void keyPressed() {
   groove.removeListener(visuals[select]);
   select++;
   select %= visuals.length;
   groove.addListener(visuals[select]);
   visuals[select].setup();
}*/

void stop()
{
  groove.close();
  minim.stop();
  super.stop();
}


