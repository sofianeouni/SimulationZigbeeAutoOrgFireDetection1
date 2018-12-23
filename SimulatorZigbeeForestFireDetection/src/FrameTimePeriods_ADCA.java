
//ADCA: Adaptative Duty Cycle Algorithm for Energy Efficiant IEEE 802.15.4 
public class FrameTimePeriods_ADCA extends FrameTimePeriods  {
	FrameTimePeriods_ADCA (){
		super();
	}
	
	
	
	void Ajust_SD (Device d, double Ttrans,  double TackMax, int N ){

		double []T= new double[5];
		
		
		T=Ajust_SD_traffics (d.FP.BI,d.Ti.gi,  d.Ti.Fi,  Ttrans, TackMax,  N );
		
		d.FP.SO=(int) T[1];
		d.FP.SD= T[0];
		
		//d.FP.BI=index_to_time (d.FP.BO);
		//
		
	//	System.out.println("BI="+d.FP.BI+" SO="+d.FP.SO);
		
		double Tr=ResponseTime.Rt (d.FP.SD, d.FP.BI, d.Ti.gi, d.Ti.Fi, Ttrans,  TackMax, N );
		
				//index_to_time (d.FP.SO); 
		//d.FP.BO=(int)T[1]; 
		//d.FP.BI=index_to_time (d.FP.BO); 
		d.R1=Tr;
		
		d.FP.compute_DutyCycle ();
		
		
	}
	
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}*/
	

}
