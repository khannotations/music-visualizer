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
 //  Tweeked and adopted for the Android mobile platform     //
 //  by Rafi Khan, Mike Levine, Hari Ganesan,                //
 //     and Jacob Metrick                                    // 
 //  December 12, 2012 for CS 434 at Yale University         //
 //                                                          //
 //                                                          //
 //                                                          //
 //////////////////////////////////////////////////////////////


import ddf.minim.*;

Minim minim;

AudioPlayer song;
// AudioRenderer radar, vortex, iso, graph;
// AudioRenderer[] visuals;
AudioRenderer well;

int select;
 
void setup()
{
   // setup graphics
   size(640, 480, P2D);
   size(320, 240, P2D);
   frameRate(30);
   // setup player
   minim = new Minim(this);
   song = minim.loadFile("beat.mp3", 1024);
   song.loop(); 
   well = new WellRenderer(song);
   song.addListener(well);
    well.setup(); 

  // setup renderers
  /*
  vortex = new VortexRenderer(groove);
  radar = new RadarRenderer(groove);
  iso = new IsometricRenderer(groove);
  
  graph = new VortexRenderer(groove);
  visuals = new AudioRenderer[] {radar, vortex,  iso};
  // activate first renderer in list
  select = 0;
  groove.addListener(visuals[select]);
  visuals[select].setup();
  */
}
 
void draw()
{
  well.draw();
  // visuals[select].draw();
}
void keyPressed() {
  well.keyPress();
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
  song.close();
  minim.stop();
  super.stop();
}


