class Tracker {

float 
centerX,
centerY,
radius;

float 
beginTrack;

float
minute,
seconds,
startCounter;

float
beginAngle,
secondsAngle;

boolean firstSecond = true;

  Tracker (float tempX, float tempY, float tempR) {
    
    centerX = tempX;
    centerY = tempY;
    radius = tempR;
  }

  void begin(){

    startCounter = second();
    beginTrack = CurrentTime();
  }

  void track(){

    if (second() == 0 && firstSecond) {
      minute = seconds +1;
      startCounter = 0;
      firstSecond = false;
    }else if (second()>0) {
      firstSecond = true;
    }

    seconds = minute + second() - startCounter;

    println("seconds: "+seconds);

    beginAngle = beginTrack/120;
    secondsAngle = seconds/120;

    arc(centerX, centerY, radius*2, radius*2, radians(beginAngle-90),radians(beginAngle + secondsAngle-90));
  }

  void pause(){

    beginAngle = beginTrack/120;
    secondsAngle = seconds/120;

    arc(centerX, centerY, radius*2, radius*2, radians(beginAngle-90),radians(beginAngle + secondsAngle-90) );
  }

}
