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

  Planner (float tempX, float tempY, float tempR) {
    
    centerX = tempX;
    centerY = tempY;
    radius = tempR;

    trackID = 0;
    numOfTrackers = 1;

    t = new Tracker[j];
    t[0] = new Tracker(centerX,centerY,radius);
  }

	void plan(float start, float time){

    startPlanner = round(start*10);
    endPlanner = round(startPlanner + time*10);

    arc(centerX, centerY, radius*2, radius*2, radians(startPlanner-90), radians(endPlanner-90));

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
