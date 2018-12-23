import java.util.*;


public class Device implements ZigbeeParameters, Cloneable{
 double idx=0,idy=0; // the id is on x,y as in mat
 int type =parent_coordinator; // 0: PAN coordinator, 1: coordinator, 2: RFD device
 double E=100; //Energy of the node
 int depth=-1; // depth not yet assigned it will be if the device connected to the network
 double Ddetect=DetectionDistance; // detection distance fire by default 2m 

 point pd=new point(); //  double x=0,y=0; // position of the sensor device (x,y) 
 int node_state=0; // 0 low_activity, 1 fired , 2 fire detection, 3 neighbor suspected, 4 in path active node(high activity)   
 Device coordinator = null; // parent 
 
 ArrayList <Device> children = new  ArrayList <Device>();
 ArrayList <Device> path = new  ArrayList <Device>(); // path to the root PAN coordinator
 Traffic Ti= new Traffic();// traffic for this devices (gi: generated, fi: forwarded)
 
 // time periods
 FrameTimePeriods FP=new FrameTimePeriods();
 double R1; // response time for 1 hope 
 double R_base_station; // response time to the base station, the pan coordinator
 
 Transmission_Errors TE=new Transmission_Errors();
 
 Device (){
	// TE=new Transmission_Errors(this);
 }
 Device (int idx,int idy, int type, double E, double x, double y, int depth){
	  this(idx,idy,type,E,x,y);
	  this.depth=depth;
	  Device coordinator = null;
	  TE=new Transmission_Errors();
 }
 
 
 // most used
 Device (int idx,int idy, int type, double E, double x, double y, double gi){
		
     
	this.E=E;
	 this.idx=idx;
	 this.idy=idy;
	 this.pd.x=x; this.pd.y=y;
     node_state=0;
	 this.type=type;
	 depth=-1; ///////////////////// 
	 coordinator = null;
	 if (type!=PAN_coordinator) Ti=new Traffic(gi); 
	 else { Ti=new Traffic(0);this.E=-1;}
	 TE=new Transmission_Errors();
	 
 }
 
 Device (int idx,int idy, int type, double E, double x, double y){
	
		           
	 this.E=E;
	 this.idx=idx;
	 this.idy=idy;
	 this.pd.x=x; this.pd.y=y;
     node_state=0;
	 this.type=type;
	 depth=-1; ///////////////////// 
	 coordinator = null;
	 if (type!=PAN_coordinator) Ti=new Traffic(gi_default);
	 else {Ti=new Traffic(0);this.E=-1;}
	 TE=new Transmission_Errors();
 }
 
 
 @Override public boolean equals(Object aThat) {
	    //check for self-comparison
	    if ( this == aThat ) return true;
	    if (this== null && aThat==null) return true;
	    if (aThat ==null && this!=null || aThat!=null && this==null ) return false;
   
	    if ( !(aThat instanceof Device) ) return false;
	    

	    //cast to native object is now safe
	    Device that = (Device)aThat;
 
	    return this.idx==that.idx && this.idy==that.idy;
 }
 
 

 
 void displayShort(){
	 if (this==null) return;
	 System.out.println ("id ("+idx+","+idy+")"+" Depth="+depth);
 }
 
 
 
 void determine_path(){
	 this.path.clear();
	 Device p=this.coordinator;
	 while (p!=null){
		 this.path.add(p);
		 p=p.coordinator;
	 }
 }
 
 
 void display()
 {
	 if (this==null) return;
	 System.out.println ("id ("+idx+","+idy+") type="+type+" E="+ E +" Depth="+depth+" pos> ("+pd.x+","+pd.y+")"
	                +" gi="+Ti.gi+" fi="+Ti.fi);
	 
 }
 
 void Compute_dispaly_response_time (int type , double Ttrans,  double TAckMax, int N){
	 double rt=0;
	 switch (type){
	  
	  case association_depth_Energy_Trust_Node:  rt=ResponseTime.Rt_base_station (this, Ttrans,  TAckMax, N );break;
	  case association_depth_CSB:  rt=ResponseTime.Rt_base_station_CSB (this,  Ttrans, TAckMax,  N ); break;
	  case association_depth_ADCA:  rt=ResponseTime.Rt_base_station (this, Ttrans,  TAckMax, N ); break;
	  default :System.out.println("Errrrrrrrror!!!!!!!");break;	  
	  }
	 
	
	 R_base_station=rt;
	
	// displayShort();
	//System.out.println("Total Response Time="+rt);
	 }
 
 void displayPath()
 {
	 System.out.println("---------path -------");
	 for (Device d:path){
		 d.display();
	 }
	 System.out.println("----------------");
 }
 
 void displayMore()
 {
	 if (this==null) return;
	 System.out.println();
	 System.out.println ("________________________________");
	 display();
	 System.out.println("--parent--");
	 if (this.coordinator!=null) this.coordinator.display();
	 System.out.println("------children-----------");
	 for (Device ch:this.children){ ch.display(); }
	 displayPath();
	 
	 
 }
 
 void displayTiming()
 {
 FP.display();
 }
 
	

}
