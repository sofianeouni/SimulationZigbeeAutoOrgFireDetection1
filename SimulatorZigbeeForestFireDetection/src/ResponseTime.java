import java.util.ArrayList;


public class ResponseTime implements ZigbeeParameters{

double R=0; //response time
double Rt=0; // total response time to the base station
	
	static int F_compute (ArrayList<Double> Fi, double t){
		int F=0; 
		for (Double D:Fi){
			 F+= ((int) (D.doubleValue()*t))+1; 
		  }
		return F;
	}

	static double Worst_Wait_time (double SD, double gi, ArrayList<Double> Fi, double Ttrans)// without cumulative work, this function to change class...
	{
	 int F=0;	// Number of forwarded traffics
	 double t=SD;
	 //if (t>SD) t=SD; // no cumulative work 
	 F=F_compute (Fi, t);
	  int G=(int) (gi*t);

		return ( (G+F)*Ttrans ); 
	}

	
	
	static double Rt_CSB (double SD, double gi, ArrayList<Double> Fi,double Ttrans,double TAckMax, int N ){
		double C= TAckMax*N+Ttrans;// worst case
		double W=Worst_Wait_time (0, gi,  Fi, Ttrans);
		 
		return W+C;
	}
	
	// ResponseTime.Rt_base_station (d, Ttrans,  TAckMax, N );
		static double Rt_base_station_CSB (Device d, double Ttrans,double TAckMax, int N ){
			double Tr=0;
			
			Tr=ResponseTime.Rt_CSB (d.FP.SD,  d.Ti.gi, d.Ti.Fi, Ttrans,  TAckMax, N );
			d.R1=Tr;
			
			for (Device dp:d.path){
				if (dp.type!=PAN_coordinator)
					{//dp.displayShort();
					Tr+=ResponseTime.Rt_CSB (dp.FP.SD,  dp.Ti.gi, dp.Ti.Fi, Ttrans,  TAckMax, N );
					
					}
				
			}
			//
			d.R_base_station=Tr;
			return Tr;
			
		}
		
	
	
	static double Rt (double SD, double BI, double gi, ArrayList<Double> Fi,double Ttrans,double TAckMax, int N ){
		double C= TAckMax*N+Ttrans+(BI-SD);// worst case
		double W=Worst_Wait_time (BI-SD, gi,  Fi, Ttrans);
		 
		return W+C;
	}
	
	
	// ResponseTime.Rt_base_station (d, Ttrans,  TAckMax, N );
	static double Rt_base_station (Device d, double Ttrans,double TAckMax, int N ){
		double Tr=0;
		
		Tr=ResponseTime.Rt (d.FP.SD, d.FP.BI, d.Ti.gi, d.Ti.Fi, Ttrans,  TAckMax, N );
		d.R1=Tr;
		
		for (Device dp:d.path){
			if (dp.type!=PAN_coordinator)
				{//dp.displayShort();
				Tr+=ResponseTime.Rt (dp.FP.SD, dp.FP.BI, dp.Ti.gi, dp.Ti.Fi, Ttrans,  TAckMax, N );
				
				}
			
		}
		//
		d.R_base_station=Tr;
		return Tr;
		
	}
	
	

}
