
public class Energy implements EnergyParameters,ZigbeeParameters {

	double TPkt,TAck;
	Energy (double TPkt, double TAck ){
	this.TPkt=TPkt; this.TAck=TAck;	
	}
	Energy ( ){
		this.TPkt=DefaultPacketTime; this.TAck=DefaultAckTime ;	
		}
	
	double E(double t, Device d) {
		
		return (Erx(t,d)+Etx(t,d)+Eidle(t,d)+Esleep(t,d));
	}
	double Erx(double t, Device d) {
		
		return t*e_rx*(TPkt*d.Ti.fi+TAck*(d.Ti.gi+d.Ti.fi)); 
	}
	
	double Eidle(double t, Device d) {
	return t*d.FP.DutyCycle*e_idle;	
	}
	
	double Esleep (double t, Device d)
	{
		return t*e_sleep*(1-d.FP.DutyCycle); 
	}
	
	
	double Etx(double t, Device d) {
		return t*e_tx*(TPkt*(d.Ti.gi+d.Ti.fi)+TAck*d.Ti.fi); 
	}
	
	static double Etx_lost_packets (double nbre){
		return e_tx*DefaultPacketTime*nbre; // we need to change if is not TPkt==DefaultPacketTime
	}
	
	
	static void compute_E_all_nodes_dt(ZigbeeNetwork zn, double dt ){
		for (Device d:zn.Tree){
			Energy EE= new Energy();
			
			if (d.type!= PAN_coordinator) 
			{ d.E-=EE.E(dt,d);	
			//System.out.println ("Energy concept="+EE.E(dt,d));
			}
			
			
		}
	}
	

}
