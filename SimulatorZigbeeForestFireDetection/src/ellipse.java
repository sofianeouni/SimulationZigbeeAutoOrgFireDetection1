
public class ellipse {
    point PC; // center of the ellipse 
	double Od; //direction ongle 
	double a=0; double b=0;// ellipse axes
	
	ellipse() {	}
	
	ellipse(point PC, double Od, double a, double b) {
		this.PC=PC;
		this.Od=Od;
		this.a=a;
		this.b=b;
		
	}
	point ellipsePoint (point PC , double a, double b, double O)
	{
		point p= new point();;
		p.x= a*Math.cos(O)+PC.x;
		p.y=b*Math.sin(O)+PC.y;	
		return p; 	
	}
	
	point Rotation_Direction ( double Od, point PC,  point p){
		point p1=new point(); 
		point p2=new point(); 
		point p3=new point(); 
		// translation
		//System.out.println(PC.x+" ");
		p1.x=p.x-PC.x; 
		p1.y=p.y-PC.y;
		// rotation
		p2.x= p1.x*Math.cos(Od) + p1.x*Math.sin(Od);
		p2.y= p1.x* Math.sin(Od) +p1.y*Math.cos(Od);
		// opposite translation
		p3.x=p2.x+PC.x; 
		p3.y=p2.y+PC.y;
		
		
		
		return (p3);
	}
	
	point ellipseDir (point PC , double Od, double a, double b, double O)
	{
		point p1 = new point();
		point p2 = new point();
		
		p1=ellipsePoint (PC , a, b, O);
		p2=Rotation_Direction ( Od, PC,  p1);
		return p2;
		}
	
	void aff_ellipse ( )
	{
	 point PC = new point(100,100);
	 double Od=Math.PI/6;
	 double a=50; double b=70;
	 point d=ellipse_axes (  70, 20  );
	 a=d.x; b=d.y;
	 System.out.println ("a="+a+" b="+b);
	 for (double i=0; i<Math.PI*2; i+=0.2) {
		 point p1=	 ellipseDir( PC , Od, a, b,  i);
		 p1.display();
	     }
	
	
	
	 
	 }
	
	// to be deleted
	point ellipse_axes ( double R, double v  ){
		double L= 1+0.0012*Math.pow(v,2.154);
		double a,b;
		b= R/Math.sqrt(L*L-1+L);
		a=b*L;
		point d= new point(a,b);
		 return d;
		
	}
		
	double Point_Dir(point p, point PC){
		return Math.atan( (PC.y-p.y)/(PC.x-p.x));
	}
	
	static double distance2 (point p1, point p2){
			
		double r=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
		
		return r;
	}
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/

}
