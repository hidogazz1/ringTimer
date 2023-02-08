float AfterPlan(){

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

float AfterPlanMouse(){

  float after;

  if (planID == numOfPlanners-1) {
    
    after = 36;
    
  }else {
    after = clk.startPlan[planID + 1];
  }

  return after;
}

float BeforePlan(){

  float before;

  if (planID == 0 && planID < numOfPlanners - 1) {
    
    before = clk.startPlan[numOfPlanners-1] + clk.timePlan[numOfPlanners-1]-36;
  }else{

    before = 0;
  }

  return before;
}

float BeforePlanMouse(){

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

float CurrentTime(){

  float 
  current;

  current = hours*3600 + minute()*60 + second();

  return current;
}

boolean CurrentPlanRange(){

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

float HourTime(){

  if (hour() > 12) {

     hours = hour() - 12;
   }else {
     
     hours = hour();
   }

   return hours;
}
