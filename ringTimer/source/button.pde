class Button {

color
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

	 	mouseHoverColor = #fcba03;
		defaultButtonColor = #fc7b03;
		clickedColor = #fc3503;
	}


void hoverChecker(){

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

void display(){
	if (clicked) {
		fill(clickedColor);

	}else if(hovered){
		fill(mouseHoverColor);
	}else {
		fill(defaultButtonColor);
	}
	ellipse(x, y, radius*2, radius*2);
}

void setColors(color tempC1,color tempC2,color tempC3){
	defaultButtonColor = tempC1;
	mouseHoverColor = tempC2;
	clickedColor = tempC3;
}

void update(int tx,int ty){

	x = tx;
	y = ty;

}

void mover(){

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

void radialRestriction(float tx, float ty, float tradius, int tspacing){

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
