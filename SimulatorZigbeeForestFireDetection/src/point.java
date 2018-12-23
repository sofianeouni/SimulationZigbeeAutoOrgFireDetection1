
public class point {
public double x,y;
point()
{x=0; y=0;}

point (point p){
	x=p.x;
	y=p.y;
}

point (double x,double y ){
	this.x=x;
	this.y=y;
}

void display(){
	//System.out.println (x+" "+ y);
	System.out.println (y);
}
}
