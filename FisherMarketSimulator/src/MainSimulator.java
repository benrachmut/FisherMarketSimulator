import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainSimulator {

	// ------- VARIABLES TO CHECK BEFORE STARTING A RUN
	// -- variables of dcop problem

	public static boolean central = true;
	protected static final double THRESHOLD = 1E-2;
	public static int maxIteration = 1000;
	public static double stdUtil = 100;
	public static double muUtil = 100;
	public static int numberTypes = 4;
	public static int[] buyers = {2};
	public static int[] goods = { 2 };
	public static int meanRepsStart = 0;
	public static int meanRepsEnd = 2;

	public static int distributionParameterType = 1; // 1 = uniform, 2 = exp
	public static int distributionDelayType = 1;// 1 = uniform, 2 = exp
	public static int[] distributionParameters = { 0 };
	// public static double[] p4s = { 0,1 };
	public static boolean considerDecisionCounter = true;

	public static int currRep;
	public static int currBuyersNum;
	public static int currGoodsNum;
	public static int currParameter;

	public static Random randomUtil;
	public static Random randomGoodTypes;

	public static Random randomDistributionParameter = new Random();

	private static List<FisherData> allData = new ArrayList<FisherData>();
	private static List<FisherData> averageData = new ArrayList<FisherData>();

	private static List<FisherData> allDataLast = new ArrayList<FisherData>();
	private static List<FisherData> averageDataLast = new ArrayList<FisherData>();

	public static void main(String[] args) {

		List<List<Market>> markets = createMarkets();
		if (central) {
			runCentralistic(markets);
		} else {
			setParametersForMarkets(markets);
			runDistributed(markets);
		}
		createExcel();
	}

	private static void setParametersForMarkets(List<List<Market>> markets) {
		RandomNumberGenerator rng = null;
		if (distributionParameterType == 1) {
			rng = RandomNumberGenerator.Uniform;
		}

		if (distributionParameterType == 2) {
			rng = RandomNumberGenerator.Exponential;
		}

		for (List<Market> list : markets) {
			for (Market market : list) {
				for (int parameter : distributionParameters) {
					currParameter = parameter;
					market.createParameterMatrix(parameter, rng);
				}
			}
		}
	}

	private static void createExcel() {
		
		for (int i = 1; i <= 4; i++) {
			
			if (i==1) {
				createFile(averageData, i);
			}
			if (i==2) {
				createFile(allData, i);
			}
			if (i==3) {
				createFile(averageDataLast, i);
			}
			if (i==4) {
				createFile(allDataLast, i);
			}
			
		}
		
	}

	private static void createFile(List<FisherData> data, int input) {
		try {
			String fileName = createFileName(input);
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
		
		String ans = "id," + "numByuers," + "numGoods," + "iteration," + 
		"algo," + "maxIteration," + "sumRX,"+ "envyFree";
		

		if (!central) {
			ans = ans + "," + "parameter" + "," + "distributionDelay" + "," + "distributionParameter";
		}
		return ans;
	}

	private static String createFileName(int input) {

		String typeFile = null;
		if (input==1) {
			typeFile = "averageAll";
		} 
		if(input==2){
			typeFile = "fullAll";
		}
		if(input==3){
			typeFile = "averageLast";
		}
		if(input==4){
			typeFile = "fullLast";
		}
		
		typeFile = "typeFile_"+typeFile+",";
		String central1 = "central_" + central + ",";
		String meanRepsStart1 = "start_" + meanRepsStart + ",";
		String meanRepsEnd1 = "end_" + meanRepsEnd ;
		
		return typeFile + central1 + meanRepsStart1 + meanRepsEnd1;
	}

	private static void runCentralistic(List<List<Market>> markets) {

		for (List<Market> list : markets) {
			List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
			int max = 0;
			for (Market market : list) {

				FisherSolver f = new FisherSolverCentralistic(market);
				List<FisherData> lonelyRunData = f.algorithm();
				max = updateMaxIteration(max, lonelyRunData.size());
				toBeAverage.add(lonelyRunData);
				allData.addAll(lonelyRunData);
				allDataLast.add(lonelyRunData.get(lonelyRunData.size() - 1));
				System.out.println(market);
			}

			List<FisherData> lastToBeAverage = getLastFromToBeAverage(toBeAverage);
			averageDataLast.add(calculateAverageLast(lastToBeAverage));

			toBeAverage = fixAverage(toBeAverage, max);
			List<FisherData> average = calculateAverage(toBeAverage, max);
			averageData.addAll(average);

			// averageDataLast;

			// dataToBeAverage.add(toBeAverage);
		}

	}

	private static FisherData calculateAverageLast(List<FisherData> lastToBeAverage) {

		FisherData f = lastToBeAverage.get(0);
		int numByuersF = f.getNumByuers();
		int numGoodsF = f.getNumGoods();
		double iterationF = calculateAvgIterations(lastToBeAverage);
		String algoF = f.getAlgo();
		boolean considerDecisionCounterF = f.getConsiderDecisionCounter();
		int maxIterationF = f.getMaxIteration();
		double avgRX = calculateAvgRX(lastToBeAverage);
		double envyFreeF = calculateAvgEnvyFree(lastToBeAverage);
		FisherData ans = null;
		if (!central) {
			String distributionDelay = ((FisherDataDistributed) f).getDistributionDelay();
			String distributionParameter = ((FisherDataDistributed) f).getDistributionParameter();
			int parameterF = ((FisherDataDistributed) f).getParamter();
			ans = new FisherDataDistributed(-1, numByuersF, numGoodsF, iterationF, algoF, considerDecisionCounterF,
					maxIterationF, avgRX, envyFreeF, distributionDelay, distributionParameter, parameterF);
		} else {
			ans = new FisherDataCentralistic(-1, numByuersF, numGoodsF, iterationF, algoF, considerDecisionCounterF,
					maxIterationF, avgRX, envyFreeF);

		}

		return ans;
	}

	private static double calculateAvgEnvyFree(List<FisherData> lastToBeAverage) {
		List<Double> ans = new ArrayList<Double>();
		for (FisherData f : lastToBeAverage) {
			ans.add(f.getEnvyFree());
		}
		return calcAverage(ans);
	}

	private static double calculateAvgRX(List<FisherData> lastToBeAverage) {
		List<Double> ans = new ArrayList<Double>();
		for (FisherData f : lastToBeAverage) {
			ans.add(f.getSumRX());
		}
		return calcAverage(ans);
	}

	private static double calculateAvgIterations(List<FisherData> lastToBeAverage) {
		List<Double> ans = new ArrayList<Double>();
		for (FisherData f : lastToBeAverage) {
			ans.add(f.getIteration());
		}
		return calcAverage(ans);
	}

	private static double calcAverage(List<Double> vector) {
		double sum = 0.0;
		for (Double d : vector) {
			sum += d;
		}
		return sum / vector.size();
	}

	private static List<FisherData> getLastFromToBeAverage(List<List<FisherData>> toBeAverage) {
		List<FisherData> ans = new ArrayList<FisherData>();
		for (List<FisherData> list : toBeAverage) {
			FisherData last = list.get(list.size() - 1);
			ans.add(last);
		}
		return ans;
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
			double iterationF = t.getIteration();
			String algoF = t.getAlgo();
			boolean considerDecisionCounterF = t.getConsiderDecisionCounter();
			int maxIterationF = t.getMaxIteration();
			double rxF = calculateAvgRX(sameIterList);
			double envyFreeF = calculateAvgEnvyFree(sameIterList);
			if (!central) {
				int param = ((FisherDataDistributed) t).getParamter();
				String distDelay = ((FisherDataDistributed) t).getDistributionDelay();
				String distParam = ((FisherDataDistributed) t).getDistributionParameter();
				ans.add(new FisherDataDistributed(idF, numByuersF, numGoodsF, iterationF, algoF,
						considerDecisionCounterF, maxIterationF, rxF, envyFreeF, distDelay, distParam, param));
			} else {
				ans.add(new FisherDataCentralistic(idF, numByuersF, numGoodsF, iterationF, algoF,
						considerDecisionCounterF, maxIterationF, rxF, envyFreeF));
			}

			counter++;
		}

		return ans;
	}

	/*
	 * private static FisherData getFisherDataAverage(List<FisherData> sameIterList,
	 * int idF, int numByuersF, int numGoodsF, int iterationF, String algoF, boolean
	 * considerDecisionCounterF, int maxIterationF) {
	 * 
	 * double sumRX = 0.0; double sumR = 0.0; double sumX = 0.0;
	 * 
	 * double n = sameIterList.size(); for (FisherData fisherData : sameIterList) {
	 * sumRX += fisherData.getSumRX(); }
	 * 
	 * double avgR = sumR / sameIterList.size(); double avgX = sumX /
	 * sameIterList.size(); double avgRX = sumRX / sameIterList.size();
	 * 
	 * if (central) { return new FisherDataCentralistic(idF, numByuersF, numGoodsF,
	 * iterationF, algoF, considerDecisionCounterF, maxIterationF, avgR, avgX,
	 * avgRX); } else {
	 * 
	 * FisherDataDistributed t = (FisherDataDistributed) sameIterList.get(0); int
	 * param = t.getParamter(); String distDelay = t.getDistributionDelay(); String
	 * distParam = t.getDistributionParameter();
	 * 
	 * return new FisherDataDistributed(idF, numByuersF, numGoodsF, iterationF,
	 * algoF, considerDecisionCounterF, maxIterationF, avgR, avgX, avgRX, distDelay,
	 * distParam, param); }
	 * 
	 * }
	 */
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
				double iterationOfCopied = lastFisherDate.getIteration() + 1;
				while (list.size() < max) {

					if (central) {
						copiedFisherData = new FisherDataCentralistic(lastFisherDate, iterationOfCopied);
					} else {
						copiedFisherData = new FisherDataDistributed(lastFisherDate, iterationOfCopied);
					}
					iterationOfCopied++;
					list.add(copiedFisherData);
				}
			}
		}
		return toBeAverage;
	}
	/*
	 * private static Double[][] turnUtilToR(Utility[][] rutil) { Double[][] ans =
	 * new Double[rutil.length][rutil[0].length];
	 * 
	 * for (int i = 0; i < rutil.length; i++) { for (int j = 0; j < rutil[i].length;
	 * j++) { ans[i][j] = rutil[i][j].getUtility(1); } } return ans; }
	 */

	private static void runDistributed(List<List<Market>> markets) {

		Mailer mailer = new Mailer(distributionDelayType);
		for (List<Market> marketReps : markets) {
			runDifferentCommunicationOnMarketReps(marketReps, mailer);
		}

	}

	private static void runDifferentCommunicationOnMarketReps(List<Market> marketReps, Mailer mailer) {

		List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
		int max = 0;

		for (int parameter : distributionParameters) {
			for (Market market : marketReps) {
				market.restartMarketBetweenRuns(mailer, parameter);
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

	}

	private static int updateMaxIteration(int maxIteration, int currentMaxIter) {
		if (maxIteration < currentMaxIter) {
			return currentMaxIter;
		} else {
			return maxIteration;
		}
	}
	/*
	 * private static List<Mailer> createMailers() { List<Mailer> ans = new
	 * ArrayList<Mailer>(); for (double selectedP3 : p3s) { currP3 = selectedP3; if
	 * (selectedP3 == 0) { currP3 = 0; for (double selectedP4 : p4s) { currP4 =
	 * selectedP4; Mailer mailer = new Mailer(selectedP3, selectedP4, 0);
	 * ans.add(mailer); } } else { for (double selectedP4 : p4s) { currP4 =
	 * selectedP4;
	 * 
	 * if (dist) {
	 * 
	 * } for (int selectedParameter : ubs) { currDistributionParameter =
	 * selectedParameter; Mailer mailer = new Mailer(selectedP3, selectedP4,
	 * selectedParameter); ans.add(mailer); } } } }
	 * 
	 * return ans; }
	 */

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
