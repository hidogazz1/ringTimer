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

int percentage;

color 
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

	void display(){

		float 
		 x2,
		 y2,
		 deg,
		 distance = 1.08;

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

	void hourHand(){

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
		text(nf(int(hours),2), x2, y2-2);
		text(nf(minute(),2), x2, y2+12);
	}

	void overlay(){

		
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

	void planInitiate(){

		for (int i = 1; i < numOfPlanners; ++i) {
			
			p[i] = new Planner(centerX, centerY, radius);
		}
	}

	void edit(float start, float time){

		starts[planID] = start;

		if (planID > 0) {
			
			startPlan[planID] = startPlan[planID - 1] + timePlan[planID - 1] + start; // check the last plan arc and start from there
		}else {
			
			startPlan[planID] = start;
		}
		
		timePlan[planID] = time;
	}

	void plan(){

		for (int i = 0; i < numOfPlanners; ++i) {

			if (!saved) {
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

	void secondsHand(){

		color pl = #3b2da6;
		color p2 = #7f47c4;

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
		ellipse(centerX, centerY, radius*1.05, radius*1.05);

		if (switchColor) {
			
			fill(p1);
		}else {
			fill(p2);
		}
		arc(centerX, centerY, radius*1.05, radius*1.05, radians(-90), radians(second()*6-90));
	}

	void capsule(){

		int 
		timerH,
		timerM,
		timerS;

		fill(50);
		ellipse(centerX, centerY, radius, radius);

		textAlign(CENTER);
		fill(200);

		timerH = int(p[planID].addUp()/3600);
		timerM = int(p[planID].addUp()/60 - timerH*3600/60);
		timerS = int(p[planID].addUp() - timerM*60 - timerH * 3600);

		textSize(24);
		text(nf(timerH,2)+":"+nf(timerM,2)+":"+nf(timerS,2), centerX, centerY+60);

		percentage = round(p[planID].addUp()/(timePlan[planID]*12));

		fill(#b8e693);
		textSize(64);
		text(nf(percentage,2)+"%", centerX, centerY+15);

	}

	void track(){
			
			p[planID].track();

			for (int i = 0; i < numOfPlanners; ++i) {
			
			p[i].pause();
			}
	}

	void begin(){
	    	
	    	p[planID].begin(); 
	    }

	void pause(){

		for (int i = 0; i < numOfPlanners; ++i) {
			
			p[i].pause();
			println("i: "+i);
		}
	}

	void resume(){

		p[planID].resume(); 
	}

}
