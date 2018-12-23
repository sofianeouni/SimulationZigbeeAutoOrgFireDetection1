import java.math.BigInteger;
import java.util.ArrayList;




public class FrameTimePeriods implements ZigbeeParameters {
double SD , BI;
int SO=5; // interval [1,14]
int BO=7;// interval [0,15]
double DutyCycle;




FrameTimePeriods (){
	 BO= BO_default;  SO=SO_default;
	 compute_SD_BI (SO,BO );
	 compute_DutyCycle ();
	 
}

void display ()
{
	System.out.println("BI="+BI+" SD="+SD+" Duty Cycle="+DutyCycle);
	System.out.println("BO="+BO+" SO="+SO);
}


FrameTimePeriods (double SD, double BI){
	this.SD=SD; this.BI=BI;
	
	compute_DutyCycle ();
}

FrameTimePeriods (int SO, int BO){
	this.SO=SO; this.BO=BO;
	compute_SD_BI ( SO, BO );
	
	compute_DutyCycle ();
}


void compute_SD_BI (int SO,int BO ){
	//3 seconds BI = aBaseSuperframeDuration · 2^BO, BO=7
	if (SO>BO) System.out.println ("Error SO>BO!!");
	if (BO>14 || SO>13 || BO<0 || SO<1 || SO>BO ) { System.out.println("OB or SO over values!!"); 
	      BO= BO_default;  SO=SO_default;
	}
	
	SD= aBaseSuperFrameDuration*Math.pow(2,SO);
	BI= aBaseSuperFrameDuration*Math.pow(2,BO);
}

void compute_DutyCycle ()
{
	DutyCycle= SD/BI;
}


//{(double)SOi,SDi};
double [] Ajust_SD_traffics (double BI, double gi, ArrayList<Double> Fi, double Ttrans, double TackMax, int N ) // assumption no cumulative work to the next BI
{
	int F =ResponseTime.F_compute(Fi, BI);
	int G= (int) (gi*BI)+1;
	int NbreTrafics= F+G; // for BI period
	int SOi=1;
	double TSD= NbreTrafics*(Ttrans+TackMax*N);
	//System.out.println ("NbreTrafics="+ NbreTrafics);
	double SDi= aBaseSuperFrameDuration*Math.pow(2,SOi);
	while (SOi<14 && SDi<TSD)
		{SOi++;
		SDi= aBaseSuperFrameDuration*Math.pow(2,SOi);
		}
	double T[]= {(double)SOi,SDi};
	
	return (T);
}

int compute_SO (double SD){
	return ((int) (Math.log(SD/aBaseSuperFrameDuration)/Math.log(2)));
}


double index_to_time (double v ){
	return( aBaseSuperFrameDuration*Math.pow(2,v));
}

   

// {(double)SOi,SDi,(double)BOi,BIi, Ri};
double[] Ajust_Max_BI_Min_SD_traffics_deadline1 (int ind_BO, double gi, ArrayList<Double> Fi, double Ttrans,  double TackMax, int N, double deadline )// assumption no cumulative work to the next BI
{
	
	// double T_BO[]={15,7,3,1};
	double Tr=deadline+1;
	double BIi=0;
	int BOi=0;
	
	double SDi=0;
	int SOi=0;
	double T[]=new double[5];
	
	if (ind_BO<0 || ind_BO>4) ind_BO=0;
	 // else BOi=T_BO[ind_BO];
	
	
	
	// find max BI and min associated SD 
	while (Tr>deadline && ind_BO<4 ){
	
	  BOi=T_BO[ind_BO];	
	  BIi=index_to_time (BOi);
	
	  T=Ajust_SD_traffics (BIi,gi,  Fi,  Ttrans, TackMax, N );////
	
	  SOi=(int) T[0];
      SDi=T[1];
      Tr=ResponseTime.Rt (SDi, BIi, gi, Fi, Ttrans,  TackMax, N );
   
      if ((BIi-SDi)>=0)  Tr=ResponseTime.Rt (SDi, BIi, gi, Fi, Ttrans,  TackMax, N );
       else Tr=-1;
    
	  ind_BO++;
	} 

	
//	System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
//	System.out.println("  Tr="+Tr+" deadline="+ deadline);
	  if (Tr> deadline) System.out.println("Deadline not respected!! Tr="+Tr+" deadline="+ deadline);
	  if (SOi>BOi) {SOi=BOi; SDi=BIi;}
	  Tr=ResponseTime.Rt (SDi, BIi, gi, Fi, Ttrans,  TackMax, N );
	  
	double TT[]= new double[] {(double)SOi,SDi,(double)BOi,BIi, Tr};

	return TT;
}


//{(double)SOi,SDi,(double)BOi,BIi, Ri};
double[] Ajust_Max_BI_Min_SD_traffics_deadline (int ind_BO, double gi, ArrayList<Double> Fi, double Ttrans,  double TackMax, int N, double deadline )// assumption no cumulative work to the next BI
{
	// double T_BO[]={15,7,3,1};
	double Tr=deadline+1;
	double BIi=0;
	int BOi=0;
	
	double SDi=0;
	int SOi=0;
	double T[]=new double[5];
	
	if (ind_BO<0 || ind_BO>4) ind_BO=0;

	
	
	for (int i=ind_BO;i<4;i++){
	  BOi=T_BO[i];	
	  BIi=index_to_time (BOi);
	
	  T=Ajust_SD_traffics (BIi,gi,  Fi,  Ttrans,  TackMax,  N );
	
	  SOi=(int) T[0];     SDi=T[1];
	  if (BIi>=SDi){  Tr=ResponseTime.Rt (SDi, BIi, gi, Fi, Ttrans,  TackMax, N );
                     if (Tr<=deadline) break;}
	  else break;
	} 

	
 if (BOi<SOi){ BOi=SOi; BIi=SDi;} 	
	 Tr=ResponseTime.Rt (SDi, BIi, gi, Fi, Ttrans,  TackMax, N );
	 
	//System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
	//System.out.println("  Tr="+Tr+" deadline="+ deadline);
	
	  
	double TT[]= new double[] {(double)SOi,SDi,(double)BOi,BIi, Tr};

	return TT;
}

//{(double)BOi,BIi};
double[] Ajust_BI_childrenNodes (Device d )// PGCD BI, 
{
	double T[]=new double[2];
	BigInteger b1 = BigInteger.valueOf(d.children.get(0).FP.BO+1);
	
	for(Device dc:d.children){
		BigInteger b2 = BigInteger.valueOf(dc.FP.BO+1);
		b1 = b1.gcd(b2);}
	T[0]=b1.intValue()-1;
	T[1]=index_to_time (T[0]);
	
	return T;
}

//{(double)SOi,SDi,(double)BOi,BIi, Ri}; for parent node and child node  ///////////////////////
double[] Ajust_BI_SD_Child_traffics_deadline (Device d, double Ttrans,  double TackMax, int N, double deadline )
{
	//d.display();////
	double T[]={15,0};
	if (d.children.size()!=0) 	T= Ajust_BI_childrenNodes ( d );
	
	// double T_BO[]={15,7,3,1};
	int ind_BO=3;
	//T_BO
	for (int i=0; i<4;i++)
		if (T[0]==T_BO[i]){ind_BO=i; break;} 
	
	
	                                                    //(int) T[0]
	double TT[]= Ajust_Max_BI_Min_SD_traffics_deadline (ind_BO, d.Ti.gi, d.Ti.Fi, Ttrans,  TackMax, N, deadline );
	if (TT[2]==0){ System.out.println("Deadline !! node ="); d.display();}
	
	return TT;
}


void Ajust_BI_SD_deadline (Device d, double Ttrans,  double TackMax, int N, double deadline ){

	double []T= new double[5];
	T=Ajust_BI_SD_Child_traffics_deadline ( d,  Ttrans,   TackMax,  N, deadline );
	d.FP.SO=(int) T[0];d.FP.SD=T[1]; d.FP.BO=(int)T[2]; d.FP.BI=T[3];d.R1=T[4];
	d.FP.compute_DutyCycle ();
}
	
/*
	public static void main(String[] args) {
		
		double []T= new double[4];
		FrameTimePeriods FP= new FrameTimePeriods();
		// default values 
		System.out.println("BI="+FP.BI+" SD="+FP.SD+" Duty Cycle="+FP.DutyCycle);
		ArrayList<Double> F=new ArrayList<Double>();
		F.add(5d); F.add(5d);
		T=FP.Ajust_SD_traffics (FP.BI, 1, F, 0.004, 0.0002, 1 );
		
		FP.SD=T[1];
		FP.SO=(int) T[0];
		FP.compute_DutyCycle ();
		
		FP.display();
		
		// second example 
		double dd=1;
		System.out.println(" adjust values--- Deadline="+dd);
		T=	FP.Ajust_Max_BI_Min_SD_traffics_deadline (-1, 1, F, 0.01,  0.1, 0, dd );
		FP.SO=(int) T[0];FP.SD=T[1]; FP.BO=(int)T[2]; FP.BI=T[3];
		FP.compute_DutyCycle ();
		FP.display();
		
			

	}
	*/

}
