 

public class ConcaveUtility extends LinearUtility {
	static public double ro;

	public ConcaveUtility(Buyer buyer, Good good,
			double ro) {
		super(buyer, good);
		this.ro=ro;
		
	}
	public ConcaveUtility(double linearUtility,double ro){
		super(linearUtility);
		this.ro=ro;
	}
	
	public double getUtility(double ratio) {// returns related part of the utility
		return Math.pow(ratio*linearUtility,ro);
	}
	
	public double getLinearUtility(){
		return linearUtility;
	}
	
	@Override
	public Object clone() {
		ConcaveUtility l=new ConcaveUtility( linearUtility,ro);
		return l;
	}




}
