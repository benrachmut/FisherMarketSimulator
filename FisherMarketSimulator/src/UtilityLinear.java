
public class UtilityLinear extends Utility {
	
	
	protected double linearUtility;	
	
	public UtilityLinear(Buyer buyer, Good good,double linearUtility) {
		super(buyer, good);
		this.linearUtility = linearUtility;
	}
	
	
	public UtilityLinear(double linearUtility) {
		super();
		this.linearUtility = linearUtility;
	}


	public UtilityLinear(Buyer buyer, Good good) {
		super(buyer, good);
	}





	@Override
	public String toString() {
		return "LinearUtility [linearUtility=" + linearUtility + "]";
	}


	public double getUtility(double ratio) {// returns related part of the utility
		return ratio*linearUtility;
	}


	@Override
	public Object clone() {
		UtilityLinear l=new UtilityLinear(this.buyer, this.good, linearUtility);
		return l;
	}

	
  // && agent.getStatus()==Status.WORKING
	/*private double calculatePenalty(double tnow) {
		double h=(5-task.getPriority());
		double uti= task.getTotalUtility();
			
		return Math.max(h*Utility.minAbanPenalty, (uti/10)*Math.pow(0.5,task.getDoneWorkload()/Utility.timeUnit));
		
	}*/


	
	

}
