import java.util.*;


public class ZigbeeNetwork implements ZigbeeParameters{

	Device PanC= null;
	ArrayList <Device> Tree=  new ArrayList <Device>();  // tree topology of the network, the first is the PAN coordinator
	double Xmin,Ymin, Xmax, Ymax; // zone of the zigbee network
	double Xsize, Ysize;
	int nbre=0;
	int nx=0,ny=0;
	double TR=TransmissionRange; //transmission range
	int Depth=0;
	
	ZigbeeNetwork (int nx, int ny, double dd){ //distance between node grid
		 this.nbre=nx*ny;
		    this.nx=nx; this.ny=ny;
			PanC = 	new Device(0,0,PAN_coordinator,-1,0,0,0); //Device (int idx,int idy, int type, double E, double x, double y, int depth)
			Tree.add(PanC);
			TR= TransmissionRange;
			Grid_devices_Deploy (nx, ny,dd);
	}
	
		
	
	ZigbeeNetwork (int nx, int ny){
	    this.nbre=nx*ny;
	    this.nx=nx; this.ny=ny;
		PanC = 	new Device(0,0,PAN_coordinator,-1,0,0,0); //Device (int idx,int idy, int type, double E, double x, double y, int depth)
		Tree.add(PanC);
		TR= TransmissionRange;
		Grid_devices_Deploy (nx, ny);
	}
	
	ZigbeeNetwork (int nbre, double Xsize, double Ysize){
	    this.nbre=nbre;
		PanC = 	new Device(0,0,PAN_coordinator,-1,0,0,0);
		Tree.add(PanC);
		Xmin=-Xsize/2; Xmax= Xsize/2;
		Ymin=-Ysize/2; Ymax= Ysize/2;
		this.Xsize=Xsize; this.Ysize= Ysize;
		//... To be completed
	}
	
	void Grid_devices_Deploy (int nx, int ny, double dd){
		double x,y; // dd=TR
		nx--; ny--;/////////// [0,nx[, [0,ny[
		int idx=0, idy=0;
		for (int j=0; j<=nx; j++)
		{ idx=0;
			y= j*dd - nx*dd/2;
			for (int i=0;i<=ny;i++){
				x= i*dd-ny*dd/2;
				if (!(x==0 && y==0)){// don't add the PANc pan coordination , it is added
					Device d= 	new Device(idx-(nx+1)/2,idy-(ny+1)/2,1,EnergyInit,x,y);
			        Tree.add(d);
				   }
			   idx++;
			  }
		  idy++;
		}
	}
	
	// with different gi for nodes 	
	void Grid_devices_Deploy (int nx, int ny, double dd,double gi){
		double x,y; // dd=TR
		nx--; ny--;/////////// [0,nx[, [0,ny[
		int idx=0, idy=0;
		for (int j=0; j<=nx; j++)
		{ idx=0;
			y= j*dd - nx*dd/2;
			for (int i=0;i<=ny;i++){
				x= i*dd-ny*dd/2;
				if (!(x==0 && y==0)){// don't add the PANc pan coordination , it is added
					Device d= 	new Device(idx-(nx+1)/2,idy-(ny+1)/2,1,EnergyInit,x,y,gi);
			        Tree.add(d);
				   }
			   idx++;
			  }
		  idy++;
		}
	}
	
	
	
		void Grid_devices_Deploy (int nx, int ny){
			
			double dd=TR;
			 Grid_devices_Deploy (nx, ny, dd);
		
		
	}
	
	void displayMore(){
		for (Device d:Tree ){
			d.displayMore();
		}
	}
	
	void display(){
		for (Device d:Tree ){
			d.display();
		}
	}
	
	void displayTiming(){
		for (Device d:Tree ){
			d.display();
			d.displayTiming();
		}
	}
	
	
	int compute_depth ()
	{
	 int maxd=0;	
	
	 for (Device d:Tree )
	    if (d.depth>maxd) maxd=d.depth;
		
	 return(maxd);
	}
	
void adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline ){
	
	int maxdepth=compute_depth ();
	//System.out.println ("____depth="+maxdepth);
	for( int i=maxdepth; i>0;i--) 
	for (Device d:Tree ){
	 if (d.depth==i && d.type !=PAN_coordinator)	
	    d.FP.Ajust_BI_SD_deadline ( d,  Ttrans,   TackMax,  N, deadline );
	}
}


void adjust_frame_timing_our_approach_fire (double Ttrans,  double TackMax, int N, double deadline1, double deadline2_fire ){
	
	int maxdepth=compute_depth ();
	//System.out.println ("____depth="+maxdepth);
	for( int i=maxdepth; i>0;i--) 
	for (Device d:Tree ){
	 if (d.depth==i && d.type !=PAN_coordinator)	
		 if ( d.type==low_activity)   d.FP.Ajust_BI_SD_deadline ( d,  Ttrans,   TackMax,  N, deadline1 );
		 else d.FP.Ajust_BI_SD_deadline ( d,  Ttrans,   TackMax,  N, deadline2_fire );
	}
}



void adjust_frame_timing_ADCA_approach (double Ttrans,  double TackMax, int N ){
	FrameTimePeriods_ADCA FP_ADCA = new FrameTimePeriods_ADCA ();
	
	for (Device d:Tree )
	 if (d.type !=PAN_coordinator)	FP_ADCA.Ajust_SD ( d,  Ttrans,   TackMax,  N );
}


void adjust_frame_timing_CSB_approach (double Ttrans,  double TackMax, int N ){
	FrameTimePeriods_CSB FP_ADCA = new FrameTimePeriods_CSB ();
	
	for (Device d:Tree )
	 if (d.type !=PAN_coordinator)	FP_ADCA.Ajust_SD ( d,  Ttrans,   TackMax,  N );
}

//int low_activity=0; int fired=1; int fire_detection=2;  int neighbor_suspected=3;
//int in_path_active=4;  

void Nbre_discarted_Packets(double t) 
{
	for (Device d:Tree ){
		if (d.type!= PAN_coordinator)	d.TE.Nbre_discarted_Packets(t, d);
	}
}

	
void add_traffics(double gi)
{
	for (Device d:Tree ){
		if (d.type!= PAN_coordinator)	d.Ti.gi=gi;
	}
}
	



void Compute_dispaly_response_time (int type, double Ttrans,  double TAckMax, int N){
	
	for (Device d:Tree ){
		if (d.type!= PAN_coordinator)	{
			//d.displayShort();
			d.Compute_dispaly_response_time (type, Ttrans,  TAckMax, N);
		}
	}
	
	
}
/*
	
	public static void main(String[] args) {

		ZigbeeNetwork zn= new ZigbeeNetwork (5, 5);
		zn.display();
		
		
	}
*/

}
