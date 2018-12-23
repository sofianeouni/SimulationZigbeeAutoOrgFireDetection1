
public interface ZigbeeParameters extends EnergyParameters {

	// packets transmission times 
	double DefaultPacketTime = 0.00032; // 10 bytes, so Tt is 3.2 msec on a	250 Kbps radio.
	double DefaultPacketSize=100*8;
	double DefaultAckTime = 0.00016;      //(5*8)/250000;     4 bytes on a	250 Kbps radio.
	//double macAckWaitDuration=  6*5.3; // macAckWaitDuration=aUnitBackoffPeriod+aTurnaroundTime phySHRDuration ceiling(6 × phySymbolsPerOctet)
	int MaxRetransmission=3;
	int W_default=31;///
	int aMaxLostBeacons=4;
	
	
	 double aBaseSuperFrameDuration =    0.024; //aBaseSuperFrameDuration =  24 ms (915 GHz PHY)
	 double macResponseWaitTime= aBaseSuperFrameDuration*16; // 32 max , 1- is the average value 
	 
	int BO_default=7;
	int SO_default=5;
	
	int T_BO[]={15,7,3,1}; // value are multiples : 16,8,4,2
	
	double DefaultBeaconPeriod=3; //3 seconds BI = aBaseSuperframeDuration · 2^BO, BO=7
	                              //  BI= [ 24ms (BO=1), 6,55mn (BO=14)]
	double BI_Default= DefaultBeaconPeriod; // same thing
	double SD_default=0.387; //0,387 second= active period default value in Zigbee [24ms (SO=1), 4,28mn (SO=13) ]  
	double DefaultActivePeriod = SD_default; // same thing
	double gi_default= 1.0/60.0; //1.0d/60.0d; // 1 packet/minute generated traffic 
	//double gi_default= 1.0/10.0;// 1 packets every  10 seconds.
	double MacFrameTime= 1000;
	double TransmissionRange = 100;
	//double EnergyInit=30;  //Energy J for full working up to 2 months (20ma*3V) j/s 
    //double EnergyInit=e_rx*3600*10; // 10 h of full functioning e_rx
    //double EnergyInit=1.2*3600/3.0;// Energy= 1200 mA * 3600s / 3=      1440J
	double EnergyInit=1.6*2*3600.0/3.0;                    // 2 AA NiMH battery 1600
	double EnergyThreshold=EnergyInit/4;
	double DetectionDistance=2; 
	
	int low_activity=0; int fired=1; int fire_detection=2;  int neighbor_suspected=3;
	int in_path_active=4;  int energy_Low=5;
	
	int PAN_coordinator=0; int parent_coordinator=1; int RFD=2;
	
    int association_depth=0; int association_depth_LQI=1; int association_depth_Energy=2; int association_depth_Energy_Trust_Node=3;
    int association_depth_CSB=4; int association_depth_ADCA=5;
}
