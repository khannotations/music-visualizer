
class GraphRenderer extends FourierRenderer {

  int n = 48;
  float squeeze = .5;
  int box_width = 2;
  int y;
  float val[];

  GraphRenderer(AudioSource source) {
    super(source); 
    val = new float[n];
    y = height - 30;
  }

  void setup() {
    //colorMode(HSB, n, n, n);
    colorMode(RGB);
    //rectMode(CORNERS);
    //noStroke();   
    stroke(255);
    
  }

  synchronized void draw() {
    background(0);
    strokeWeight(2);
    stroke(255);
    line(0, y, width, y);
    if(left != null) {  
      
      strokeWeight(1);
      stroke(255, 0, 0);
      noFill();
      // draw coloured slices
      for(int i=0; i < n; i++)
      {
        rect(i*(box_width+2)+30, y-30, box_width, 30);
        /*
        val[i] = lerp(val[i], pow(leftFFT[i] * (i+1), squeeze), .1);
        float x = map(i, 0, n, height, 0);
        float y = map(val[i], 0, maxFFT, 0, width/2);
        pushMatrix();
          translate(x, 0, 0);
          rotateX(PI/16 * i);
          fill(i, n * .7 + i * .3, n-i);
          box(dy, dx + y, dx + y);
        popMatrix();
        */
      }
    }
  }
}


