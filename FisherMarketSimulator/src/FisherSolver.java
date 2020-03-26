import java.util.ArrayList;
import java.util.List;


public abstract class FisherSolver {

	protected Market market;
	protected int iterations;
	protected double change;
	protected Utility[][]R;

	
	protected List<FisherData> data;

	public FisherSolver(Market m) {
		this.market=m;
		this.R = m.getR();
		this.iterations = 0;
		this.change = Double.MAX_VALUE;
		this.data = new ArrayList<FisherData>();
	}

	public List<FisherData> algorithm() {
		data.add(iterate());
		printStatues();
		while (!isStable()) {
			this.iterations = this.iterations + 1;
			data.add(iterate());
			printStatues();
		}

		return this.data;
	}
	
	private void printStatues() {
		System.out.println("______iterations = " +this.iterations+"______" );
		System.out.println();
		if (MainSimulator.central == true) {
			printBuyerReactionToAllocation();
			printGoodReactionToBid();
		}else {
			if (this.iterations%2==0) {
				printBuyerReactionToAllocation();
			}else {
				printGoodReactionToBid();
			}
		}
		
	}


	

	private void printBuyerReactionToAllocation() {
		System.out.println("current Utility according to price:");
		double [][] tempUtil = getCurrentUtil();
		print2DArray(tempUtil);

		System.out.println("bids matrix:");
		double [][] bids = getBids();
		print2DArray(bids);
		
	}
	


	protected abstract double[][] getBids();

	private static void print2DArray(double[][] input) {
		System.out.println();

		for (int i = 0; i < input.length; i++) {
			System.out.print("|");
			for (int j = 0; j < input[i].length; j++) {
				System.out.print("["+input[i][j]+"]");
			}
			System.out.print("|");
			System.out.println();
		}			
		System.out.println();
	}

	protected abstract double[][] getCurrentUtil();

	private void printGoodReactionToBid() {
		System.out.println("prices:");
		double[]prices = getPricers();
		printPriceVector(prices);
		
		System.out.println("allocation matrix:");
		double [][]  allocation = getAllocation();
		print2DArray(allocation);
		
	}

	

	protected abstract double[][] getAllocation();

	private void printPriceVector(double[] prices) {
		System.out.println();
		for (int i = 0; i < prices.length; i++) {
			System.out.println("[g"+i+":"+prices[i]+"]");
		}
		System.out.println();
		
	}

	protected abstract double[] getPricers();

	public abstract FisherData iterate();

	public boolean isStable() {
		boolean isStable = change < MainSimulator.THRESHOLD;
		boolean isComplete = this.iterations == MainSimulator.maxIteration;
		return isStable || isComplete;
	}
	
	private static void printEF(Double[][] output, Utility[][] input3) {
		for (int k = 0; k < input3.length; k++) {
			for (int i = 0; i < output.length; i++) {
				double u = 0;
				for (int j = 0; j < output[i].length; j++) {
						if(output[i][j]!=null){
							u = u + input3[k][j].getUtility(output[i][j]);
						}
				}
				//u= Math.pow(u,1.0/pow);
				if (i == k) {
					System.out.println("my utility " + u);
				} else {
					System.out.println("others utility " + u);
				}
			}
			System.out.println("");
			System.out.println("");

		}

	}
}
