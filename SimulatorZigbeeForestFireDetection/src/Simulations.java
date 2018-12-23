
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Simulations implements ZigbeeParameters {
 double t; // simulation time 
 double LifeTime=0;
 
 TheTime Time= new TheTime();
 
double  PacketTime = DefaultPacketTime; // 10 bytes, so Tt is 3.2 msec on a	250 Kbps radio.
double AckTime = DefaultAckTime;
int Retransmission=MaxRetransmission;
double Deadline1=10.0*60.0; // 5 minutes
double Deadline2_fire= 1; // 1 second
//double Deadline1=Deadline2_fire;




/*

public static void main(String[] args) {
	Simulations S= new Simulations();
	

	System.out.println ("Packet nTime="+S.PacketTime+"Ack Time "+ S.AckTime+" nbre Ret"+ S.Retransmission);
	// the first parameter is the type of associations: association_depth_Energy_Trust_Node (our approach), association_depth_CSB, association_depth_ADCA  
	S.Sim4_Energy_conception_withFire_withFramePeriods(   null, 
			                            association_depth_Energy_Trust_Node // our approach 
			                              
			                                            , 25,25, // 25*25 sensor devices 
			                                            3600*24*30*12*1,  // 30 days 
			                                            3600,
                                                                    60,
			                                            S.PacketTime, S.AckTime, S.Retransmission
                                                            ,0,null,0, "Result.txt"); // every hour
*/
	
/*	S.Sim4_Energy_conception_withFire_withFramePeriods(   
               association_depth_ADCA   
                            , 25,25, // 25*25 sensor devices 
                            3600*24*30*12*1,  // 30 days 
                            3600,
                            S.PacketTime, S.AckTime, S.Retransmission ,0,null,0); // every hour
  */ 
	
	/*	S.Sim4_Energy_conception_withFire_withFramePeriods(   
                 association_depth_CSB
                 , 25,25, // 25*25 sensor devices 
                 3600*24*30*12*1,  // 30 days 
                 3600,
                 S.PacketTime, S.AckTime, S.Retransmission ,0,null,0); // every hour
*/ 
/*	
	
}


*/


double MinE_sup0(ZigbeeNetwork zn){
	 double min=Double.MAX_VALUE;
	 for(Device d:zn.Tree){
		 { if (min>d.E && d.E>0 && d.node_state!=fired)min=d.E;
		// System.out.println(min);
		 }
	 }
	 return min;
}

double MoDutyCycley(ZigbeeNetwork zn){
	 double s=0;
	 int n=0;
	 for(Device d:zn.Tree)
		  if ( d.E>0 && d.node_state!=fired){ s+=d.FP.DutyCycle; n++;}
		// System.out.println(min);
		 	 
	 return (s/n);
}


double MidE(ZigbeeNetwork zn){
	double S=0;
	int n=0;

	for(Device d:zn.Tree)
		 if (d.E>0 && d.node_state!=fired) { S+=d.E;n++;}
	
	return (S/n);
		
	
}


 
 
 void Sim1_Energy_conception_withoutFire (double SimTime, double PeriodReAssociation)
 {
	 
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
			
	ZigbeeNetwork zn= new ZigbeeNetwork (5, 5,dd);
	DrawNetwork dn= new DrawNetwork (zn);
	double DT=60; // step of simulation
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
while (t<SimTime){	
	Association Asso= new Association (zn);
	//Asso.association (association_depth);
    //Asso.association(association_depth_LQI);
	if (t-ta>PeriodReAssociation){	Asso.association(association_depth_Energy); ta=t;}
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	
	
	
	try {	Thread.sleep(2);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	  dn.p.repaint(t);
	// System.out.println(t+" ");
	  
	  t+=DT;
	}
	
}
 
 
 
 void Sim2_Energy_conception_withFire (double SimTime, double PeriodReAssociation)
 {
	 
	 
	 double Od=Math.PI/10;
	 double v=20;
	 double R=5;
	 point PC = new point(80,80);
	
	 
	FirePropagation Fp= new FirePropagation(PC, v, Od, R);
	
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
			
	ZigbeeNetwork zn= new ZigbeeNetwork (5, 5,dd);
	DrawNetwork dn= new DrawNetwork (zn,Fp);
	Fp.Change_Node_State( zn); // state...........
	double Delta_R=0.1;
	double DT=60; // step of simulation
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
while (t<SimTime){	
	Association Asso= new Association (zn);
	//Asso.association (association_depth);
    //Asso.association(association_depth_LQI);
	Fp.FireProgress (Fp.R+=Delta_R);
	  Fp.Change_Node_State( zn);
	  
	if (t-ta>PeriodReAssociation){	Asso.association(association_depth_Energy); ta=t;}
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	
	
	
	try {	Thread.sleep(20);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	  dn.p.repaint(t);
	// System.out.println(t+" ");
	  
	  t+=DT;
	}
	
}
 
 
 void Sim3_Energy_conception_withoutFire_withFramePeriods (int NbreX,int NbreY, double SimTime, double PeriodReAssociation,double PacketTime, double AckTime, int Retransmission)

 {
	 
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
			
	ZigbeeNetwork zn= new ZigbeeNetwork (NbreX, NbreY,dd);
	DrawNetwork dn= new DrawNetwork (zn);
	double DT=60; // step of simulation
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
	Association Asso= new Association (zn);
	//Asso.association(association_depth_Energy);
while (t<SimTime){	
	
	
	//Asso.association (association_depth);
    //Asso.association(association_depth_LQI);
	if (t-ta>PeriodReAssociation){	Asso.association(association_depth_Energy);
	//adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline )
	                                zn.adjust_frame_timing_our_approach ( PacketTime, AckTime, Retransmission, Deadline1 );
	                                ta=t;}
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	
	
	
	try {	Thread.sleep(20);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	  dn.p.repaint(t);
	// System.out.println(t+" ");
	  
	  t+=DT;
	}
	
}
 

 void Sim4_Energy_conception_withFire_withFramePeriods (int NbreX, int NbreY, double SimTime, double PeriodReAssociation, double PacketTime, double AckTime, int Retransmission)
 {
	 
	 double Od=Math.PI/10;
	 double v=0;//
	 double R=1;//
	 point PC = new point(99990,99990);///
	 double MinEnergy=-1;
	
	 
	FirePropagation Fp= new FirePropagation(PC, v, Od, R);
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
			
	ZigbeeNetwork zn= new ZigbeeNetwork (NbreX, NbreY,dd); // impaire!
	DrawNetwork dn= new DrawNetwork (zn,Fp);
	//Fp.Change_Node_State( zn); // state...........
	double Delta_R=0.1;
	double DT=60; // step of simulation
	 boolean newFired=false;
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
	Association Asso= new Association (zn);
	Asso.association(association_depth_Energy_Trust_Node);	
while (t<SimTime){	
	
	Fp.FireProgress (Fp.R+=Delta_R);
	  //Fp.Change_Node_State( zn);
	newFired=Fp.Change_Node_State_our_approach(zn);////////////
	  //deadline2_fire
	//adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline1, double deadline2_fire )
	 //zn.adjust_frame_timing_our_approach_fire(0.005,  0.01, 0, 100, 1 );
	
	 zn.adjust_frame_timing_our_approach_fire(PacketTime, AckTime,  Retransmission, Deadline1, Deadline2_fire);
	 
	 	
	 //Compute_dispaly_response_time (double Ttrans,  double TAckMax, int N)
	  zn.Compute_dispaly_response_time (association_depth_Energy_Trust_Node, PacketTime, AckTime, Retransmission);
	// zn.Compute_dispaly_response_time (0.005, 0.01, 0);
	  
	//Asso.association (association_depth);
    //Asso.association(association_depth_LQI);
	 // Asso.association(association_depth_Energy_Trust_Node);
	if (t-ta>PeriodReAssociation || newFired){	//Asso.association(association_depth_Energy);
									Asso.association(association_depth_Energy_Trust_Node);	
									//System.out.println("------------------");
	                                ta=t;}
	zn.Nbre_discarted_Packets(DT); 
	
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	//MinEnergy=MinE(zn);
	System.out.println("Min Energy="+MinEnergy);
	
	
	try {	Thread.sleep(2);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	  dn.p.repaint(t);
	// System.out.println(t+" ");
	  
	  t+=DT;
	}


	
}
 
 
 void Sim4_Energy_conception_withFire_withFramePeriods (JFrame F1, int type, int NbreX, int NbreY, double SimTime, double PeriodReAssociation, double DT , double PacketTime, double AckTime, int Retransmission
               , double v,point PC, double MaxR, String FileResult )
 {
     
     if (F1!=null)        F1.dispose();
	 // type : association_depth_Energy_Trust_Node, association_depth_CSB, association_depth_ADCA
	 TheTime T=null; 
	 double Od=Math.PI/10;
         if (v==0)v=20; // default value //double v=20;
	 double R=20;//20 ............................
         if (PC==null) PC = new point(0,500);///
         if (MaxR==0)MaxR=400;
	 double MinEnergy=-1;
	 int NbStop=0;
	 int NbMaxStop= 25; 
	 //(int) ((NbreX*NbreY)/10.0); // 10%
	
	 System.out.println("ret="+Retransmission);
	FirePropagation Fp= new FirePropagation(PC, v, Od, R);
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
	 	//double dd= TransmissionRange-80; 
	
	if (type==association_depth_ADCA) Retransmission/=2;
	if (Retransmission<0)Retransmission=0;
			
	ZigbeeNetwork zn= new ZigbeeNetwork (NbreX, NbreY,dd); // impaire!
	DrawNetwork dn= new DrawNetwork (zn,Fp);
	//Fp.Change_Node_State( zn); // state...........
	double Delta_R=1;
        if (DT==0) DT=60; // step of simulation 
	//double DT=3600*24; // step of simulation without fire
	 boolean newFired=false;
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
	Association Asso= new Association (zn);
	//if (type==association_depth_Energy_Trust_Node) Asso.association(association_depth_Energy_Trust_Node);
	//if (type==association_depth_ADCA) Asso.association(association_depth_ADCA);
	Asso.association(type);
	
while (t<SimTime /*&& NbStop<NbMaxStop*/){	
	
	if (Fp.R<MaxR)	Fp.FireProgress (Fp.R+=Delta_R);/////////////
	  
	if (type==association_depth_Energy_Trust_Node)	newFired=Fp.Change_Node_State_our_approach(zn);////////////
	else newFired=Fp.Change_Node_State(zn);
	
	if (type==association_depth_CSB)  Fp.hole_impact(zn); // total reorganization!
	
	  //deadline2_fire
	//adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline1, double deadline2_fire )
	 //zn.adjust_frame_timing_our_approach_fire(0.005,  0.01, 0, 100, 1 );
	
	if (type==association_depth_Energy_Trust_Node)
	        zn.adjust_frame_timing_our_approach_fire(PacketTime, AckTime,  Retransmission, Deadline1, Deadline2_fire);
	 if (type==association_depth_ADCA)
		 zn.adjust_frame_timing_ADCA_approach (PacketTime, AckTime,  Retransmission ); 
	 if (type==association_depth_CSB)
	      zn.adjust_frame_timing_CSB_approach (PacketTime, AckTime,  Retransmission);
	 	
	 //Compute_dispaly_response_time (double Ttrans,  double TAckMax, int N)
	  zn.Compute_dispaly_response_time (type, PacketTime, AckTime, Retransmission);
	
	  // zn.Compute_dispaly_response_time (0.005, 0.01, 0);
	  
	
	
	  if (type==association_depth_Energy_Trust_Node)
	 
	            //if (t-ta>PeriodReAssociation || newFired){	//Asso.association(association_depth_Energy);
		         if (t-ta>PeriodReAssociation ){	
									Asso.association(association_depth_Energy_Trust_Node);	
									//System.out.println("------------------");
	                                ta=t;}
	
	if (newFired)	{Asso.association (type);NbStop++;          
	                    }
	//zn.Nbre_discarted_Packets(DT); 
	
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	//MinEnergy=MinE_sup0(zn);
	//MinEnergy=MidE(zn);
	//System.out.println(EnergyInit-MinEnergy);
	
	
	
	//try {	Thread.sleep(200);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	  dn.p.repaint(t);
	// System.out.println(t+" ");
	//T= new TheTime(t);
	//T.display();
	  t+=DT;
	}
     
     try {FileWriter fileWriter = new FileWriter(FileResult);
     PrintWriter P= new PrintWriter (fileWriter);
         





if (NbStop>=NbMaxStop) System.out.println("End At XX% stoped nodes ");


MinEnergy=MidE(zn);
System.out.println("Avrage spend Energy "+(EnergyInit-MinEnergy));
P.println("Avrage spend Energy "+(EnergyInit-MinEnergy));

MinEnergy=MinE_sup0(zn);

System.out.println("Max. spend Energy "+(EnergyInit-MinEnergy));
P.println("Max. spend Energy "+(EnergyInit-MinEnergy));

double du=MoDutyCycley(zn);
System.out.println("dutyCycle "+du );
P.println("dutyCycle "+du );

fileWriter.close();
} catch (IOException ex) {
         Logger.getLogger(Simulations.class.getName()).log(Level.SEVERE, null, ex);
     }



/*
System.out.println("End....  ");
System.out.println("Response Time  ");
display_responseTime(zn);
System.out.println("DutyCyle");
display_DutyCycle_Depth( zn);
System.out.println(".............................  ");
System.out.println("Min Energy="+MinEnergy);
display_Total_error_discarted_packets(t,zn);
	*/

//System.exit(0);
}
 
 
 void display_Total_error_discarted_packets(double t, ZigbeeNetwork zn){
	 
	 double Nbre_errors=0;
		double Nbre_Pdiscarted=0;
		double Nbre_packets=0;
		
		for (Device d:zn.Tree){
			Nbre_errors+=d.TE.Nbre_errors;
			Nbre_Pdiscarted+=d.TE.Nbre_Pdiscarted;
			d.TE.Nbre_packets=Transmission_Errors.Nbre_packets( t, d);
			Nbre_packets+=d.TE.Nbre_packets;
		}
		
		System.out.println("N errors:"+Nbre_errors+" N discarted="+Nbre_Pdiscarted+" N_packets="+Nbre_packets);
 }
 
 //
 void display_responseTime(ZigbeeNetwork zn){
	 
	 int dep=1;
	 boolean moreDepth=true;
	 while (moreDepth){
		 double s=0;int n=0;
		 moreDepth=false;
		 for (Device d:zn.Tree)
			 if (d.depth==dep) { s+=d.R_base_station;n++;moreDepth=true;}
		 s=s/n;	 
		 //System.out.println("depth="+dep+" R="+s);
		 System.out.println(s);
		 dep++; 
	 }
	 
 }
 
void display_DutyCycle_Depth(ZigbeeNetwork zn){
	 
	 int dep=1;
	 boolean moreDepth=true;
	 while (moreDepth){
		 double s=0;int n=0;
		 moreDepth=false;
		 for (Device d:zn.Tree)
			 if (d.depth==dep) { s+=d.FP.DutyCycle;n++;moreDepth=true;}
		 s=s/n;	 
		 //System.out.println("depth="+dep+" R="+s);
		 System.out.println(s);
		 dep++; 
	 }
	 
 }
 
 
 
 
 void Sim4_ADCA_Energy_conception_withFire_withFramePeriods (double SimTime, double PeriodReAssociation)
 {
	 
	 double Od=Math.PI/10;
	 double v=20;
	 double R=5;
	 point PC = new point(90,90);
	
	 
	FirePropagation Fp= new FirePropagation(PC, v, Od, R);
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
			
	ZigbeeNetwork zn= new ZigbeeNetwork (5, 5,dd); // impaire!
	DrawNetwork dn= new DrawNetwork (zn,Fp);
	Fp.Change_Node_State( zn); // state...........
	double Delta_R=0.1;
	double DT=60; // step of simulation
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
	Association Asso= new Association (zn);
	Asso.association (association_depth);
while (t<SimTime){	
	
	Fp.FireProgress (Fp.R+=Delta_R);
	  //Fp.Change_Node_State( zn);
	boolean newFired=Fp.Change_Node_State(zn);
	  
	//adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline1, double deadline2_fire )
	 zn.adjust_frame_timing_ADCA_approach (0.005,   0.01, 0 ); 
	 //zn.adjust_frame_timing_our_approach_fire(0.005,  0.01, 0, 100, 1 );
	  //Compute_dispaly_response_time (double Ttrans,  double TAckMax, int N)
	  zn.Compute_dispaly_response_time (association_depth_Energy_Trust_Node,0.005,   0.01, 0); 
	  
	if (newFired) Asso.association (association_depth);
    
	zn.Nbre_discarted_Packets(DT); 
	
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	
	
	//try {	Thread.sleep(20);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	  dn.p.repaint(t);
	// System.out.println(t+" ");
	  
	  t+=DT;
	}
	
}
 
 
 void Sim4_CSB_Energy_conception_withFire_withFramePeriods (double SimTime, double PeriodReAssociation)
 {
	 
	 double Od=Math.PI/10;
	 double v=20;
	 double R=5;
	 point PC = new point(90,90);
	 TheTime T=null;
	
	 
	FirePropagation Fp= new FirePropagation(PC, v, Od, R);
	 	double ta=-PeriodReAssociation;
	double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
			
	ZigbeeNetwork zn= new ZigbeeNetwork (5, 5,dd); // impaire!
	DrawNetwork dn= new DrawNetwork (zn,Fp);
	Fp.Change_Node_State( zn); // state...........
	double Delta_R=0.1;
	double DT=3600; // step of simulation 60s
	System.out.println ("PeriodReAssociation="+ PeriodReAssociation);
	t=0;
	Association Asso= new Association (zn);
	Asso.association (association_depth_CSB);
while (t<SimTime){	
	
	Fp.FireProgress (Fp.R+=Delta_R);
	  //Fp.Change_Node_State( zn);
	boolean newFired=Fp.Change_Node_State(zn);
	  
	//adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline1, double deadline2_fire )
	 zn.adjust_frame_timing_CSB_approach (0.005,   0.01, 0 ); 
	 //zn.adjust_frame_timing_our_approach_fire(0.005,  0.01, 0, 100, 1 );
	  //Compute_dispaly_response_time (double Ttrans,  double TAckMax, int N)
	  zn.Compute_dispaly_response_time (association_depth_Energy_Trust_Node, 0.005,   0.01, 0); 
	  
	if (newFired) Asso.association (association_depth_CSB);
    
	zn.Nbre_discarted_Packets(DT); 
	
	Energy.compute_E_all_nodes_dt(zn,  DT );	
	
	
	//try {	Thread.sleep(2);	} catch (InterruptedException e) {e.printStackTrace();	}
	  	
	 //dn.p.repaint(t);
	
	 //System.out.println(t+" ");
	T= new TheTime(t);
	T.display();
	  
	  t+=DT;
	}
T= new TheTime(t);
T.display();



}
 
 
 
 
	
	

}
