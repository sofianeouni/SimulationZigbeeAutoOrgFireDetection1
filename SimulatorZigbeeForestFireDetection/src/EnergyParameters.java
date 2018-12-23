
public interface EnergyParameters {
  
	double VBattery=3; // 3valtage
	double e_tx=(VBattery*11.0)/1000.0;
	double e_rx=(VBattery*19.7)/1000.0;
	double e_idle=(VBattery*20)/1000000.0;
	double e_sleep=(VBattery*1)/1000000.0;
	
}
