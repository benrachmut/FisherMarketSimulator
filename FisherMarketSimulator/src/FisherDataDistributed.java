
public class FisherDataDistributed extends FisherData {


	private int parameter;
	private String distributionDelay;
	private String distributionParameter;

	public FisherDataDistributed(Double[][] allocation, Utility[][] r, int iterations, Market market, Mailer mailer) {
		super(allocation,r,iterations,market, "distributed");
		
		considerDecisionCounter = MainSimulator.considerDecisionCounter;

		this.parameter = mailer.getParameter();
		int distDelayNumber= mailer.getDistributionDelay();
		int distParamNumber= mailer.getDistributionParameter();


		if (distDelayNumber == 1) {
			distributionDelay = "Uniform";
		}
		if (distDelayNumber == 2) {
			distributionDelay = "exp";

		}
		
		
		if (distParamNumber == 1) {
			distributionParameter = "Uniform";
		}
		if (distParamNumber == 2) {
			distributionParameter = "exp";

		}
		
		
	}

	public FisherDataDistributed(FisherData copiedFisherData, int iterationOfCopied) {
		super(copiedFisherData, iterationOfCopied);
		
		if (copiedFisherData instanceof FisherDataDistributed) {
			
			FisherDataDistributed fdd = (FisherDataDistributed)copiedFisherData;
			considerDecisionCounter = MainSimulator.considerDecisionCounter;
			this.distributionDelay=fdd.getDistributionDelay();
			this.parameter=fdd.getParamter();

		}else {
			System.err.println("sent a weird copiedFisherData");
			throw new RuntimeException();
		}
	}

	public int getParamter() {
		return this.parameter;
	}

	public String getDistributionDelay() {
		return this.distributionDelay;
	}

	public FisherDataDistributed(int idF, int numByuersF, int numGoodsF, int iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF, double avgR, double avgX, double avgRX,
			String distributionDelay,String distributionParameter, int parameterF) {
		super( idF,  numByuersF,  numGoodsF,  iterationF,  algoF,
				 considerDecisionCounterF,  maxIterationF,  avgR,  avgX,  avgRX);
		considerDecisionCounter = MainSimulator.considerDecisionCounter;
		this.distributionDelay= distributionDelay;
		this.distributionParameter=distributionParameter;
		this.parameter=parameterF;
		// TODO Auto-generated constructor stub
	}
/*
	public int getUb() {
		// TODO Auto-generated method stub
		return this.ub;
	}

	public double getP4() {
		// TODO Auto-generated method stub
		return this.p4;
	}

	public double getP3() {
		// TODO Auto-generated method stub
		return this.p3;
	}
	*/

	@Override
	public String toString() {
		String ans = super.toString();
		ans = ans+","+this.parameter+","+this.distributionDelay+","+this.distributionParameter;
		return ans;
	}

	public String getDistributionParameter() {
		// TODO Auto-generated method stub
		return this.distributionParameter;
	}
}
