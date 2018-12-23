import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;


public class TheTime {

	double t;
	
	 int Days;
		 int Hours;
		 int Minutes;
		 int Seconds;
		
		 
		 TheTime(){
			 
		 }
		 
		 TheTime (double t){
			 Time_conversion(t);
		 }
		 
		 void display(){
				String S= new String(Days+"Days |"+ Hours+":"+Minutes+":"+Seconds);
				System.out.println(t+" / "+S);
		 }
		 
		 
		 void display(Graphics g){
			 Graphics2D ga;
			    ga = (Graphics2D)g;
				//ga.setColor(Color.white);
                                ga.fillRect(10,0,60,15);
				ga.setColor(Color.black);
				String S= new String(Days+"Days |"+ Hours+":"+Minutes+":"+Seconds);
				//System.out.println(t+" / "+S);
				ga.drawString(S, 10, 10); //////////////////
		 }
		 
	void Time_conversion (double t){
		 
		
		

		Days = (int) (t / 86400);
		 Hours = (int) (t % 86400 ) / 3600 ;
		Minutes =(int) ((t % 86400 ) % 3600 ) / 60; 
		 Seconds = (int) ((t % 86400 ) % 3600 ) % 60  ;
	 }
	

}
