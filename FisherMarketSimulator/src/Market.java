import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Market {

	private int id;
	private List<Buyer> buyers;
	private List<Good> goods;
	private Random rUtil;
	private Random rGoodTypes;
	private Utility[][] R;
	private Mailer mailer;
	private SortedMap<Integer, double[][]> parameterDelayMartix;
	private int currentParameter;

	public Market(int buyersNum, int goodsNum, Random rUtil, Random rGoodTypes, int i) {
		this.id = i;
		this.rUtil = rUtil;
		this.rGoodTypes = rGoodTypes;
		this.goods = createGoods(goodsNum);
		this.buyers = createBuyers(buyersNum);
		this.R = createR();
		this.parameterDelayMartix = new TreeMap<Integer, double[][]>();
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
		String ans = "Market number: " + id + ", " + "buyers: " + buyers.size() + ", goods: " + goods.size()+ ", parameter: " + this.currentParameter;

		return ans;
	}

	private List<Buyer> createBuyers(int buyersNum) {
		List<Buyer> ans = new ArrayList<Buyer>();

		for (int i = 0; i < buyersNum; i++) {
			Buyer b = new Buyer(i, goods, rUtil);
			ans.add(b);
		}
		return ans;
	}

	private List<Good> createGoods(int goodsNum) {
		List<Good> ans = new ArrayList<Good>();

		for (int i = 0; i < goodsNum; i++) {
			Good g = new Good(getType(), i);
			ans.add(g);
		}

		return ans;
	}

	private int getType() {
		int type = 0;
		do {
			type = rGoodTypes.nextInt(MainSimulator.numberTypes);
		} while (type == 0);

		return type;
	}

	public void restartMarketBetweenRuns(Mailer mailer, int parameter) {
		
		//updateMailer(mailer, parameter);
		this.currentParameter = parameter;
		this.mailer = mailer;

		for (Good g : this.goods) {
			g.updateMailer(this.mailer);
			g.resetGoodsBetweenRuns();
		}

		for (Buyer b : this.buyers) {
			b.updateMailer(this.mailer);
			b.resetBuyerBetweenRuns();
		}

	}

	private void updateMailer(Mailer mailer, int parameter) {
		this.currentParameter = parameter;
		this.mailer = mailer;
		double[][] parameterMatrix = this.parameterDelayMartix.get(parameter);
		this.mailer.setParameterMatrix(parameterMatrix);
		this.mailer.emptyMessageBox();
		this.mailer.updateSeeds(this.id,parameter);
		this.mailer.updateParameter(parameter);

		
		
	}

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

	public Mailer getMailer() {
		// TODO Auto-generated method stub
		return this.mailer;
	}

	public void createParameterMatrix(int parameter, RandomNumberGenerator rng) {

		double[][] value = new double[R.length][R[0].length];

		if (parameter == 0) {
			value = setParametersToZero();
			
		} else {
			Random r = new Random(id);
			for (int i = 0; i < R.length; i++) {
				for (int j = 0; j < R[i].length; j++) {
					value[i][j] = rng.getRandom(r, parameter);
				}
			}
		}

		this.parameterDelayMartix.put(parameter, value);

	}

	private double[][] setParametersToZero() {
		double[][] ans = new double[R.length][R[0].length];
		for (int i = 0; i < R.length; i++) {
			for (int j = 0; j < R[i].length; j++) {
				ans[i][j] = 0;
			}
		}
		return ans;
	}

	public int getCurrentParameter() {
		return currentParameter;
	}

	public double[][] getParametersMatrix(int parameter) {
		this.currentParameter = parameter;
		return this.parameterDelayMartix.get(parameter);
	}

}
