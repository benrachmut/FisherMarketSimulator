import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Market {

	private List<Buyer> buyers;
	private List<Good> goods;
	private Random rUtil;
	private Random rGoodTypes;
	
	public Market(int buyersNum, int goodsNum, Random rUtil, Random rGoodTypes) {
		this.rUtil = rUtil;
		this.rGoodTypes = rGoodTypes;
		this.goods = createGoods(goodsNum);
		this.buyers=createBuyers(buyersNum);
	}
	

	private List<Buyer> createBuyers(int buyersNum) {
		List<Buyer> ans = new ArrayList<Buyer>();
		
		for (int i = 0; i < buyersNum; i++) {
			Buyer b = new Buyer (i,goods, rUtil);
		}
		
	}


	private List<Good> createGoods(int goodsNum) {
		List<Good> ans = new ArrayList<Good>();
		
		for (int i = 0; i < goodsNum; i++) {
			Good g = new Good (getType(),i);
		}
		
		return ans;
	}

	private int getType() {
		int type=0;
		do {
			type = rGoodTypes.nextInt(MainSimulator.numberTypes);
		} while (type == 0);
		
		return type;
	}

}
