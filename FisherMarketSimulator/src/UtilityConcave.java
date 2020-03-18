 

public class UtilityConcave extends UtilityLinear {
	static public double ro;

	public UtilityConcave(Buyer buyer, Good good,
			double ro) {
		super(buyer, good);
		this.ro=ro;
		
	}
	public UtilityConcave(double linearUtility,double ro){
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
		UtilityConcave l=new UtilityConcave( linearUtility,ro);
		return l;
	}




}
