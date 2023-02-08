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
saved = true,
c,
ready = false,
inRange,
firstBegin = true;

color
defaultColor = #9c3125,
readyColor = #a0d971,
pauseColor = #6a6582,
setColor = #c9673c,
trackingColor = #3b2ea3;

void setup() {
	
	colorMode(HSB);
	size(512, 512);
	clk = new Clock(width/2, height/2, 200);
	clk.edit(planBegin,planTime);
}

void draw() {
	
	background(50);


	HourTime();

	clk.display();
	clk.plan();
	

	if (!saved) {

		if (!ready) {

	    	inRange = CurrentPlanRange();
		}

	    if (inRange) {
	    	 ready = true;
	    }else {
	    	ready = false;
	    }

	    if (ready) {
			/*	
			fill(250);
			textSize(18);
			text("Ready", 50, 55);
			fill(readyColor);*/
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
					text(nf(int(clk.startPlan[planID]*1200 - CurrentTime()),2),50,60);
				}
				//strokeWeight(1);
			
	}else 
		if (!saved) {
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

}

void keyPressed(){

	if (key == 'f' && saved) {

		if (planBegin + planTime < AfterPlan()) {
			
			planTime++;
			clk.starts[planID+1]--;
			clk.edit(planBegin,planTime);
		}

	}

	if (key == 'd' && planTime > 1 && saved) {
		
		planTime--;
		clk.starts[planID+1]++;
		clk.edit(planBegin,planTime);
	}


	if (key == 'r' && saved) {


		if (planBegin + planTime < AfterPlan()) {
			
			planBegin++;

			clk.starts[planID+1]--;
			clk.edit(planBegin,planTime);
		}
	}

	if (key == 'e' && saved) {

		if (planBegin > BeforePlan()) {
			
			planBegin--;
			clk.starts[planID+1]++;
			clk.edit(planBegin,planTime);
		}

	}

	if (key == 's' && numOfPlanners < j && planID == numOfPlanners-1 && saved) {
		
		if (AfterPlan() - planBegin - planTime > planTime) {
			
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
	}

	if (key == 'q' && saved) {

		if (planID > 0) {
			
			planID --;
		}else {
			
			planID = numOfPlanners-1;
		}

			planBegin = clk.starts[planID];
			planTime = clk.timePlan[planID];

		
	}

	if (key == 'w' && planID < numOfPlanners && saved) {
		
		if (planID == numOfPlanners-1) {
			
			planID = 0;
		}else {
			
			planID++;
		}

			planBegin = clk.starts[planID];
			planTime = clk.timePlan[planID];
	}

	if (key == 'c' && ready) {
		c=!c;

		
		if (c && CurrentTime() >= clk.startPlan[planID]*1200) {
			clk.resume();
		}
	}
}
