
package Fisher;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import Market.Market;
import SimulatorCreators.MainSimulator;
import Utility.Utility;

public abstract class FisherSolver {

	protected Market market;
	protected int iteration;
	protected double change;
	protected Utility[][] R;
	private Double[][] r;

	protected double sumRX;
	protected double envyFree;

	protected SortedMap<Integer, Double> cumulativeRX;
	protected SortedMap<Integer, Double> cumulativeEnvyFree;
	// protected List<String> performanceList;

	private Double[][] x;
	private int maxIteration;
	private double threshold;
	private int currentIterationForToString;

	public FisherSolver(Market m, int maxIteration, double threshold) {
		this.market = m;
		this.R = m.getR();
		this.r = createUtilsAsNumbers();
		this.iteration = 0;
		this.change = Double.MAX_VALUE;

		this.cumulativeRX = new TreeMap<Integer, Double>();
		this.cumulativeEnvyFree = new TreeMap<Integer, Double>();

		this.maxIteration = maxIteration;
		this.threshold = threshold;

	}

	public String getInfoGivenIteration(int i) {
		this.currentIterationForToString = i;
		return toString();
	}

	public Double createRX() {
		Double ans = 0.0;
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[i].length; j++) {
				if (x[i][j] != null && r[i][j] != null) {
					ans = ans + r[i][j] * x[i][j];
				}
			}
		}

		return ans;
	}

	private static void printR(Utility[][] input) {
		System.out.println("Matrix R:");

		double[][] r = new double[input.length][input[0].length];
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[i].length; j++) {
				r[i][j] = input[i][j].getUtility(1);
			}
		}
		FisherSolver.print2DArray(r);

	}

	private static void printX(Double[][] input) {
		System.out.println("Matrix X:");
		double[][] x = new double[input.length][input[0].length];

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (input[i][j] != null) {
					x[i][j] = input[i][j];
				} else {
					x[i][j] = 0;
				}
			}
		}
		print2DArray(x);
	}

	private int checkEnvyFree() {
		if (MainSimulator.envyDebug) {
			printX(x);
			printR(R);
		}

		for (int a = 0; a < R.length; a++) {

			double[] calcUtility = new double[R.length];
			for (int aWantSwitch = 0; aWantSwitch < x.length; aWantSwitch++) {
				double u = 0;
				for (int good = 0; good < x[aWantSwitch].length; good++) {
					if (x[aWantSwitch][good] != null) {
						double calc = R[a][good].getUtility(x[aWantSwitch][good]);
						u = u + calc;
					}
				} // good
				calcUtility[aWantSwitch] = u;
			} // aWantSwitch

			if (checkEnvyFreeOfAgent(calcUtility, a) == false) {
				return 0;
			}

		} // is a envy?
		return 1;
	}

	private static boolean checkEnvyFreeOfAgent(double[] util, int aIsEnvy) {

		if (MainSimulator.envyDebug) {
			System.out.println("a" + aIsEnvy + " utility view is:");
			for (int i = 0; i < util.length; i++) {
				System.out.print("[a" + i + ":" + util[i] + "]");
			}
			System.out.println();
		}

		double aUtility = util[aIsEnvy];
		for (int aOther = 0; aOther < util.length; aOther++) {
			if (aOther != aIsEnvy) {
				double aOtherUtility = util[aOther];
				if (aUtility + MainSimulator.epsilonEnvyFree < aOtherUtility) {
					return false;
				}
			}
		}
		return true;
	}

	private Double[][] createUtilsAsNumbers() {
		Double[][] ans = new Double[R.length][R[0].length];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				ans[i][j] = R[i][j].getUtility(1);
			}
		}
		return ans;
	}

	public void algorithm() {
		/*
		 * if (MainSimulator.central) {
		 * System.out.println("central status after initialization");
		 * System.out.println("___________________________________"); printStatues(); }
		 */
		x = iterate();
		updateInfo();
		/*
		 * if (!MainSimulator.central) {
		 * System.out.println("distributed status after initialization");
		 * System.out.println("_______________________________________");
		 * printStatues(); } else { printStatues(); }
		 */
		while (!isStable()) {
			this.iteration = this.iteration + 1;
			this.x = iterate();
			updateInfo();
			// printStatues();
		}
	}

	private void updateInfo() {
		this.sumRX = this.createRX();
		this.envyFree = (double) this.checkEnvyFree();
		this.cumulativeEnvyFree.put(iteration, envyFree);
		this.cumulativeRX.put(iteration, sumRX);

	}

	@Override
	public String toString() {
		String ans = this.maxIteration + "," + this.threshold + "," + currentIterationForToString;

		if (this.cumulativeRX.lastKey() < this.currentIterationForToString) {
			ans = ans + "," + this.cumulativeRX.get(this.cumulativeRX.lastKey()) + ","
					+ this.cumulativeEnvyFree.get(this.cumulativeRX.lastKey());

		} else {
			ans = ans + "," + this.cumulativeRX.get(this.currentIterationForToString) + ","
					+ this.cumulativeEnvyFree.get(this.currentIterationForToString);

		}
		return ans;
	}

	public static String header() {
		return "MaxIteration" + "," + "Threshold" + "," + "Iteration" + "," + "Sum RX" + "," + "Envy Free";
	}

	private void printStatues() {
		if (MainSimulator.printForDebug) {

			if (MainSimulator.central == true) {
				printStatuesSeq();

			} else {
				if (this.iteration % 2 == 0) {
					printStatuesSeq();

				}
			}
		}
	}

	private void printStatuesSeq() {
		System.out.println("______iterations = " + this.iteration + "______");
		System.out.println();
		printBuyerReactionToAllocation();
		printGoodReactionToBid();

	}

	private void printBuyerReactionToAllocation() {
		System.out.println("current Utility according to allocation:");
		double[][] tempUtil = getCurrentUtil();
		print2DArray(tempUtil);

		System.out.println("bids matrix:");
		double[][] bids = getBids();
		print2DArray(bids);

	}

	protected abstract double[][] getBids();

	public static void print2DArray(double[][] input) {
		System.out.println();

		for (int i = 0; i < input.length; i++) {
			System.out.print("|");
			for (int j = 0; j < input[i].length; j++) {
				System.out.print("[" + input[i][j] + "]");
				// System.out.print(input[i][j] + ",");

			}
			System.out.print("|");
			System.out.println();
		}

		System.out.println();
	}

	protected abstract double[][] getCurrentUtil();

	private void printGoodReactionToBid() {
		System.out.println("prices:");
		double[] prices = getPricers();
		printPriceVector(prices);

		System.out.println("allocation matrix:");
		double[][] allocation = getAllocation();
		print2DArray(allocation);

		System.out.println("change measure is:");
		System.out.println(this.change);
		System.out.println();

		System.out.println("XijRij:");
		System.out.println(createRX());
		System.out.println();
	}

	private Double[][] turnToDouble(double[][] input) {
		Double[][] ans = new Double[input.length][input[0].length];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[0].length; j++) {
				ans[i][j] = input[i][j];
			}
		}
		return ans;
	}

	protected abstract double[][] getAllocation();

	private void printPriceVector(double[] prices) {
		System.out.println();
		for (int i = 0; i < prices.length; i++) {
			System.out.println("[g" + i + ":" + prices[i] + "]");
		}
		System.out.println();

	}

	protected abstract double[] getPricers();

	public abstract Double[][] iterate();

	public boolean isStable() {
		
		//System.out.println(change);
		//boolean isStable = change < this.threshold;
		boolean isComplete = this.iteration == MainSimulator.maxIteration;
		return isComplete; //|| isStable ;
	}

	public Market getMarket() {
		return this.market;
	}

	public Double getRx(int i) {

		if (this.cumulativeRX.lastKey() < i) {
			return this.cumulativeRX.get(this.cumulativeRX.lastKey());
		} else {
			return this.cumulativeRX.get(i);
		}
	}

	public Double getEnvyFree(int i) {

		if (this.cumulativeEnvyFree.lastKey() < i) {
			return this.cumulativeEnvyFree.get(this.cumulativeEnvyFree.lastKey());
		} else {
			return this.cumulativeEnvyFree.get(i);
		}

	}

	public String getLastInfo() {
		this.currentIterationForToString = this.cumulativeEnvyFree.lastKey();
		return this.toString();
	}

	public Integer getLastIteration() {
		return this.cumulativeRX.lastKey();
	}

}
