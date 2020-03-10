import java.util.Random;

public class MainSimulator {

	// ------- VARIABLES TO CHECK BEFORE STARTING A RUN
		// -- variables of dcop problem
		
	
		public static double stdUtil = 100;
		public static double muUtil = 100;
		public static int numberTypes = 4;

		public static int[] buyers = {5,10,20,30,40,50};
		public static int [] goods = {5,10,20,30,40,50}; 
		
	
		
		public static int meanRepsStart = 0;
		public static int meanRepsEnd = 100;
		
		public static int [] ubs = {0,5,10,25,50,75,100};
		public static double [] p3s = {0,1};
		public static double [] p4s = {0};
		public static String algo="";

		
		public static int currRep;
		public static int currBuyersNum;
		public static int  currGoodsNum;
		public static int currUb;
		public static double currP3;
		public static double currP4;
		
		
		public static Random randomUtil;
		public static Random randomGoodTypes;

		public static Random randomUb = new Random();
		public static Random randomP3 = new Random();
		public static Random randomP4 = new Random();

		
		
		
		
	public static void main(String[] args) {
		
	
		for (Integer b : buyers) {
			currBuyersNum = b;
			for (Integer s : goods) {
				currGoodsNum = s;
				for (int i = meanRepsStart; i < meanRepsEnd; i++) {
					currRep = i;
					randomUtil = new Random((b*10)+(s*100)+(i*1000));	
					randomGoodTypes = new Random((b*10)+(s*100)+(i*1000));
					Market market = new Market(currBuyersNum,currGoodsNum, randomUtil,randomGoodTypes);
				}
			}
		}
		
	}







	public static double getRandomNorm(double type, Random r) {
		double ans = stdUtil*r.nextGaussian()+muUtil*type;
		do {
			ans = stdUtil*r.nextGaussian()+muUtil*type;
		} while (ans<0);
		
		return ans;
	}

}
