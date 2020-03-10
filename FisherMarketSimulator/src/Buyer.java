import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class Buyer {

	private int id;
	private SortedMap <Good,Utility> m;
	private Random randomUtil;
	
	
	public Buyer(int i, List<Good> goods, Random r) {
		this.id=i;
		this.randomUtil = r;
		
		this.m = createUtils(goods);
	}


	private SortedMap<Good, Utility> createUtils(List<Good> goods) {
		
		SortedMap<Good, Utility> ans = new TreeMap<Good, Utility>();
		for (int j = 0; j < goods.size(); j++) {
			Good g = goods.get(j);
			
			int goodId = g.getId();
			int goodType = g.getType();
			double value =MainSimulator.getRandomNorm(goodType, randomUtil);
			LinearUtility util = new ConcaveUtility(value,1);
			
			
			
		}
		
	}

}
