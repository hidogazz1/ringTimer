class Planner {

float 
centerX,
centerY,
radius;

float
startPlanner,
endPlanner;

int
trackID,
numOfTrackers;

int addedSeconds;

Tracker[]
t;

Button
move,
expand;

  Planner (float tempX, float tempY, float tempR) {
    
    centerX = tempX;
    centerY = tempY;
    radius = tempR;

    trackID = 0;
    numOfTrackers = 1;

    t = new Tracker[j];
    t[0] = new Tracker(centerX,centerY,radius);

    move = new Button(10);
    expand = new Button(10);
  }

	void plan(float start, float time){

    startPlanner = round(start*10);
    endPlanner = round(startPlanner + time*10);

    arc(centerX, centerY, radius*2, radius*2, radians(startPlanner-90), radians(endPlanner-90));

  }

  void controlDisplay(){

    int
    x2,
    y2;

    x2 = int(centerX + radius*1.2*sin(radians(startPlanner)));
    y2 = int(centerY - radius*1.2*cos(radians(startPlanner)));

    move.update(x2,y2);
    move.display();

    x2 = int(centerX + radius*1.2*sin(radians(endPlanner)));
    y2 = int(centerY - radius*1.2*cos(radians(endPlanner)));


    expand.update(x2,y2);
    expand.display();
  }

  void track(){
    
    for (int i = 0; i < numOfTrackers; ++i) {
      if (i == numOfTrackers-1) {
        
        t[i].track(); 

      }else {
        t[i].pause(); 
      }
    }   
  }

  void pause(){

    for (int i = 0; i < numOfTrackers; ++i) {
      t[i].pause();
    }
  }

  void begin(){

    t[trackID] = new Tracker(centerX,centerY,radius);
    t[trackID].begin();
  }

  void resume(){

    if (numOfTrackers < j) {
      
      trackID++;
      numOfTrackers++;
      t[trackID] = new Tracker(centerX,centerY,radius);
      t[trackID].begin();
    }
  }

  int addUp(){

    addedSeconds = 0;
    for (int i = 0; i < numOfTrackers; ++i) {
      
      addedSeconds += t[i].seconds;
    }

    return addedSeconds;
  }
}
