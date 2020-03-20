
public class FisherDataDistributed extends FisherData {


	private int parameter;
	private String distribution;

	public FisherDataDistributed(Double[][] allocation, Utility[][] r, int iterations, Market market, Mailer mailer) {
		super(allocation,r,iterations,market, "distributed");
		
		considerDecisionCounter = MainSimulator.considerDecisionCounter;

		this.parameter = mailer.getParameter();
		int distNumber= mailer.getDistribution;
		if (distNumber == 1) {
			distribution = "Uniform";
		}
		if (distNumber == 2) {
			distribution = "Possion";

		}
		
		
	}

	public FisherDataDistributed(FisherData copiedFisherData, int iterationOfCopied) {
		super(copiedFisherData, iterationOfCopied);
		if (copiedFisherData instanceof FisherDataDistributed) {
			FisherDataDistributed fdd = (FisherDataDistributed)copiedFisherData;
			considerDecisionCounter = MainSimulator.considerDecisionCounter;
			this.distribution=fdd.getDistribution();
			this.parameter=fdd.getParamter();

		}else {
			System.err.println("sent a weird copiedFisherData");
			throw new RuntimeException();
		}
	}

	public FisherDataDistributed(int idF, int numByuersF, int numGoodsF, int iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF, double avgR, double avgX, double avgRX,String distributionF, int parameterF) {
		super( idF,  numByuersF,  numGoodsF,  iterationF,  algoF,
				 considerDecisionCounterF,  maxIterationF,  avgR,  avgX,  avgRX);
		considerDecisionCounter = MainSimulator.considerDecisionCounter;
		this.distribution= distributionF;
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
		ans = ans+","+this.p3+","+this.p4+","+this.ub;
		return ans;
	}
}
