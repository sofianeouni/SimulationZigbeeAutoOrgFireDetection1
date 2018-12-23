import java.util.ArrayList;


class child_coordinator implements ZigbeeParameters{
	double idx=0,idy=0;
	double idx_coor=0,idy_coor=0;
	child_coordinator (double idx, double idy,double idx_coor, double idy_coor){
		this.idx=idx; this.idy=idy;
		this.idx_coor=idx_coor;this.idy_coor=idy_coor;
		
	}
	child_coordinator (Device d){
		
		this.idx=d.idx; this.idy=d.idy;
		if (d.type==PAN_coordinator) {this.idx=0; this.idy=0;}
		else 
		   if (d.coordinator!=null){this.idx_coor=d.coordinator.idx;this.idy_coor=d.coordinator.idy;}
		   else {this.idx_coor=0; this.idy_coor=0;}
	}
}


public class Association implements ZigbeeParameters{

	ZigbeeNetwork ZN;
	ArrayList <child_coordinator> old_ch_coor=  new ArrayList <child_coordinator>();
	int Nbre_change=0;
	
	Association (ZigbeeNetwork ZN){
		this.ZN=ZN;
	}
	
	
	void copy_old_tree()
	{
		old_ch_coor.clear();
		for (Device d:ZN.Tree){
			old_ch_coor.add (new child_coordinator(d) );
		}
	}
	
	
	
	void Clear_Association()
	{
		for (Device d:ZN.Tree){
			
			d.coordinator=null;
			d.children.clear();
			d.path.clear();
			if (d.type !=PAN_coordinator)	d.depth=-1;
			
		}
	}
	
	int Nbre_Association_nodes_changed(){
		int nb=0;
		
			for (Device d:ZN.Tree)
	           for (child_coordinator cc : old_ch_coor)	
	        	   if (d.idx==cc.idx && d.idy==cc.idy)
	        		   if (d.coordinator!=null)
	        			  if (d.coordinator.idx!=cc.idx_coor || d.coordinator.idy!=cc.idy_coor)	
	        				  {nb++;
	        				 
	        				  d.TE.Nbre_discarted_Packets__reassociation(d, macResponseWaitTime);
	        				  
	        				  }
	        			  
	        		   
	        			  

	        				  
			
		return nb;
	}
	
	
	int Nbre_Association_nodes_changed_CSB(){
		int nb=0;
		boolean change=false;
		for (Device d:ZN.Tree)
	           for (child_coordinator cc : old_ch_coor)	
	        	   if (d.idx==cc.idx && d.idy==cc.idy)
	        		   if (d.coordinator!=null)
	        			  if (d.coordinator.idx!=cc.idx_coor || d.coordinator.idy!=cc.idy_coor)	
	        				  {change=true;
	        				  
	        				  }
		if (change)
			for (Device d:ZN.Tree)  				  
	        				  {d.TE.Nbre_discarted_Packets__reassociation(d, macResponseWaitTime);
	        				  nb++;
	        				  }
		return nb;
	}
	
	
	
	void association (int typeAssociation)
	{ 
		copy_old_tree();
		Clear_Association();
	
		while (ExistDeviceNotAssociated (ZN.Tree)){
			
			Boolean change= false;
			for (Device d:ZN.Tree)
				if (d.type!=PAN_coordinator && d.node_state!=fired)
				{//d.coordinator==null && 
				  ArrayList <Device> parents= ListParentsRange(d);
				  
				  Device parent=null;
				  switch (typeAssociation){
				    case association_depth: parent= SelectParentAssociateDevices_Depth ( parents); break;
				    case association_depth_LQI: parent=SelectParentAssociateDevices_Depth_LQI ( d, parents); break;
				    case association_depth_Energy: parent=SelectParentAssociateDevices_Depth_Energy ( parents); break;
				    case association_depth_Energy_Trust_Node: parent=SelectParentAssociateDevices_Depth_Energy_strust_Node ( parents); break;
				    case association_depth_CSB: parent= SelectParentAssociateDevices_Depth ( parents); break;
				    case association_depth_ADCA: parent= SelectParentAssociateDevices_Depth ( parents); break;
				    default :System.out.println("Errrrrrrrror!!!!!!!");break;	  
				  }  
				  
				  if (parent != null)	{AssociateToParent(d,parent); 
				                         change=true;}
				  
				   //else System.out.println(" network fragmentation!!!");
				 }			
			 
			// if no change then exit 
			if (!change) break;
		  }
		
		for (Device d:ZN.Tree) d.determine_path();
		Traffic.compute_fi_all_nodes(ZN ); // compute forwarded traffic depending on the established association
		
		int nbch=0;
		if (typeAssociation==association_depth_CSB ) nbch=Nbre_Association_nodes_changed_CSB();
		else nbch=Nbre_Association_nodes_changed();
		//System.out.println ("+++++++++++++> nbre change="+nbch);
		Nbre_change+=nbch;
	//	System.out.println ("+++++++++++++> nbre change Total="+Nbre_change);
	}
	
	
	void AssociateToParent(Device d,Device parent){
		
		d.coordinator=parent;
		
		d.depth=parent.depth+1;
		parent.children.add (d);	
	}
	
	
	Device 	SelectParentAssociateDevices_Depth_LQI ( Device N, ArrayList <Device> parents){
		  // select the parent
			int min_Depth =Integer.MAX_VALUE;
			Device Pselect=null;
			// min depth
			for (Device p:parents ){
				if (p.depth<min_Depth && p.node_state !=fired && (p.E>0 ||p.type==PAN_coordinator) ) {min_Depth= p.depth;	Pselect=p;  }
				}
			
		 // Max LQI depend in the distance (min) for the link
			double distMin=Integer.MAX_VALUE;;
			for (Device p:parents )
				if (p.depth==min_Depth  && p.node_state !=fired &&  (p.E>0 ||p.type==PAN_coordinator)) {
					    double d= ellipse.distance2 (p.pd,N.pd );
					    if (distMin>d){ distMin=d;  Pselect=p;  }
				 }
				
			
		 return Pselect;
			
		}
	
	
	Device 	SelectParentAssociateDevices_Depth_Energy (   ArrayList <Device> parents){
		  // select the parent
			int min_Depth =Integer.MAX_VALUE;
			Device Pselect=null;
			// min depth
			for (Device p:parents )
				if (p.depth<min_Depth && p.node_state !=fired &&  (p.E>0 ||p.type==PAN_coordinator)) {min_Depth= p.depth;	Pselect=p;  }
				
			
		 // Max Energy
			
			double EMax=0;
			for (Device p:parents )
				if (p.depth==min_Depth  && p.node_state !=fired &&  (p.E>0 ||p.type==PAN_coordinator)) 
					    if (p.E>EMax){ EMax=p.E;  Pselect=p;  }
				 
		 return Pselect;
			
		}
	
	
	Device 	SelectParentAssociateDevices_Depth_Energy_strust_Node (   ArrayList <Device> parents){
		  // select the parent
			int min_Depth =Integer.MAX_VALUE;
			Device Pselect=null;
			// min depth
			for (Device p:parents )
				if (p.depth<min_Depth && p.node_state !=fired && p.node_state !=neighbor_suspected 
				                                 &&  (p.E>0 ||p.type==PAN_coordinator)) {min_Depth= p.depth;	Pselect=p;  }
				
			
		 // Max Energy
			
			double EMax=0;
			for (Device p:parents )
				if (p.depth==min_Depth  && p.node_state !=fired &&  p.node_state !=neighbor_suspected 
				                                                     && (p.E>0 ||p.type==PAN_coordinator)) 
					    if (p.E>EMax){ EMax=p.E;  Pselect=p;  }
			
			
			
			// second choice
			if (Pselect==null){
				min_Depth =Integer.MAX_VALUE;
				
				for (Device p:parents )
					if (p.depth<min_Depth && p.node_state !=fired  
					                                 &&  (p.E>0 ||p.type==PAN_coordinator)) {min_Depth= p.depth;	Pselect=p;  }
					
				
			 // Max Energy
				
				 EMax=0;
				for (Device p:parents )
					if (p.depth==min_Depth  && p.node_state !=fired &&        (p.E>0 ||p.type==PAN_coordinator)) 
						    if (p.E>EMax){ EMax=p.E;  Pselect=p;  }
				
			}
				 
		 return Pselect;
			
		}
	//neighbor_suspected
	
	
	Device 	SelectParentAssociateDevices_Depth ( ArrayList <Device> parents){
	  // select the parent
		int min_Depth =Integer.MAX_VALUE;
		Device Pselect=null;
		// min depth
		for (Device p:parents ){
			if (p.depth<min_Depth && p.node_state !=fired &&  (p.E>0 ||p.type==PAN_coordinator)) {min_Depth= p.depth;	Pselect=p;  }
			}
	 return Pselect;
		
	}
	
	
	ArrayList <Device> ListParentsRange(Device d1){
		ArrayList <Device> p = new ArrayList <Device>();
	
		for (Device d:ZN.Tree )
			if (d.depth!=-1 && inTranmissionRange( d1, d, ZN.TR)) p.add(d);
		return p;
	}
	

	static boolean inTranmissionRange( Device d1, Device d2,double TR){

		double r=Math.sqrt((d1.pd.x-d2.pd.x)*(d1.pd.x-d2.pd.x)+(d1.pd.y-d2.pd.y)*(d1.pd.y-d2.pd.y));
		
		if (r>TR) return false;
		else return true;
	}
	
	
	
	boolean ExistDeviceNotAssociated (ArrayList <Device> Tree){ //////******
		for (Device d:Tree )
			if (d.node_state!=fired)
			        if (d.depth==-1) return true;
		
		return false;
		
	}
	
	int MaxDepth (ArrayList <Device> Tree){
		int max=0;
		for (Device d:Tree ){
			if (d.depth>max) max=d.depth;
		}
		return max;
		
	}
	
	/*
	public static void main(String[] args) {
		
		ZigbeeNetwork ZN=new ZigbeeNetwork (8, 8);
		//ZN.add_traffics(1/100); // 1p/10 seconds 
		
		Association Asso= new Association (ZN);
		Asso.association(association_depth);
		//Asso.association(association_depth_LQI);
		//Asso.association(association_depth_Energy);
		//adjust_frame_timing_our_approach (double Ttrans,  double TackMax, int N, double deadline )
		ZN.adjust_frame_timing_our_approach (0.01,  0.1, 0, 3 );
		ZN.display();
		System.out.println("@@@@@@@@@@@@@@@@@");
		
	//	ZN.displayTiming();
		DrawNetwork dn= new DrawNetwork (ZN,null);
	}
*/
}
