import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Market {

	private int id;
	private List<Buyer> buyers;
	private List<Good> goods;
	private Random rUtil;
	private Random rGoodTypes;
	private Utility[][] R;
	private Mailer mailer;

	public Market(int buyersNum, int goodsNum, Random rUtil, Random rGoodTypes, int i) {
		this.id = i;
		this.rUtil = rUtil;
		this.rGoodTypes = rGoodTypes;
		this.goods = createGoods(goodsNum);
		this.buyers=createBuyers(buyersNum);
		this.R = createR();
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

	public void restartMarketBetweenRuns(Mailer mailer) {
		this.mailer = mailer;
		this.mailer.updateSeeds(this.id);
		this.mailer.emptyMessageBox();
		
		for (Good g : this.goods) {
			g.updateMailer(this.mailer);
			g.resetGoodsBetweenRuns();
		}
		
		for (Buyer b : this.buyers) {
			b.updateMailer(this.mailer);
			b.resetBuyerBetweenRuns();
		}
		
	}

	
		
	

	public Utility[][] getR() {
		// TODO Auto-generated method stub
		return this.R;
	}

	public List<Buyer> getBuyers() {
		// TODO Auto-generated method stub
		return this.getBuyers();
	}

	public List<Good> getGoods() {
		// TODO Auto-generated method stub
		return this.goods;
	}

	public Mailer getMailer() {
		// TODO Auto-generated method stub
		return this.mailer;
	}

}
