import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MainSimulator {

	// ------- VARIABLES TO CHECK BEFORE STARTING A RUN
	// -- variables of dcop problem

	public static boolean central = false;
	protected static final double THRESHOLD = 1E-4;

	public static int maxIteration = 1000;

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
		} else {
			runDistributed(markets);
		}
		createExcel();
	}

	private static void createExcel() {
		createFile(averageData, true);
		createFile(allData, false);
	}

	private static void createFile(List<FisherData> data, boolean average) {
		try {
			String fileName = createFileName(average);
			FileWriter s = new FileWriter(fileName + ".csv");
			BufferedWriter out = new BufferedWriter(s);
			String h = createHeader();
			out.write(h);
			out.newLine();

			for (FisherData fd : data) {
				out.write(fd.toString());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			System.err.println("Couldn't write to file");
		}

	}

	private static String createHeader() {
		String ans = "id," + "numByuers," + "numGoods," + "iteration," + "algo," + "considerDecisionCounter,"
				+ "maxIteration," + "sumR," + "sumX," + "sumRX";

		if (!central) {
			ans = ans + "," + "p3" + "," + "p4" + "," + "ub";
		}

		return ans;
	}

	private static String createFileName(boolean average) {
		String central1 = "central_" + central + ",";
		String maxIteration1 = "maxIteration_" + maxIteration + ",";

		String stdUtil1 = "stdUtil_" + stdUtil + ",";
		String muUtil1 = "muUtil_" + muUtil + ",";
		String numberTypes1 = "numberTypes_" + numberTypes + ",";

		String meanRepsStart1 = "meanRepsStart_" + meanRepsStart + ",";
		String meanRepsEnd1 = "meanRepsEnd_" + meanRepsEnd + ",";

		String considerDecisionCounter1 = "considerDecisionCounter_" + considerDecisionCounter;

		return central1 + stdUtil1 + muUtil1 + numberTypes1 + meanRepsStart1 + meanRepsEnd1 + considerDecisionCounter1;
	}

	private static void runCentralistic(List<List<Market>> markets) {

		for (List<Market> list : markets) {
			List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
			int max = 0;
			for (Market market : list) {

				FisherSolver f = new FisherSolverCentralistic(market);
				List<FisherData> lonlyRunData = f.algorithm();
				max = updateMaxIteration(maxIteration, lonlyRunData.size());
				toBeAverage.add(lonlyRunData);
				allData.addAll(lonlyRunData);
			}

			toBeAverage = fixAverage(toBeAverage, max - 1);
			List<FisherData> average = calculateAverage(toBeAverage, max - 1);
			averageData.addAll(average);
			// dataToBeAverage.add(toBeAverage);
		}

	}

	private static List<FisherData> calculateAverage(List<List<FisherData>> toBeAverage, int max) {
		List<FisherData> ans = new ArrayList<FisherData>();
		checkIfToBeAverageIsValid(toBeAverage, max);
		int counter = 0;
		FisherData fd;
		while (counter != max) {
			List<FisherData> sameIterList = new ArrayList<FisherData>();

			for (List<FisherData> list : toBeAverage) {
				sameIterList.add(list.get(counter));
			}

			FisherData t = sameIterList.get(0);
			int idF = -1;
			int numByuersF = t.getNumByuers();
			int numGoodsF = t.getNumGoods();
			int iterationF = t.getIteration();
			String algoF = t.getAlgo();
			boolean considerDecisionCounterF = t.getConsiderDecisionCounter();
			int maxIterationF = t.getMaxIteration();

			fd = getFisherDataAverage(sameIterList, idF, numByuersF, numGoodsF, iterationF, algoF,
					considerDecisionCounterF, maxIterationF);
			counter++;
		}

		return null;
	}

	private static FisherData getFisherDataAverage(List<FisherData> sameIterList, int idF, int numByuersF,
			int numGoodsF, int iterationF, String algoF, boolean considerDecisionCounterF, int maxIterationF) {

		double sumRX = 0.0;
		double sumR = 0.0;
		double sumX = 0.0;

		double n = sameIterList.size();
		for (FisherData fisherData : sameIterList) {
			sumRX += fisherData.getSumRX();
			sumX += fisherData.getSumX();
			sumR += fisherData.getSumR();
		}

		double avgR = sumR / sameIterList.size();
		double avgX = sumX / sameIterList.size();
		double avgRX = sumRX / sameIterList.size();

		if (central) {
			return new FisherDataCentralistic(idF, numByuersF, numGoodsF, iterationF, algoF, considerDecisionCounterF,
					maxIterationF, avgR, avgX, avgRX);
		} else {

			FisherDataDistributed t = (FisherDataDistributed) sameIterList.get(0);
			double p3F = t.getP3();
			double p4F = t.getP4();
			int ubF = t.getUb();
			return new FisherDataDistributed(idF, numByuersF, numGoodsF, iterationF, algoF, considerDecisionCounterF,
					maxIterationF, avgR, avgX, avgRX, p3F, p4F, ubF);
		}

	}

	private static void checkIfToBeAverageIsValid(List<List<FisherData>> toBeAverage, int max) {
		for (List<FisherData> list : toBeAverage) {
			int sizeOfList = list.size();
			if (sizeOfList != max) {
				System.err.println("size of a list is " + sizeOfList + " and max is " + max);
				throw new RuntimeException();
			}
		}

	}

	private static List<List<FisherData>> fixAverage(List<List<FisherData>> toBeAverage, int max) {
		for (List<FisherData> list : toBeAverage) {
			if (list.size() < max) {
				FisherData lastFisherDate = list.get(list.size() - 1);
				FisherData copiedFisherData;
				while (list.size() < max) {
					if (central) {
						copiedFisherData = new FisherDataCentralistic(lastFisherDate);
					} else {
						copiedFisherData = new FisherDataDistributed(lastFisherDate);

					}

					list.add(copiedFisherData);
				}
			}
		}
		return toBeAverage;
	}

	private static Double[][] turnUtilToR(Utility[][] rutil) {
		Double[][] ans = new Double[rutil.length][rutil[0].length];

		for (int i = 0; i < rutil.length; i++) {
			for (int j = 0; j < rutil[i].length; j++) {
				ans[i][j] = rutil[i][j].getUtility(1);
			}
		}
		return ans;
	}

	private static void runDistributed(List<List<Market>> markets) {
		List<Mailer> mailers = createMailers(markets);
		for (List<Market> marketReps : markets) {
			for (Mailer mailer : mailers) {
				runDifferentCommunicationOnMarketReps(marketReps, mailer);
			}
		}

	}

	private static void runDifferentCommunicationOnMarketReps(List<Market> marketReps, Mailer mailer) {

		List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
		int max = 0;
		for (Market market : marketReps) {
			market.restartMarketBetweenRuns(mailer);
			FisherSolver f = new FisherSolverDistributed(market);
			List<FisherData> lonlyRunData = f.algorithm();
			max = updateMaxIteration(max, lonlyRunData.size());
			toBeAverage.add(lonlyRunData);
			allData.addAll(lonlyRunData);
		}
		toBeAverage = fixAverage(toBeAverage, max);
		List<FisherData> average = calculateAverage(toBeAverage, max);
		averageData.addAll(average);
	}

	private static int updateMaxIteration(int maxIteration, int currentMaxIter) {
		if (maxIteration < currentMaxIter) {
			return currentMaxIter;
		} else {
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
