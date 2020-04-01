import java.util.ArrayList;
import java.util.List;

public abstract class FisherSolver {

	protected Market market;
	protected int iterations;
	protected double change;
	protected Utility[][] R;
	private Double[][] utilsAsNumber;

	protected List<FisherData> data;
	protected int parameter;

	public FisherSolver(Market m) {
		this.market = m;
		this.parameter = m.getCurrentParameter();
		this.R = m.getR();
		this.utilsAsNumber = createUtilsAsNumbers();
		this.iterations = 0;
		this.change = Double.MAX_VALUE;
		this.data = new ArrayList<FisherData>();
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

	public List<FisherData> algorithm() {
/*
		if (MainSimulator.central) {
			System.out.println("central status after initialization");
			System.out.println("___________________________________");
			printStatues();
		}
*/
		data.add(iterate());
/*
		if (!MainSimulator.central) {
			System.out.println("distributed status after initialization");
			System.out.println("_______________________________________");
			printStatues();
		} else {
			printStatues();
		}
*/
		while (!isStable()) {
			
			this.iterations = this.iterations + 1;
			data.add(iterate());
			printStatues();
		}	
		return this.data;
	}

	private void printStatues() {
		if (MainSimulator.printForDebug) {

			
			if (MainSimulator.central == true) {
				printStatuesSeq();
				
			} else {
				if (this.iterations % 2 == 0) {
					printStatuesSeq();

				}
			}
		}
	}

	private void printStatuesSeq() {
		System.out.println("______iterations = " + this.iterations + "______");
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

	private static void print2DArray(double[][] input) {
		System.out.println();

		for (int i = 0; i < input.length; i++) {
			System.out.print("|");
			for (int j = 0; j < input[i].length; j++) {
				System.out.print("[" + input[i][j] + "]");
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
		System.out.println(FisherData.createRX(utilsAsNumber, turnToDouble(allocation)));
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

	public abstract FisherData iterate();

	public boolean isStable() {
		boolean isStable = change < MainSimulator.THRESHOLD;
		boolean isComplete = this.iterations == MainSimulator.maxIteration;
		return isComplete;
	}

}
