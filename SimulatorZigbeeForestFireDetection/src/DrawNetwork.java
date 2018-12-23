import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import javax.swing.*;





class Panel1 extends JPanel implements ZigbeeParameters{
	
TheTime Time = new TheTime();
	ZigbeeNetwork zn;
	int Xw,Yw;
	FirePropagation Fp;
	
	point CR; // center of grid and (rep�re)
	double scal; //scale for display from the grid dim
	
	void init() {
		
	}
	
	
	point changeCoord (point p)
	{
		point p1= new point(p.x*scal+CR.x,p.y*scal+CR.y);
	return p1;
	}
	
	point changeCoord (point p,double scal,point CR)
	{
		point p1= new point(p.x*scal+CR.x,p.y*scal+CR.y);
	return p1;
	}
	
	point ChangeCoordGrid (point p, int nx, int ny, double TR){
		double d= Math.min(Xw/nx,Yw/ny);
		CR= new point (Xw/2,Yw/2);
		 scal=d/TR*1.3;
		point f= new point(changeCoord(p,scal,CR));
		
		return (f);	
		
	}
	
	void displayDevice (Device d,Graphics g, int nx, int ny, double TR ){
		
		 DrawLinks(d,g, nx,ny, TR ); //with link
		
		 // draw node
		 point p= ChangeCoordGrid (d.pd, nx, ny, TR);
		 if (d.type==PAN_coordinator) setNode (g,(int) p.x, (int) p.y, Color.black ,d);//d.type,d.E,d.FP.BO,d.FP.SO ,d.R1);
		 else  
			 if (d.node_state==fired) setNode (g,(int) p.x, (int) p.y, Color.red, d);
			 else if ( d.E<=0) setNode (g,(int) p.x, (int) p.y, Color.red, d);
			else if (d.node_state==in_path_active) setNode (g,(int) p.x, (int) p.y, Color.green, d);
			 else if (d.node_state==neighbor_suspected) setNode (g,(int) p.x, (int) p.y, Color.gray, d);
			 else setNode (g,(int) p.x, (int) p.y, Color.blue, d);
		 
		 
		 
	}
	
	void DrawLinks(Device d,Graphics g, int nx, int ny, double TR )
	{Graphics2D  g2d = (Graphics2D)g;
		//g2d.draw(new Line2D.Double(0,0, 400,400));
		if (d.coordinator==null) return;
		
		point p1=  ChangeCoordGrid (d.pd, nx, ny, TR);
		point p2= ChangeCoordGrid (d.coordinator.pd, nx, ny, TR);
		
		g2d.setColor(Color.blue);
		
		g2d.draw(new Line2D.Double(p1.x,p1.y, p2.x,p2.y));
	}
	
	void displayNetwork (Graphics g )
	{
		for (Device d:zn.Tree){
			displayDevice ( d, g,  zn.nx,  zn.ny, zn.TR );
			
		}
	}
	
	
	Panel1 (ZigbeeNetwork zn,int Xw,int Yw ,FirePropagation Fp)
	{super();
	this.Fp=Fp;
	this.zn= zn;
	this.Xw=Xw; this.Yw=Yw;
	init();
	}
	
	Panel1 (ZigbeeNetwork zn,int Xw,int Yw)
	{super();	this.zn= zn;
	this.Xw=Xw; this.Yw=Yw;
	init();
	}
	Panel1 (ZigbeeNetwork zn,FirePropagation Fp)
	{super();
	this.zn= zn;	this.Fp=Fp;
	this.Xw=(int) this.getSize().getWidth(); this.Yw=(int) this.getSize().getHeight();
	init();
	}
	
	Panel1 (ZigbeeNetwork zn)
	{super();
	this.zn= zn;
	this.Xw=(int) this.getSize().getWidth(); this.Yw=(int) this.getSize().getHeight();
	init();
	}
	
	public void repaint (double t){
		Time.Time_conversion (t); 
               
               Graphics g=this.getGraphics();
               //paintComponent(g);
                      // g.clearRect (0, 0, 70, 40);
		  this.Xw=(int) this.getSize().getWidth(); this.Yw=(int) this.getSize().getHeight();
		  //System.out.println("::::::::::::::::");
		  
		  displayNetwork (g);
		  displayEllipse ( g);
		  Time.display(g);
                
                  
                  
	}
	
	

	//d.type,d.E,d.FP.BO,d.FP.SO ,d.R1);
	  // int type, double E, int BO,int SO )
	public void setNode (Graphics g, int x, int y, Color c,Device d ) {
	int type=d.type; 
	double E=d.E; int BO=d.FP.BO; int SO=d.FP.SO;
	double R1=d.R1;
	double duty=d.FP.DutyCycle*100; 
	Graphics2D ga;
    ga = (Graphics2D)g;

	ga.setPaint(c );
	ga.setColor(c);
	Shape circle;
	if (type==PAN_coordinator) circle = new Ellipse2D.Float(x-20/2, y-20/2, 20, 20);
	else 	circle = new Ellipse2D.Float(x-10/2, y-10/2, 10, 10);
	ga.fill(circle);
	//if (E!=-1 && c!=Color.red)	setEnergyLevel (g, x+5, y-3,  type, E );
	
	if (type!=PAN_coordinator){
	ga.setColor(Color.gray);
	Font myFont = new Font ("Times New Roman", 1, 9);
	ga.setFont (myFont);
	DecimalFormat df = new DecimalFormat("##.#");
	//String S= new String("(BO="+BO+",SO="+SO+") ");
       
	String S= new String("("+BO+","+SO+") ");
	//String S= new String("(BO="+BO+",SO="+SO+") "+df.format(duty)+"%");
	//
	//int xx,yy; xx= (int) d.idx;yy=(int) d.idy;
	//String S= new String("("+xx+","+yy+")");
	//String S= new String(df.format(duty)+"%");
	ga.drawString(S, x, y); //////////////////
	
	//S=new String( " R1="+df.format(R1));
	//ga.drawString(S, x, y+10);
	
	}
	//ga.dispose( );
	}
	
	public void setEnergyLevel (Graphics g, int x, int y,  int type, double E ) {
		if (type==PAN_coordinator) return; 
		Graphics2D ga;
	    ga = (Graphics2D)g;
        double l=20,r=E/EnergyInit;
		ga.setPaint(Color.black );
		ga.setColor(Color.green);
		Shape rect;
		rect = new Rectangle2D.Double(x, y, 10, l);
		ga.draw (rect);
		
		rect = new Rectangle2D.Double(x, y+l*(1-r), 10, l*r);//// energy
		ga.draw (rect);
		
		ga.fill(rect);
		//ga.dispose( );
		}
	
	void displayEllipse (Graphics g)
	{
		if (Fp==null) return;
		Graphics2D  g2d = (Graphics2D)g;
		 
		g2d.setColor(Color.red);
		point pc= changeCoord ( Fp.PC);
		Shape ell;
		
		//ell = new Ellipse2D.Double(pc.x-(Fp.ell.a*scal)/2, pc.y-(Fp.ell.b*scal)/2, Fp.ell.a*scal, Fp.ell.b*scal);
		// g2d.rotate(Fp.Od, pc.x-(Fp.ell.a*scal)/2, pc.y-(Fp.ell.b*scal)/2);
		 
		 ell = new Ellipse2D.Double(pc.x-(Fp.ell.a*scal), pc.y-(Fp.ell.b*scal), Fp.ell.a*scal*2, Fp.ell.b*scal*2);
		
		 g2d.rotate(Fp.Od, pc.x, pc.y);
		 
		 //System.out.println(" x="+pc.x+", y="+pc.y) ;
		g2d.draw(ell);
		
		 g2d.rotate(-Fp.Od, pc.x, pc.y);
	}

	
}






public class DrawNetwork extends JFrame implements ZigbeeParameters{
	int Xw=1000,Yw=1000;
	ZigbeeNetwork zn;
	FirePropagation Fp=null;
	Panel1 p;
        JLabel jLabel1 = new javax.swing.JLabel();
         
        
        void init(){  
           // jLabel1.setText("Time 00:00:00");
          //  p.add(jLabel1);
            
            addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                System.out.println("llllllllllllllllllll END");
               // formWindowClosed(evt);
            }
        });
        }
        
	DrawNetwork (ZigbeeNetwork zn){
		super("Draw network");
		this.zn=zn;
	       
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(Xw,Yw);
         p= new Panel1(zn,  Fp);
        this.getContentPane().add(p);
        init();
        setVisible(true);
		
	}
	
	DrawNetwork (ZigbeeNetwork zn, FirePropagation Fp){
		super("Draw network");
		this.zn=zn;
	    this.Fp=Fp;   
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(Xw,Yw);
         p= new Panel1(zn,  Fp);
        this.getContentPane().add(p);
        init();
        setVisible(true);
		
	}
	
	
	
/*
	
	public static void main(String[] args) {
		
		//ZigbeeNetwork zn= new ZigbeeNetwork (50, 50);
		//zn.display();
          
		 double Od=Math.PI/10;
		 double v=20;
		 double R=50;
		 point PC = new point(70,70);
		
		 
		FirePropagation Fp= new FirePropagation(PC, v, Od, R);
		
	
		
		double dd= TransmissionRange/ Math.sqrt(2);  // diagonal=TransmissionRange
				
		ZigbeeNetwork zn= new ZigbeeNetwork (5, 5,dd);
		
		Fp.Change_Node_State( zn); // state...........
		
		zn.display();	
		Association Asso= new Association (zn);
		//Asso.association (association_depth);
	//	Asso.association(association_depth_LQI);
		Asso.association(association_depth_Energy);
		//Traffic.compute_fi_all_nodes(zn );
		
		System.out.println ("end of  association!!!");
		DrawNetwork dn= new DrawNetwork (zn,Fp);
	
		for (int i=0; i<5;i++)
		{
			zn.displayMore();
			
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		  //Fp.FireProgress (Fp.R+=20);
		  //Fp.Change_Node_State( zn);
		  //Asso.association(association_depth_Energy);
		  dn.p.repaint();
		}
		
	}
*/

}
