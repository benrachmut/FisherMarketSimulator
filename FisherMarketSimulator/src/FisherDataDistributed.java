
public class FisherDataDistributed extends FisherData {

	private double p3;
	private double p4;
	private int ub;

	public FisherDataDistributed(Double[][] allocation, Utility[][] r, int iterations, Market market, Mailer mailer) {
		super(allocation,r,iterations,market, "distributed");
		this.p3 = mailer.getP3();
		this.p4 =mailer.getP4();
		this.ub =mailer.getUB();
		
	}

	public FisherDataDistributed(FisherData copiedFisherData) {
		super(copiedFisherData);
		if (copiedFisherData instanceof FisherDataDistributed) {
			FisherDataDistributed fdd = (FisherDataDistributed)copiedFisherData;
			this.p3=fdd.getP3();
			this.p4=fdd.getP4();
			this.ub=fdd.getUb();

		}else {
			System.err.println("sent a weird copiedFisherData");
			throw new RuntimeException();
		}
	}

	public FisherDataDistributed(int idF, int numByuersF, int numGoodsF, int iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF, double avgR, double avgX, double avgRX, double p3f,
			double p4f, int ubF) {
		super( idF,  numByuersF,  numGoodsF,  iterationF,  algoF,
				 considerDecisionCounterF,  maxIterationF,  avgR,  avgX,  avgRX);
		this.p3 = p3f;
		this.p4 = p4f;
		this.ub = ubF;
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public String toString() {
		String ans = super.toString();
		ans = ans+","+this.p3+","+this.p4+","+this.ub;
		return ans;
	}
}
