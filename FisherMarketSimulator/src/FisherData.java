
public class FisherData {
	
	protected int id;
	protected int numByuers;
	protected int numGoods;
	protected int iteration;
	
	protected Double sumR;
	protected Double sumX;
	protected Double sumRX;
	protected String algo;
	protected boolean considerDecisionCounter;
	protected int maxIteration;
	

	public FisherData(Double[][] X, Utility[][] rUtil, int iterations, Market market,String algo) {
		
		this.id = market.getId();
		this.numByuers = market.getBuyers().size();
		this.numGoods = market.getGoods().size();
		this.iteration =iterations;
		Double[][] R = turnUtilToDouble(rUtil);
		this.algo = algo;
		maxIteration=MainSimulator.maxIteration;
		this.sumR = sumMatrix(R);
		this.sumX = sumMatrix(X);
		this.sumRX = createRX(R,X);
		
	}
	@Override
	public String toString() {
		String ans= 
		this.id+","+
		this.numByuers+","+
		this.numGoods+","+
		this.iteration+","+
		this.algo+","+
		this.maxIteration+","+
		this.sumR+","+
		this.sumX+","+
		this.sumRX;
		return ans;
	}

	public FisherData(FisherData copiedFisherData, int iterationOfCopied) {
		
		
		
		this.id=copiedFisherData.getId();
		this.numByuers=copiedFisherData.getNumByuers();
		this.numGoods=copiedFisherData.getNumGoods();
		this.iteration=iterationOfCopied;
		
		this.sumR=copiedFisherData.getSumR();
		this.sumX=copiedFisherData.getSumX();
		this.sumRX=copiedFisherData.getSumRX();
		this.algo=copiedFisherData.getAlgo();
		this.considerDecisionCounter=copiedFisherData.getConsiderDecisionCounter();
		this.maxIteration=copiedFisherData.getMaxIteration();
	}

	public FisherData(int idF, int numByuersF, int numGoodsF, int iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF, double avgR, double avgX, double avgRX) {
		this.id=idF;
		this.numByuers=numByuersF;
		this.numGoods=numGoodsF;
		this.iteration=iterationF;
		this.sumR=avgR;
		this.sumX=avgX;
		this.sumRX=avgRX;
		this.algo=algoF;
		this.considerDecisionCounter=considerDecisionCounterF;
		this.maxIteration=maxIterationF;
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

	private Double createRX(Double[][] r, Double[][] x) {
		Double ans = 0.0;
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[i].length; j++) {
				if (x[i][j]!=null && r[i][j]!=null) {
					ans =ans+ r[i][j]*x[i][j];
				}
			}
		}
		return ans;
	}

	private Double sumMatrix(Double[][] input) {
		Double ans = 0.0;
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++) {
				if (input[i][j]!=null) {
					ans+=input[i][j];
				}
			}
		}
		return ans;
	}

	private Double[][] turnUtilToDouble(Utility[][] rUtil) {
		Double[][] ans = new Double[rUtil.length][rUtil[0].length];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				ans[i][j]=rUtil[i][j].getUtility(1);
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

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public Double getSumR() {
		return sumR;
	}

	public void setSumR(Double sumR) {
		this.sumR = sumR;
	}

	public Double getSumX() {
		return sumX;
	}

	public void setSumX(Double sumX) {
		this.sumX = sumX;
	}

	public Double getSumRX() {
		return sumRX;
	}

	public void setSumRX(Double sumRX) {
		this.sumRX = sumRX;
	}
	public void increaseIterByOne() {
		this.iteration = this.iteration+1;
		
	}

	
	
}
