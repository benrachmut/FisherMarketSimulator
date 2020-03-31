
public class FisherSolverCentralistic extends FisherSolver {

	protected Utility[][] valuations;// utilities of buyers over the goods
	protected Double[][] currentAllocation;// current allocation of the goods
	protected double[][] bids;// buyers bids over the goods
	protected double[] prices;// prices of the goods in the market

	protected int nofAgents;
	protected int nofGoods;
	protected double[][] utilites;
	public FisherSolverCentralistic(Market market) {
		super(market);
		this.nofAgents = R.length;
		this.nofGoods = R[0].length;

		initializeFields(this.R);
		double[] valuationSums = initializeValuations(this.R);
		initializeBids(this.R,valuationSums);
		updatePriceVectorUsingBids();
		updateCurrentAllocationMatrixAndChanges();
		
	}

	// next iteration: calculates prices, calculates current valuation and
	// update the bids
	public FisherData iterate() {
		updateBidsUsingUtilites();
		updatePriceVectorUsingBids();
		updateCurrentAllocationMatrixAndChanges();
		FisherData ans = new FisherDataCentralistic(currentAllocation, this.R, iterations, market);

		// generateAllocations();
		return ans;
	}

	// -----------METHODS OF iterate------

	private void updateBidsUsingUtilites() {
		double[][] utilitiesHere = new double[nofAgents][nofGoods];
		// calculate current utilities and sum the utility for each agent
		final double[] utilitySum = new double[nofAgents];
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				if (currentAllocation[i][j] != null) {
					utilitiesHere[i][j] = valuations[i][j].getUtility(currentAllocation[i][j]);
					utilitySum[i] += valuations[i][j].getUtility(currentAllocation[i][j]);
				}
			}
		}
		
		this.utilites = utilitiesHere;
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				bids[i][j] = utilitiesHere[i][j] / utilitySum[i];
			}
		}

	}

	private void updateCurrentAllocationMatrixAndChanges() {
		change = 0;
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				updateSumOfChanges(i, j);
				updateCurrentAllocation(i, j);

			}
		}
	}

	private void updatePriceVectorUsingBids() {
		for (int j = 0; j < nofGoods; j++) {
			prices[j] = 0;
			for (int i = 0; i < nofAgents; i++) {
				prices[j] += bids[i][j];
			}
		}
	}

	// -----------METHODS OF updateCurrentAllocationMatrixAndChanges------

	private void updateCurrentAllocation(int i, int j) {
		if (bids[i][j] / prices[j] > MainSimulator.THRESHOLD) {
			currentAllocation[i][j] = bids[i][j] / prices[j];// aaaaa
		} else {
			currentAllocation[i][j] = null;
		}

	}

	private void updateSumOfChanges(int i, int j) {
		if (currentAllocation[i][j] != null) {
			change += Math.abs(((bids[i][j] / prices[j]) - currentAllocation[i][j]));/// aaaa
		}
	}

	// -----------Getters and Object methods------

	public double getChange() {
		return change;
	}

	public Double[][] getAllocations() {

		return currentAllocation;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("FisherPrd:").append("\n");
		sb.append("valuations:").append("\n");
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				sb.append(valuations[i][j] + "\t");
			}
			sb.append("\n");
		}
		sb.append("bids:").append("\n");
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				sb.append(Math.round(bids[i][j] * 1000.0) / 1000.0 + "\t");
			}
			sb.append("\n");
		}
		sb.append("allocations:").append("\n");
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				sb.append(Math.round(currentAllocation[i][j] * 1000.0) / 1000.0 + "\t");
				// sb.append(currentAllocation[i][j] + "\t");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	// -----------METHODS OF CONSTRUCTOR------

	private void initializeBids(Utility[][] utilities, double[] valuationSums) {


		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				if (utilities[i][j] != null) {
					bids[i][j] = utilities[i][j].getUtility(1)/valuationSums[i];
				}
			}
		}
		
		/*
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				if (utilities[i][j] != null) {
					bids[i][j] = 1 * (i + 1);
				}
			}
		}
		*/

	}

	private double[] initializeValuations(Utility[][] utilities) {
		final double[] valuationSums = new double[nofAgents]; // utilits sum of rows,
		for (int i = 0; i < nofAgents; i++) {
			for (int j = 0; j < nofGoods; j++) {
				if (utilities[i][j] != null) {
					this.valuations[i][j] = (Utility) utilities[i][j].clone();
					valuationSums[i] += utilities[i][j].getUtility(1);
				}
			}
		}
		return valuationSums;

	}

	private void initializeFields(Utility[][] utilities) {
		this.nofGoods = utilities[0].length; // number columns = number of goods
		this.nofAgents = utilities.length; // number of rows = number of agents
		this.currentAllocation = new Double[nofAgents][nofGoods];// X
		this.bids = new double[nofAgents][nofGoods]; // the prices each agents offers per goods
		this.valuations = new Utility[nofAgents][nofGoods]; // ?
		this.prices = new double[nofGoods]; // price vector
	}

	@Override
	protected double[][] getCurrentUtil() {
		double[][] ans = new double[valuations.length][valuations[0].length];

		for (int i = 0; i < valuations.length; i++) {
			for (int j = 0; j < valuations[i].length; j++) {
				if (currentAllocation[i][j] == null) {
					ans[i][j] = 0;

				} else {
					if (this.utilites==null) {
						ans[i][j] = valuations[i][j].getUtility(1);

					}else {
					ans[i][j] = this.utilites[i][j];
					}
				}
			}
		}
		return ans;
	}

	@Override
	protected double[][] getBids() {
		// TODO Auto-generated method stub
		return this.bids;
	}

	@Override
	protected double[] getPricers() {
		return this.prices;
	}

	@Override
	protected double[][] getAllocation() {
		double[][] ans = new double[currentAllocation.length][currentAllocation[0].length];

		for (int i = 0; i < currentAllocation.length; i++) {
			for (int j = 0; j < currentAllocation[i].length; j++) {
				if (currentAllocation[i][j]==null) {
					ans[i][j] =0;
				}else {
				ans[i][j] = currentAllocation[i][j];
			}}
		}
		return ans;
	}

	// -----------METHODS OF GENERATE ALLOCATIONS------

	// generates allocation according to current bids and prices
	/*
	 * private Double[][] generateAllocations() { updatePriceVectorUsingBids();
	 * updateCurrentAllocationMatrixAndChanges(); return currentAllocation; }
	 */

}
