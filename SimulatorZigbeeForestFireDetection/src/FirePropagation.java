
public class FirePropagation implements ZigbeeParameters {

	
	point PC; // start position
	double v=20 ;//wind_speed
	double Od; //direction ongle 
	double R;
	ellipse ell=new ellipse();
	
	
	FirePropagation (){}
	
	FirePropagation(point PC, double v, double Od, double a, double b){
		this.PC=PC;
		this.v=v;
		this.Od=Od;
		ell.PC=PC;
		ell.Od=Od;
		ell= new ellipse(PC, Od, a, b);
	}
	
	FirePropagation(point PC, double v, double Od, double R){
		this.PC=PC;
		this.v=v;
		this.Od=Od;
		ell.PC=PC;
		ell.Od=Od;
		this.R=R;
		ell= ellipse_axes (  PC,  Od,  R,  v  );
	}
	
	void FireProgress (double R) {
		ell= ellipse_axes (  PC,  Od,  R,  v  );
	}

	
	boolean ifInEllipse(point p, point PC, double Od, double a, double b)
	{ point p1= new point();
	 point p2=new point();
		
	//	System.out.println ("PC= "+ PC.x+" "+PC.y);
		p1= ell.Rotation_Direction ( -Od, PC,  p);
		//System.out.println ("o rotation="+ (-Od)+"  P= "+ p1.x+" "+p1.y);
		double dir= ell.Point_Dir( p1,  PC);
		p2= ell.ellipsePoint (PC , a,  b, dir);
		//System.out.println ("P2= "+ p2.x+" "+p2.y);
		
	//	System.out.println ("P=( "+ p.x+" "+p.y);
		//System.out.println ("d(p2,pc)="+ distance2(p2,PC)+" d(p,pc)="+distance2(p,PC));
		
		if (ell.distance2(p2,PC)>=ell.distance2(p,PC)) return (true);
		else return false;
	}
	
	boolean ifInEllipseDetection(point p, point PC, double Od, double a, double b, double Ddetect)
	{ point p1= new point();
	 point p2=new point();
		
	//	System.out.println ("PC= "+ PC.x+" "+PC.y);
		p1= ell.Rotation_Direction ( -Od, PC,  p);
		//ystem.out.println ("o rotation="+ (-Od)+"  P= "+ p1.x+" "+p1.y);
		double dir= ell.Point_Dir( p1,  PC);
		p2= ell.ellipsePoint (PC , a,  b, dir);
		//System.out.println ("P2= "+ p2.x+" "+p2.y);
		
	//	System.out.println ("P=( "+ p.x+" "+p.y);
		//System.out.println ("d(p2,pc)="+ distance2(p2,PC)+" d(p,pc)="+distance2(p,PC));
		
		if (ell.distance2(p2,PC)>=(ell.distance2(p,PC)-Ddetect)) return (true);
		else return false;
	}
	
	
	
	boolean IF_Device_IN_ellipse(Device D){
		
		return ifInEllipse(D.pd, ell.PC, ell.Od, ell.a, ell.b);
			}
	
boolean IF_Device_Detect_ellipse(Device D){
		
		return ifInEllipseDetection(D.pd, ell.PC, ell.Od, ell.a, ell.b,D.Ddetect);
			}


boolean Change_Node_State(ZigbeeNetwork zn){
	boolean newFiredNode=false;
	for (Device d:zn.Tree){
		//d.display();
		if (d.type!=PAN_coordinator) 
			 if (IF_Device_IN_ellipse(d)  ){
				                            if (d.node_state!=fired) {newFiredNode=true;Fire_lost_parent_impact ( d);}
			                                 d.node_state=fired;
		                                            }
			 else if (d.E<=0) {if (d.node_state!=energy_Low) { newFiredNode=true; // as to be fired!!!!!!
			  													Fire_lost_parent_impact ( d); }
			                   d.node_state=energy_Low;}
		                        
	}
	return(newFiredNode);
}

void hole_impact(ZigbeeNetwork zn){
	for (Device d:zn.Tree)	 Fire_lost_parent_impact ( d);
}

void Fire_lost_parent_impact (Device d){
	 d.TE.Nbre_discarted_Packets__FiredNode_align(d);
}

//	int low_activity=0; int fired=1; int fire_detection=2;  int neighbor_suspected=3; int in_path_active=4;  
boolean Change_Node_State_our_approach(ZigbeeNetwork zn){
	boolean newFiredNode=false;
	
	
	for (Device d:zn.Tree){
		//d.display();
		if (d.type!=PAN_coordinator){
			 if (IF_Device_IN_ellipse(d)  ){ if (d.node_state!=fired) newFiredNode=true;
				                             d.node_state=fired;
				                            for (Device dp:d.path){ dp.node_state=in_path_active;}////
				                            	
				                            }
			                                }
			 
				    else  if (d.E<=0) {if (d.node_state!=energy_Low) { newFiredNode=true; // as to be fired!!!!!!
																		 }
	                                       d.node_state=energy_Low;}
			 
		}
	
	for (Device d:zn.Tree)
	 if(d.node_state!=fired && d.node_state!=energy_Low)
		 for (Device ch:zn.Tree)
		    if (ch.node_state==fired && Association.inTranmissionRange( d, ch,TransmissionRange))
		                                d.node_state=neighbor_suspected;
		                                 
		                    		     
	for (Device dp:zn.Tree){ if (dp.node_state==in_path_active) dp.node_state=low_activity;}///
	for (Device d:zn.Tree)	 
			 if (d.node_state==neighbor_suspected) 
				    for (Device dp:d.path)
				          if (dp.node_state!=fired && dp.node_state!=neighbor_suspected && dp.node_state!=energy_Low) 
				        	             dp.node_state=in_path_active;
		 
	
		                        
	
	return(newFiredNode);
}

ellipse ellipse_axes ( point PC, double Od, double R, double v  ){
	
	
	double L= 1+0.0012*Math.pow(v,2.154);
	if (L<=0)L=1;////////////////
	double a,b;
	b= R/Math.sqrt(L*L-1+L);
	a=b*L;
	ellipse el= new ellipse (PC,Od, a,  b);
	
	 return el;
	
}
	
	/*
	public static void main(String[] args) {
		
		point PC = new point(100,100);
		 double Od=Math.PI/6;
		 double v=20;
		 
		FirePropagation Fp= new FirePropagation(PC,Od,v,50,70);
		//FirePropagation Fp= new FirePropagation();
		Fp.ell.aff_ellipse ( );
		

	}*/

}
