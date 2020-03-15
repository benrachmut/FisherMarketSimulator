import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainSimulator {

	// ------- VARIABLES TO CHECK BEFORE STARTING A RUN
	// -- variables of dcop problem

	
	public static boolean central = false;
	public static double stdUtil = 100;
	public static double muUtil = 100;
	public static int numberTypes = 4;

	public static int[] buyers = { 5, 10, 20, 30, 40, 50 };
	public static int[] goods = { 5, 10, 20, 30, 40, 50 };

	public static int meanRepsStart = 0;
	public static int meanRepsEnd = 100;

	public static int[] ubs = { 0, 5, 10, 25, 50, 75, 100 };
	public static double[] p3s = { 0, 1 };
	public static double[] p4s = { 0 };
	public static boolean considerDecisionCounter = true;


	public static int currRep;
	public static int currBuyersNum;
	public static int currGoodsNum;
	public static int currUb;
	public static double currP3;
	public static double currP4;

	public static Random randomUtil;
	public static Random randomGoodTypes;

	public static Random randomUb = new Random();
	public static Random randomP3 = new Random();
	public static Random randomP4 = new Random();
	
	
	

	
	
	private static List<FisherData> allData;
	private static List<FisherData> averageData;

	
	public static void main(String[] args) {

		List<List<Market>> markets = createMarkets();
		if (central) {
			runCentralistic(markets);
		}else {
			runDistributed(markets);
		}
	}

	private static void runCentralistic(List<List<Market>> markets) {
		
/*
		List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
		int maxIteration=0;
		for (Market market : markets) {
			market.restartMarketBetweenRuns(mailer);
			FisherSolver f =  new FisherSolverDistributed(market);
			List<FisherData> lonlyRunData = f.algorithm();
			maxIteration = updateMaxIteration(maxIteration, lonlyRunData.size());
			toBeAverage.add(lonlyRunData);
			allData.addAll(lonlyRunData);
		}
		toBeAverage = fixAverage(toBeAverage,maxIteration);
		List<FisherData>average = calculateAverage(toBeAverage);
		averageData.addAll(average);
		
		*/
		
		for (List<Market> list : markets) {
			List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
			int maxIteration=0;
			for (Market market : list) {
				
				FisherSolver f =  new FisherSolverCentralistic(market);
				List<FisherData> lonlyRunData = f.algorithm();
				maxIteration = updateMaxIteration(maxIteration, lonlyRunData.size());
				toBeAverage.add(lonlyRunData);
				allData.addAll(lonlyRunData);
				/*
				Utility[][] Rutil = market.getR();
				Double[][] R = turnUtilToR(Rutil);
				
				FisherSolver f = new FisherSolverCentralistic(Rutil);
				Double[][] X = f.algorithm();
				FisherData data = new centralistData(market,R,X);
				toBeAverage.add(data);
				*/
			}
			
			toBeAverage = fixAverage(toBeAverage,maxIteration);
			List<FisherData>average = calculateAverage(toBeAverage);
			averageData.addAll(average);
			//dataToBeAverage.add(toBeAverage);
		}
		
	}

	private static Double[][] turnUtilToR(Utility[][] rutil) {
		Double[][] ans = new Double[rutil.length][rutil[0].length];
		
		for (int i = 0; i < rutil.length; i++) {
			for (int j = 0; j < rutil[i].length; j++) {
				ans[i][j]=rutil[i][j].getUtility(1);
			}
		}
		return ans;
	}

	private static void runDistributed(List<List<Market>> markets) {
		List<Mailer> mailers = createMailers(markets);
		for (List<Market> marketReps: markets) {
			for (Mailer mailer : mailers) {
				runDifferentCommunicationOnMarketReps(marketReps,mailer);
			}
		}
		
	}



	private static void runDifferentCommunicationOnMarketReps
	(List<Market> marketReps, Mailer mailer) {
		
		List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
		int maxIteration=0;
		for (Market market : marketReps) {
			market.restartMarketBetweenRuns(mailer);
			FisherSolver f =  new FisherSolverDistributed(market);
			List<FisherData> lonlyRunData = f.algorithm();
			maxIteration = updateMaxIteration(maxIteration, lonlyRunData.size());
			toBeAverage.add(lonlyRunData);
			allData.addAll(lonlyRunData);
		}
		toBeAverage = fixAverage(toBeAverage,maxIteration);
		List<FisherData>average = calculateAverage(toBeAverage);
		averageData.addAll(average);
	}

	private static int updateMaxIteration(int maxIteration, int currentMaxIter) {
		if (maxIteration<currentMaxIter) {
			return currentMaxIter;
		}else {
			return maxIteration;
		}
	}

	private static List<Mailer> createMailers(List<List<Market>> markets) {
		List<Mailer> ans = new ArrayList<Mailer>();
		for (double selectedP3 : p3s) {
			currP3 = selectedP3;
			for (int selectedUB : ubs) {
				currUb = selectedUB;
				for (double selectedP4 : p4s) {
					currP4 = selectedP4;
					Mailer mailer = new Mailer(selectedP3, selectedP4, selectedUB);
					ans.add(mailer);
				}
			}
		}
		return ans;
	}

	private static List<List<Market>> createMarkets() {
		List<List<Market>> ans = new ArrayList<List<Market>>();
		for (Integer b : buyers) {
			currBuyersNum = b;
			for (Integer s : goods) {
				currGoodsNum = s;
				List<Market> repsMarkets = new ArrayList<Market>();
				for (int i = meanRepsStart; i < meanRepsEnd; i++) {
					currRep = i;
					restartRandomUtilAndType(s, b, i);
					Market market = new Market(b, s, randomUtil, randomGoodTypes, i);
					repsMarkets.add(market);
				}
				ans.add(repsMarkets);
			}
		}
		return ans;
	}

	private static void restartRandomUtilAndType(Integer s, Integer b, int i) {
		randomUtil = new Random((b * 10) + (s * 100) + (i * 1000));
		randomGoodTypes = new Random((b * 10) + (s * 100) + (i * 1000));

	}

	public static double getRandomNorm(double type, Random r) {
		double ans = stdUtil * r.nextGaussian() + muUtil * type;
		do {
			ans = stdUtil * r.nextGaussian() + muUtil * type;
		} while (ans < 0);

		return ans;
	}

}
