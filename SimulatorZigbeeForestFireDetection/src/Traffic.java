import java.util.ArrayList;


public class Traffic {
double fi, gi; //forwarded and generated traffics
ArrayList<Double> Fi = new ArrayList<Double>(); // traffics  
	
Traffic()
{
	fi=0;
	gi=0;
}
Traffic(double gi){
	this.gi=gi;
}

static double compute_fi(Device Ni,ZigbeeNetwork zn ){
	double fi=0;
	Ni.Ti.Fi.clear();
	//System.out.println("-----cont----");
	for (Device d:zn.Tree)
	{ 
		if (d.path.contains(Ni)) {fi+=d.Ti.gi;
		            Ni.Ti.Fi.add(d.Ti.gi);
		}
	/*	System.out.println("-----cont---- ("+Ni.idx+","+Ni.idy+")");
		for (Device p:d.path)
	{System.out.println("("+p.idx+","+p.idy+")");
		if (p.idx==Ni.idx && p.idy==Ni.idy) {fi+=d.Ti.gi; }
		
	} */
		
	}
	//System.out.println("-----______________---");
	
	return fi;
	
}

static void compute_fi_all_nodes(ZigbeeNetwork zn ){
	for (Device d:zn.Tree){
		d.Ti.fi=compute_fi(d,zn );
		//System.out.println("����fi="+d.Ti.fi);
	}
}



	

}
