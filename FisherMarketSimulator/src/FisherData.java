
public class FisherData {

	protected int id;
	protected int numByuers;
	protected int numGoods;
	protected double iteration;
	
	
	protected String algo;
	protected boolean considerDecisionCounter;
	protected int maxIteration;

	

	
	public FisherData(Double[][] X, Utility[][] rUtil, double iterations, Market market, String algo) {

		this.id = market.getId();
		this.numByuers = market.getBuyers().size();
		this.numGoods = market.getGoods().size();
		this.iteration = iterations;
		Double[][] R = turnUtilToDouble(rUtil);
		this.algo = algo;
		maxIteration = MainSimulator.maxIteration;
		//this.sumRX = createRX(R, X);
		//this.envyFree = checkEnvyFree(X, rUtil);
	}



	

	

/*
	@Override
	public String toString() {
		String ans = this.id + "," + this.numByuers + "," + this.numGoods + "," + this.iteration + "," + this.algo + ","
				+ this.maxIteration + "," +
				// this.sumR+","+
				// this.sumX+","+
				this.sumRX + "," + this.envyFree;
		return ans;
	}
*/
	
	public FisherData(FisherData copiedFisherData, double iterationOfCopied) {

		this.id = copiedFisherData.getId();
		this.numByuers = copiedFisherData.getNumByuers();
		this.numGoods = copiedFisherData.getNumGoods();
		this.iteration = iterationOfCopied;

		// this.sumR=copiedFisherData.getSumR();
		// this.sumX=copiedFisherData.getSumX();
		this.sumRX = copiedFisherData.getSumRX();
		this.algo = copiedFisherData.getAlgo();
		this.considerDecisionCounter = copiedFisherData.getConsiderDecisionCounter();
		this.maxIteration = copiedFisherData.getMaxIteration();
		this.envyFree = copiedFisherData.getEnvyFree();
	}

	public FisherData(int idF, int numByuersF, int numGoodsF, double iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF, double avgRX, double envyFreeF) {
		this.id = idF;
		this.numByuers = numByuersF;
		this.numGoods = numGoodsF;
		this.iteration = iterationF;
		// this.sumR=avgR;
		// this.sumX=avgX;
		this.sumRX = avgRX;
		this.algo = algoF;
		this.considerDecisionCounter = considerDecisionCounterF;
		this.maxIteration = maxIterationF;
		this.envyFree = envyFreeF;
	}

	public int getMaxIteration() {
		// TODO Auto-generated method stub
		return this.maxIteration;
	}

	public boolean getConsiderDecisionCounter() {
		// TODO Auto-generated method stub
		return this.considerDecisionCounter;
	}

	public String getAlgo() {
		// TODO Auto-generated method stub
		return new String(this.algo);
	}

	
	/*
	 * private Double sumMatrix(Double[][] input) { Double ans = 0.0; for (int i =
	 * 0; i < input.length; i++) { for (int j = 0; j < input[i].length; j++) { if
	 * (input[i][j]!=null) { ans+=input[i][j]; } } } return ans; }
	 */

	private Double[][] turnUtilToDouble(Utility[][] rUtil) {
		Double[][] ans = new Double[rUtil.length][rUtil[0].length];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				ans[i][j] = rUtil[i][j].getUtility(1);
			}
		}
		return ans;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumByuers() {
		return numByuers;
	}

	public void setNumByuers(int numByuers) {
		this.numByuers = numByuers;
	}

	public int getNumGoods() {
		return numGoods;
	}

	public void setNumGoods(int numGoods) {
		this.numGoods = numGoods;
	}

	public double getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	/*
	 * public Double getSumR() { return sumR; }
	 * 
	 * public void setSumR(Double sumR) { this.sumR = sumR; }
	 * 
	 * public Double getSumX() { return sumX; }
	 * 
	 * public void setSumX(Double sumX) { this.sumX = sumX; }
	 */

	public Double getSumRX() {
		return sumRX;
	}

	public void setSumRX(Double sumRX) {
		this.sumRX = sumRX;
	}

	public void increaseIterByOne() {
		this.iteration = this.iteration + 1;

	}

	public Double getEnvyFree() {
		// TODO Auto-generated method stub
		return this.envyFree;
	}

	public static String header() {
		return "Id," + "Byuers," + "Goods," + "Iteration," + "Algorithm," + "Max Iteration," + "Sum RX," + "Envy Free";
	}

}
