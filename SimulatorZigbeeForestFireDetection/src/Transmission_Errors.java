import java.util.ArrayList;


public class Transmission_Errors implements ZigbeeParameters{

	double Nbre_errors=0;
	double Nbre_Pdiscarted=0;
	double Nbre_packets=0;
	
	
	Transmission_Errors (){
		 Nbre_errors=0;
		 Nbre_Pdiscarted=0;
		 Nbre_packets=0;
	}
	
	
	

	
	double NbrePacket (double t, Device d){
		double  F=  (d.Ti.fi*t);//F_compute (d.Ti.Fi, t);
		  double G=(d.Ti.gi*t);
		  return F+G;
	}
	
	double  F_compute (ArrayList<Double> Fi, double t){
		double F=0; 
		for (Double D:Fi){
			 F+= ((int) (D.doubleValue()*t))+1; 
		  }
		return F;
	}

	
	void P_discarted_alignement(double t, Device d){
		
	}
	
	void Nbre_discarted_Packets__FiredNode_align(Device d){ // for ADCA and CSB and Zigbee
		// also reduction of energy of lost packets
		
		for (Device dc:d.children){
			double nbre_packets_align_firedNode = nbre_Lost_packets_align (d.FP.BI*4,  dc.Ti.gi, dc.Ti.fi);
			//System.out.println("oooooooooooo"+nbre_packets_align_firedNode);
			double El=Energy.Etx_lost_packets (nbre_packets_align_firedNode);
			dc.E-=El;
			dc.TE.Nbre_Pdiscarted+=nbre_packets_align_firedNode;	
		}
		
		
		
	}
	
	void Nbre_discarted_Packets__reassociation(Device d, double Talign){
		if (Talign==0) Talign=macResponseWaitTime;
		double nbre_packets_align = nbre_Lost_packets_align (Talign,  d.Ti.gi, d.Ti.fi);
		
		// also reduction of energy of lost packets
				double El=Energy.Etx_lost_packets (nbre_packets_align);
				d.E-=El;
		
		Nbre_Pdiscarted+=nbre_packets_align;	
	}
	
void Nbre_discarted_Packets(double t, Device d) //, int MaxRetransmissions)
{
	
	double BB= B(0,0,d.children.size()+2, W_default);// neighbors 
	///double F=F_compute (d.Ti.Fi, t);//////
	///d.Ti.fi=F;///////////////////////
	
	double nb1= Nbre_packets_discarted(t,MaxRetransmission, BB,  d.Ti.gi, d.Ti.fi);
	//double Nbre_lost_packets_due_align(double t, int aMaxLostBeacons, double B, double BI, double Talign, double gi, double fi )
	
	double nb2=Nbre_lost_packets_due_align( t, aMaxLostBeacons, BB,d.FP.BI, macResponseWaitTime, d.Ti.gi, d.Ti.fi);
    
	//nb2=0;
	//System.out.println ("Nbre discart"+nb1);
	//System.out.println ("Nbre discart"+Nbre_Pdiscarted);
	Nbre_Pdiscarted+=nb1+nb2;
	
	nb1= Nbre_packets_discarted(t, 1, BB,  d.Ti.gi, d.Ti.fi);
	nb2=Nbre_lost_packets_due_align( t, 1, BB, d.FP.BI, macResponseWaitTime, d.Ti.gi, d.Ti.fi);
	
	
	Nbre_errors+=nb1+nb2;
	Nbre_packets+=NbrePacket (t, d);
	//System.out.println("Nbre packet="+Nbre_packets+" Nbre discarted="+Nbre_Pdiscarted);
	//System.out.println ("Nbr %="+  (Nbre_Pdiscarted/Nbre_packets)*100);
	//System.out.println ("Nbr errors %="+  (Nbre_errors/Nbre_packets)*100);
	
}

	double Pcoll(double n, double w){
		return 1-Math.pow((1-1/w), n-1);
	}
	
	double P_lost_Beacons(int aMaxLostBeacons, double B){
		double s=0;
		for (int i=0; i<aMaxLostBeacons; i++){
			s+=(1-B)*Math.pow(B,i);	}
		return 1-s;
	}
	
	double P_packet_discarted(int MaxRetransmissions, double B){
		double s=0;
		for (int i=0; i<MaxRetransmissions; i++){
			s+=(1-B)*Math.pow(B,i);	}
		
		
	//	System.out.println("P discart="+(1-s));
		return 1-s;
	}
	
	double Nbre_packets_discarted(double t, int MaxRetransmissions, double B,  double gi, double fi)
	{	
		//System.out.println (" B"+ B);
		//System.out.println (" P lost Beacon"+P_lost_Beacons(aMaxLostBeacons, B));	
		return ( P_packet_discarted(MaxRetransmissions, B)*(gi+fi)*t   ); }
	
	static double Nbre_packets(double t, Device d)
	{	
		//System.out.println (" B"+ B);
		//System.out.println (" P lost Beacon"+P_lost_Beacons(aMaxLostBeacons, B));	
		return ((d.Ti.gi+d.Ti.fi)*t   ); }
	
	
	
	double nbre_Lost_packets_align (double Talign, double gi, double fi){
		return (gi+fi)*Talign;
	}
	
	double Nbre_lost_packets_due_align(double t, int aMaxLostBeacons, double B, double BI, double Talign, double gi, double fi )
	{
		if (Talign==0) Talign=macResponseWaitTime;
		double nbre_beacon_lost = P_lost_Beacons(aMaxLostBeacons, B)*t/BI;
		double nbre_packets_align = nbre_Lost_packets_align (Talign,  gi, fi);
		
		return (   nbre_beacon_lost* nbre_packets_align  );
	}
	
	
	double Psuccess(double nbre_attempts, double B){
		double s=0;
		for (int i=0;i<nbre_attempts;i++){
			s+= (1-B)*Math.pow(B,i);}
		return s; 
	}
	
	double Perror(double Ber, double P_size){
		if (P_size==0) P_size=DefaultPacketSize;
		if (Ber==0) Ber=0.0001; // by default
		return Ber*P_size;
	}
	
	double B(double Ber, double P_size,double n, double w ){
		if (P_size==0) P_size=DefaultPacketSize;
		//System.out.println (" P="+ Perror(Ber, P_size));
		//System.out.println (" Pcoll="+Pcoll(n, w));
		return Perror(Ber, P_size)+Pcoll(n, w);
	}
	
	
	
	
	

}
