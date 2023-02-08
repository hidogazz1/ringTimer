import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ringTimer extends PApplet {

Clock clk;

int 
j = 36;

float 
planTime =2,
planBegin,
hours;

int
numOfPlanners = 1,
planID;

boolean 
saved = false,
c,
ready = false,
inRange,
firstBegin = true;

int
defaultColor = 0xff9c3125,
readyColor = 0xffa0d971,
pauseColor = 0xff6a6582,
setColor = 0xffc9673c,
trackingColor = 0xff3b2ea3;

public void setup() {
	
	colorMode(HSB);
	
	clk = new Clock(height/2, height/2, 200);
	clk.edit(planBegin,planTime);
}

public void draw() {
	
	background(50);


	HourTime();

	clk.display();
	clk.plan();
	

	if (saved && ready) {

	    inRange = CurrentPlanRange();
		
	    if (inRange) {
	    	 ready = true;
	    }else {
	    	ready = false;
	    }
	}

	if (!inRange) {
		fill(defaultColor);
	}else if (CurrentTime()>clk.startPlan[planID]*1200) {
		fill(pauseColor);
	}else {
		fill(readyColor);
	}
	

	if (c) {


		if (firstBegin && CurrentTime()>=clk.startPlan[planID]*1200){
			
			clk.begin();
			firstBegin = false;
		}else
			if (CurrentTime() > (clk.startPlan[planID]+clk.timePlan[planID])*1200) {

				firstBegin = true;
				c = false;
				ready = false;
				clk.pause();
				noStroke();
				fill(pauseColor);
				ellipse(50, 50, 80, 80);
				fill(250);
				textSize(18);
				text("Not", 50, 45);
				text("Ready", 50, 65);
				
			}else
				if (CurrentTime() >= clk.startPlan[planID]*1200) {

					clk.track();
					noStroke();
					fill(trackingColor);
					ellipse(50, 50, 80, 80);
					fill(250);
					textSize(16);
					text("Tracking", 50, 55);
					
				}else {
					clk.pause();
					noStroke();
					fill(setColor);
					ellipse(50, 50, 80, 80);

					fill(250);
					textSize(32);
					text(nf(PApplet.parseInt(clk.startPlan[planID]*1200 - CurrentTime()),2),50,60);
				}
			
	}else 
		if (saved) {
			clk.pause();
			noStroke();
			ellipse(50, 50, 80, 80);
			if (CurrentTime() >= clk.startPlan[planID]*1200) {
				fill(250);
				textSize(18);
				text("Paused", 50, 55);
			}else if(inRange){
				fill(250);
				textSize(18);
				text("Ready", 50, 55);
			}else {
				fill(250);
				textSize(18);
				text("Not", 50, 45);
				text("Ready", 50, 65);
			}
			
		}else {
			fill(defaultColor);
			ellipse(50, 50, 80, 80);
		}

	clk.overlay();
	clk.hourHand();
	clk.secondsHand();
	clk.capsule();
	clk.timerDisplay();

	if (!saved) {
		clk.p[planID].controlDisplay();
	}

	if (!saved && !(clk.p[planID].expand.clicked || clk.p[planID].move.clicked)) {

			clk.p[planID].expand.hoverChecker();

			clk.p[planID].move.hoverChecker();

	}

	if (mousePressed) {
			

		if (clk.p[planID].expand.clicked && !clk.p[planID].move.clicked && !saved) {

			println("AfterPlan: "+AfterPlanMouse());

			if (clk.startPlan[planID] + clk.timePlan[planID] <= AfterPlanMouse() && clk.timePlan[planID] >= 1) {
				
				clk.p[planID].expand.mover();
				clk.p[planID].expand.radialRestriction(clk.centerX,clk.centerY,clk.radius,10);
				if (clk.p[planID].expand.thetaI > AfterPlanMouse() || clk.p[planID].expand.thetaI - clk.startPlan[planID] < 1) {
					
				}else {
					planTime = clk.p[planID].expand.thetaI - clk.startPlan[planID];
				}

				clk.editTime(planTime);
			}

		}

		if (clk.p[planID].move.clicked && !clk.p[planID].expand.clicked && !saved) {

			println("planBegin: "+planBegin);
			if (clk.startPlan[planID] + clk.timePlan[planID] <= AfterPlanMouse() && clk.startPlan[planID] >= BeforePlanMouse()) {

				println("clicked: "+clk.p[planID].move.clicked);
				clk.p[planID].move.mover();
				clk.p[planID].move.radialRestriction(clk.centerX,clk.centerY,clk.radius,10);
				if (clk.p[planID].move.thetaI + clk.timePlan[planID] > AfterPlanMouse() || clk.p[planID].move.thetaI < BeforePlanMouse()) {
					
				}else {
					planBegin = clk.p[planID].move.thetaI;
				}

				clk.editStart(planBegin);
			}
		}
	}else {
		clk.p[planID].expand.hoverChecker();

		clk.p[planID].move.hoverChecker();	
	}

}

public void keyPressed(){

	if (key == 'f' && !saved) {

		if (planBegin + planTime < AfterPlan()) {
			
			planTime++;
			clk.starts[planID+1]--;
			clk.edit(planBegin,planTime);
		}

	}

	if (key == 'd' && planTime > 1 && !saved) {
		
		planTime--;
		clk.starts[planID+1]++;
		clk.edit(planBegin,planTime);
	}


	if (key == 'r' && !saved) {


		if (planBegin + planTime < AfterPlan()) {
			
			planBegin++;

			clk.starts[planID+1]--;
			clk.edit(planBegin,planTime);
		}
	}

	if (key == 'e' && !saved) {

		if (planBegin > BeforePlan()) {
			
			planBegin--;
			clk.starts[planID+1]++;
			clk.edit(planBegin,planTime);
		}

	}

	if (key == 's' && numOfPlanners < j && planID == numOfPlanners-1 && !saved) {
		
		if (AfterPlan() - planBegin - planTime > 3) {
			
			planID++;
			numOfPlanners++;
			clk.planInitiate();

			planBegin = 1;
			planTime = 2;
			clk.edit(planBegin,planTime);
		}
	}

	if (key == 'a') {
		
		saved = !saved;
		if (!saved) {
			c = false;
		}

		inRange = CurrentPlanRange();
		if (inRange) {
	    	 ready = true;
	    }else {
	    	ready = false;
	    }
	}

	if (key == 'q' && !saved) {

		if (planID > 0) {
			
			planID --;
		}else {
			
			planID = numOfPlanners-1;
		}

			planBegin = clk.starts[planID];
			planTime = clk.timePlan[planID];

		
	}

	if (key == 'w' && planID < numOfPlanners && !saved) {
		
		if (planID == numOfPlanners-1) {
			
			planID = 0;
		}else {
			
			planID++;
		}

			planBegin = clk.starts[planID];
			planTime = clk.timePlan[planID];
	}

	if (key == 'c' && ready && saved) {
		c=!c;

		
		if (c && CurrentTime() >= clk.startPlan[planID]*1200) {
			clk.resume();
		}
	}
}
class Button {

int
defaultButtonColor,
mouseHoverColor,
clickedColor;

int
thetaI;

float
x,
y,

distanceX,
distanceY;

float
mouseFromCenter,
radius,
theta;

boolean 
released = true,
firstClick = true,
clicked = false,
hovered = false;


	 Button (float tradius) {

	 	radius = tradius;

	 	mouseHoverColor = 0xfffcba03;
		defaultButtonColor = 0xfffc7b03;
		clickedColor = 0xfffc3503;
	}


public void hoverChecker(){

	mouseFromCenter = dist(x, y, mouseX, mouseY);

	if (mouseFromCenter<radius) {
		hovered = true;

	}else {
		hovered = false;
	}

	if (hovered && mousePressed || mousePressed && !released) {

		released = false;
		clicked = true;
	}else if (!mousePressed) {
		released = true;
		clicked = false;
	}
	
}

public void display(){
	if (clicked) {
		fill(clickedColor);

	}else if(hovered){
		fill(mouseHoverColor);
	}else {
		fill(defaultButtonColor);
	}
	ellipse(x, y, radius*2, radius*2);
}

public void setColors(int tempC1,int tempC2,int tempC3){
	defaultButtonColor = tempC1;
	mouseHoverColor = tempC2;
	clickedColor = tempC3;
}

public void update(int tx,int ty){

	x = tx;
	y = ty;

}

public void mover(){

	if (firstClick && mousePressed) {
		distanceX = mouseX - x;
		distanceY = mouseY - y;

		firstClick = false;
	}
	if (hovered && mousePressed || mousePressed && !released) {
		x = mouseX - distanceX;
		y = mouseY - distanceY;

		released = false;
		clicked = true;

	}else if (!mousePressed) {
		firstClick = true;
		clicked = false;
		released = true;
	}
}

public void radialRestriction(float tx, float ty, float tradius, int tspacing){

	if (x-tx>=0 && ty-y>=0) {
		theta = atan((x-tx)/(ty-y));
	}else if (x-tx>0 && ty-y<=0) {
		theta = atan((y-ty)/(x-tx))+HALF_PI;
	}else if (x-tx<=0 && ty-y<=0) {
		theta = atan((tx-x)/(y-ty))+PI;
	}else {
		theta = atan((ty-y)/(tx-x))+PI+HALF_PI;
	}
	
	thetaI = round(theta/radians(tspacing));

	if (clicked) {
		x = tx + (tradius+radius)*sin(thetaI*radians(tspacing));
		y = ty - (tradius+radius)*cos(thetaI*radians(tspacing));
	}
}

}
class Clock{

float 
centerX,
centerY,
radius;

float[]
startPlan,
timePlan,
starts;

boolean
firstColor,
switchColor;

int 
percentage;

int[]
timerH,
timerM,
timerS;

int 
p1,
p2;

Planner [] p;

	Clock (float tempX, float tempY, float tempR) {
		
		centerX = tempX;
		centerY = tempY;
		radius = tempR;

		starts = new float[j];
		startPlan = new float[j];
		timePlan = new float[j];

		timerH = new int[j];
		timerM = new int[j];
		timerS = new int[j];

		p = new Planner[j];
		p[0] = new Planner(centerX, centerY, radius);

		/*
		states:
			
			No Plan
			Ready
			Set
			Paused
			Tracking

		*/


	}

	public void display(){

		float 
		 x2,
		 y2,
		 deg,
		 distance = 1.08f;

		fill(200);
		ellipse(centerX, centerY, radius*2, radius*2);

		for (int i = 1; i < 13; ++i) {  //numbering the clock.
			
		deg = 30*i-90;

		 x2 = centerX + distance*radius*cos(radians(deg));
		 y2 = centerY + distance*radius*sin(radians(deg));

		pushMatrix();

			fill(255);
			textSize(14);
			textAlign(CENTER);
			translate(0, 5);

			text(i, x2, y2);

		popMatrix();

		}
	}

	public void hourHand(){

		float 
		 x2,
		 y2,
		 deg;

		 strokeWeight(3);
		 stroke(10,150);

		 deg = (hours*3600 + minute()*60 + second())/120-90;

		 x2 = centerX + (radius+15)*cos(radians(deg));
		 y2 = centerY + (radius+15)*sin(radians(deg));

		line(centerX, centerY, x2, y2);
		fill(20);
		ellipse(x2, y2, 40, 40);
		fill(200);
		textSize(14);
		textAlign(CENTER);
		text(nf(PApplet.parseInt(hours),2), x2, y2-2);
		text(nf(minute(),2), x2, y2+12);
	}

	public void overlay(){

		
		float 
		 x2,
		 y2,
		 deg;

		fill(50);
		strokeWeight(1);
		stroke(250, 120);

		for (int i = 1; i < 13; ++i) {  //numbering the clock.
			
			deg = 30*i-90;

			x2 = centerX + radius*cos(radians(deg));
			y2 = centerY + radius*sin(radians(deg));

			line(centerX, centerY, x2, y2);

		}
			
	}

	public void planInitiate(){

		for (int i = 1; i < numOfPlanners; ++i) {
			
			p[i] = new Planner(centerX, centerY, radius);
		}
	}

	public void edit(float start, float time){

		starts[planID] = start;

		if (planID > 0) {
			
			startPlan[planID] = startPlan[planID - 1] + timePlan[planID - 1] + start; // check the last plan arc and start from there
		}else {
			
			startPlan[planID] = start;
		}
		
		timePlan[planID] = time;
	}

	public void editStart(float start){
			
		startPlan[planID] = start;
		
	}

	public void editTime(float time){
			
		
		timePlan[planID] = time;
	}

	public void plan(){

		for (int i = 0; i < numOfPlanners; ++i) {

			if (saved) {
				fill(140, 100, 200, 100);
			}else
			if (i == planID) {
				fill(80, 100, 200, 100);
			}else {
				fill(0, 100, 200, 100);
			}
			
			p[i].plan(startPlan[i],timePlan[i]);
		}
	}

	public void secondsHand(){

		int pl = 0xff3b2da6;
		int p2 = 0xff7f47c4;

		if (second() == 0 && firstColor) {
			firstColor = false;
			switchColor = !switchColor;
		}else if (second()>0) {
			firstColor = true;
		}

		noStroke();

		if (switchColor) {
			
			fill(p2);
		}else {
			fill(p1);
		}
		ellipse(centerX, centerY, radius*1.05f, radius*1.05f);

		if (switchColor) {
			
			fill(p1);
		}else {
			fill(p2);
		}
		arc(centerX, centerY, radius*1.05f, radius*1.05f, radians(-90), radians(second()*6-90));
	}

	public void capsule(){

		fill(50);
		ellipse(centerX, centerY, radius, radius);

		textAlign(CENTER);
		fill(200);

		timerH[planID] = PApplet.parseInt(p[planID].addUp()/3600);
		timerM[planID] = PApplet.parseInt(p[planID].addUp()/60 - timerH[planID]*3600/60);
		timerS[planID] = PApplet.parseInt(p[planID].addUp() - timerM[planID]*60 - timerH[planID] * 3600);

		textSize(24);
		text(nf(timerH[planID],2)+":"+nf(timerM[planID],2)+":"+nf(timerS[planID],2), centerX, centerY+60);

		percentage = round(p[planID].addUp()/(timePlan[planID]*12));

		fill(0xffb8e693);
		textSize(64);
		text(nf(percentage,2)+"%", centerX, centerY+15);

	}

	public void track(){
			
			p[planID].track();

			for (int i = 0; i < numOfPlanners; ++i) {
			
			p[i].pause();
			}
	}

	public void begin(){
	    	
	    	p[planID].begin(); 
	    }

	public void pause(){

		for (int i = 0; i < numOfPlanners; ++i) {
			
			p[i].pause();
			println("i: "+i);
		}
	}

	public void resume(){

		p[planID].resume(); 
	}

	public void timerDisplay(){

		int zed = 0;

		rectMode(CENTER);
		fill(pauseColor);
		rect(600, 40+planID*40, 140, 40);

		for (int i = 0; i < numOfPlanners && i < 12; ++i) {
			
			textSize(24);
			fill(255);
			zed = i+1;
			text(nf(timerH[i],2)+":"+nf(timerM[i],2)+":"+nf(timerS[i],2), 600, 50+i*40);
		}
		
	}

}
public float AfterPlan(){

  float after;

  if (planID == 0 && planID == numOfPlanners-1) {
    
    after = 36;
  }else
  if (planID == numOfPlanners-1) {
    
    after = 36 - (clk.startPlan[planID -1] + clk.timePlan[planID -1] - clk.startPlan[0]);
  }else if(planID == 0){

    after = clk.startPlan[planID + 1];
    
  }else {
    after = clk.startPlan[planID + 1] - clk.startPlan[planID-1]-clk.timePlan[planID-1];
  }

  return after;
}

public float AfterPlanMouse(){

  float after;

  if (planID == numOfPlanners-1) {
    
    after = 36;
    
  }else {
    after = clk.startPlan[planID + 1];
  }

  return after;
}

public float BeforePlan(){

  float before;

  if (planID == 0 && planID < numOfPlanners - 1) {
    
    before = clk.startPlan[numOfPlanners-1] + clk.timePlan[numOfPlanners-1]-36;
  }else{

    before = 0;
  }

  return before;
}

public float BeforePlanMouse(){

  float before;

  if (planID == 0 && planID == numOfPlanners - 1) {
    
    before = 0;
    //before = clk.startPlan[numOfPlanners-1] + clk.timePlan[numOfPlanners-1]-36;
  }else if (planID == 0) {
    
    before = clk.startPlan[numOfPlanners-1]+ clk.timePlan[numOfPlanners-1]-36;
    //before = clk.startPlan[planID-1] + clk.timePlan[planID-1];
  }else {

    before = clk.startPlan[planID-1] + clk.timePlan[planID-1];
  }

  return before;
}

public float CurrentTime(){

  float 
  current;

  current = hours*3600 + minute()*60 + second();

  return current;
}

public boolean CurrentPlanRange(){

  boolean planRange = false;

  for (int i = 0; i < numOfPlanners; ++i) {
    if (CurrentTime() >= clk.startPlan[i]*1200-100 && CurrentTime()<= clk.startPlan[i]*1200 + clk.timePlan[i]*1200) {
      planID = i;
      planRange = true;
      break;
    }
  }

  return planRange;
}

public float HourTime(){

  if (hour() > 12) {

     hours = hour() - 12;
   }else {
     
     hours = hour();
   }

   return hours;
}
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

	public void plan(float start, float time){

    startPlanner = round(start*10);
    endPlanner = round(startPlanner + time*10);

    arc(centerX, centerY, radius*2, radius*2, radians(startPlanner-90), radians(endPlanner-90));

  }

  public void controlDisplay(){

    int
    x2,
    y2;

    x2 = PApplet.parseInt(centerX + radius*1.2f*sin(radians(startPlanner)));
    y2 = PApplet.parseInt(centerY - radius*1.2f*cos(radians(startPlanner)));

    move.update(x2,y2);
    move.display();

    x2 = PApplet.parseInt(centerX + radius*1.2f*sin(radians(endPlanner)));
    y2 = PApplet.parseInt(centerY - radius*1.2f*cos(radians(endPlanner)));


    expand.update(x2,y2);
    expand.display();
  }

  public void track(){
    
    for (int i = 0; i < numOfTrackers; ++i) {
      if (i == numOfTrackers-1) {
        
        t[i].track(); 

      }else {
        t[i].pause(); 
      }
    }   
  }

  public void pause(){

    for (int i = 0; i < numOfTrackers; ++i) {
      t[i].pause();
    }
  }

  public void begin(){

    t[trackID] = new Tracker(centerX,centerY,radius);
    t[trackID].begin();
  }

  public void resume(){

    if (numOfTrackers < j) {
      
      trackID++;
      numOfTrackers++;
      t[trackID] = new Tracker(centerX,centerY,radius);
      t[trackID].begin();
    }
  }

  public int addUp(){

    addedSeconds = 0;
    for (int i = 0; i < numOfTrackers; ++i) {
      
      addedSeconds += t[i].seconds;
    }

    return addedSeconds;
  }
}
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

  public void begin(){

    startCounter = second();
    beginTrack = CurrentTime();
  }

  public void track(){

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

  public void pause(){

    beginAngle = beginTrack/120;
    secondsAngle = seconds/120;

    arc(centerX, centerY, radius*2, radius*2, radians(beginAngle-90),radians(beginAngle + secondsAngle-90) );
  }

}
  public void settings() { 	size(720, 512); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ringTimer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
