package Market;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import Utility.Utility;

public class Market {

	private int id;
	private List<Buyer> buyers;
	private List<Good> goods;
	private Random rUtil;
	private Utility[][] R;

	private Mailer mailer;

	private int[][] imperfectCommunicationMatrix;


	public Market(int buyersNum, int goodsNum, int i) {
		this.id = i;
		this.goods = createGoods(goodsNum);
		this.buyers = createBuyers(buyersNum);

		conncetGoodToBuyer();
		createImperfectCommunicationMatrix();
		this.R = createR();
	}


	
	private void createImperfectCommunicationMatrix() {
		this.imperfectCommunicationMatrix = new int[this.buyers.size()][this.goods.size()];
		for (int i = 0; i < imperfectCommunicationMatrix.length; i++) {
			Buyer b = buyers.get(i);
			List<Integer> goodsResIndexes= b.getGoodsResponsibilityIndexes();
			for (int j = 0; j < imperfectCommunicationMatrix[i].length; j++) {
				if (goodsResIndexes.contains(j)) {
					imperfectCommunicationMatrix[i][j] = 0;
				}else {
					imperfectCommunicationMatrix[i][j]=1;
				}
			}
		}

	}

	private void conncetGoodToBuyer() {
		int counter = 0;
		for (Good good : goods) {
			boolean flag = false;
			while (flag == false) {
				for (Buyer buyer : buyers) {
					if (buyer.getGoodsResponsibilitySize() == counter) {
						buyer.addToGoodsResponsibility(good);
						good.setAssignedBuyer(buyer);
						flag = true;
						break;
					}
				}

				if (flag == false) {
					counter++;
				}
			}
		}
	}

	public int getId() {
		return this.id;
	}

	private Utility[][] createR() {
		Utility[][] ans = new Utility[buyers.size()][goods.size()];
		for (int i = 0; i < buyers.size(); i++) {
			Buyer b = buyers.get(i);
			for (int j = 0; j < goods.size(); j++) {
				Utility u = b.getUtility(j);
				ans[i][j] = u;
			}
		}
		return ans;
	}

	@Override
	public String toString() {
		
		return id +","+buyers.size() + "," + goods.size();

		
	}

	public static String header() {
		return "Trial" + "," + "Buyers" + "," + "Goods";
	}

	private List<Buyer> createBuyers(int buyersNum) {
		List<Buyer> ans = new ArrayList<Buyer>();

		for (int i = 0; i < buyersNum; i++) {
			Buyer b = new Buyer(i, goods, this.id);
			ans.add(b);
		}
		return ans;
	}

	private List<Good> createGoods(int goodsNum) {
		List<Good> ans = new ArrayList<Good>();

		for (int i = 0; i < goodsNum; i++) {
			Good g = new Good( i);
			ans.add(g);
		}

		return ans;
	}
/*
	private int getType() {
		int type = 0;
		do {
			type = rGoodTypes.nextInt(MainSimulator.numberTypes);
		} while (type == 0);

		return type;
	}
	*/
	/*
	 * private void updateMailer(Mailer mailer, int parameter) {
	 * this.currentParameter = parameter; this.mailer = mailer; double[][]
	 * parameterMatrix = this.parameterDelayMartix.get(parameter);
	 * this.mailer.setParameterMatrix(parameterMatrix);
	 * this.mailer.emptyMessageBox(); this.mailer.updateSeeds(this.id, parameter);
	 * this.mailer.updateParameter(parameter);
	 * 
	 * }
	 */

	public Utility[][] getR() {
		// TODO Auto-generated method stub
		return this.R;
	}

	public List<Buyer> getBuyers() {
		// TODO Auto-generated method stub
		return this.buyers;
	}

	public List<Good> getGoods() {
		// TODO Auto-generated method stub
		return this.goods;
	}
	
	public void meetMailer(Mailer mailer) {

		this.mailer = mailer;
		for (Good g : this.goods) {
			g.updateMailer(this.mailer);
			g.resetGoodsBetweenRuns();
		}

		for (Buyer b : this.buyers) {
			b.updateMailer(this.mailer);
			b.resetBuyerBetweenRuns();
		}
		//createBooleanMarix();

	}
	
	public int[][] getImperfectCommunicationMatrix() {
		// TODO Auto-generated method stub
		return this.imperfectCommunicationMatrix;
	}
	
	/*
	 * public Mailer getMailer() { // TODO Auto-generated method stub return
	 * this.mailer; }
	 */



	/*
	 * public void createParameterMatrix(int parameter, RandomNumberGenerator rng) {
	 * 
	 * double[][] value = new double[R.length][R[0].length];
	 * 
	 * if (parameter == 0) { value = setParametersToZero(); } else {
	 * 
	 * Random r = new Random(id);
	 * 
	 * if (MainSimulator.simplisticDelay) { cvbcvbcv
	 * 
	 * 
	 * 
	 * 
	 * } else {
	 * 
	 * for (int i = 0; i < R.length; i++) { for (int j = 0; j < R[i].length; j++) {
	 * value[i][j] = rng.getRandom(r, parameter); } } } }
	 * 
	 * this.parameterDelayMartix.put(parameter, value);
	 * 
	 * }
	 */
	/*
	 * private double[][] setParametersToZero() { double[][] ans = new
	 * double[R.length][R[0].length]; for (int i = 0; i < R.length; i++) { for (int
	 * j = 0; j < R[i].length; j++) { ans[i][j] = 0; } } return ans; }
	 */
	/*
	 * public int getCurrentParameter() { return currentParameter; }
	 */

	
/*
	private void createBooleanMarix() {

		if (this.mailer.isPerfectCommunication() == false) {
			this.dealyConstantSparsity = initDelaySparseConstantMatrix();
			this.dealyNoiseSparsity = initDelaySparseNoiseMatrix();
		} else {
			this.dealyConstantSparsity = new boolean[buyers.size()][goods.size()];
			this.dealyNoiseSparsity = new boolean[buyers.size()][goods.size()];
		}

		if (this.mailer.perfectFitNeverDown() == false) {
			this.downSparsity = initDownSparseMatrix();
		} else {
			this.downSparsity = new boolean[buyers.size()][goods.size()];
		}

	}
*/
	
	/*
	private boolean[][] initDownSparseMatrix() {
		boolean[][] ans = new boolean[buyers.size()][goods.size()];
		double p = mailer.getDownSparseProb();
		Random r = mailer.getDownSparseRand();

		boolean isCopyFromConstant = mailer.isCopyFromConstantSparse();
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				double rnd = r.nextDouble();
				if (isCopyFromConstant) {
					ans[i][j] = this.dealyConstantSparsity[i][j];
				} else {
					if (rnd < p) {
						ans[i][j] = true;
					}
				}
			}
		}
		return ans;

	}
	*/
/*
	private boolean[][] initDelaySparseNoiseMatrix() {
		boolean[][] ans = new boolean[buyers.size()][goods.size()];
		double p = this.mailer.getDealyNoiseSparsityProb();
		Random r = this.mailer.getDelayNoiseSparsityRand();

		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				double rnd = r.nextDouble();
				boolean flag = false;

				if (rnd < p) {
					ans[i][j] = true;
					flag = true;
				}
				if (this.dealyConstantSparsity[i][j] == false || flag == false) {
					ans[i][j] = false;
				}

			}
		}
		return ans;
	}
*/
	
	/*
	private boolean[][] initDelaySparseConstantMatrix() {
		boolean[][] ans = new boolean[buyers.size()][goods.size()];
		double p = this.mailer.getDealyConstantSparsityProb();
		Random r = this.mailer.getDelayConstantSparsityRand();

		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[i].length; j++) {
				double rnd = r.nextDouble();
				boolean isBuyerHostingGood = goods.get(j).getBuyerHost().equals(buyers.get(i));

				boolean flag = false;

				if (rnd < p) {
					ans[i][j] = true;
					flag = true;
				}
				if (isBuyerHostingGood || flag == false) {
					ans[i][j] = false;
				}

			}
		}
		return ans;
	}
	*/

}
