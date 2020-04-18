import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainSimulator {

	public static boolean printForDebug = false;
	public static boolean envyDebug = true;

	// -------------MARKET PARAMETERS-------------
	/* run the algorithm synchronous or asynchronous */
	public static boolean central = true;
	/* delta of converges */
	protected static final double THRESHOLD = 1E-10;
	/*
	 * parameters for withdrawing utilities maybe change to actual calculating
	 * withdrawing parameters
	 */
	public static double stdUtil = 100;
	public static double muUtil = 100;
	public static int numberTypes = 4;
	/* market size */
	public static int buyersNum =6 ;
	public static int goodsNum = 9 ;
	/* number of trials, start and ends */
	public static int meanRepsStart = 0;
	public static int meanRepsEnd = 1;
	/*
	 * if algorithm does not converge what is the upper bound avoid inf loop
	 */
	public static int maxIteration = 1000000;
	/* Random variables */
	//public static Random utilRandom, goodTypesRandom;
	/*
	 * sometimes agent is envy just a little and algo needs to continue alot until
	 * it changes
	 */
	public static double epsilonEnvyFree = 0.05;

	// public static int distributionParameterType = 1; // 1 = uniform, 2 = exp
	// public static int distributionDelayType = 1;// 1 = uniform, 2 = exפ
	// public static String distributionParameterString;
	// public static String distributionDelayString;
	// public static boolean simplisticDelay = true;
	// public static int[] distributionParameters = { 0, 25, 50, 200, 500 };

	// -------------DELAY PARAMETERS-------------

	/*
	 * The probability for line to have constant delay between them. false = no,
	 * true = yes delay. Note a buyer that holds a good causes for value in matrix
	 * to be false regardless of probability withdrawn.
	 */
	public static double[] dealyConstantSparsityProbs = { 1 };// set with market
	/*
	 * if sparse true constant delay present represents the velocity a message is
	 * passed
	 */
	public static double[] delayConstants = { 5 };
	/*
	 * Additionally for constant delay, The probability for line to have extra
	 * latency caused by noise. false = no, true = yes delay. or buyers and goods
	 * actually to have noise is the Multiplication between dnsp and dcsp.
	 */
	public static double[] dealyNoiseSparsityProbs = { 1 };// set with market
	/* if sparse true for noise of delay is withdrawn from normal distribution */
	public static double[] delayMuNoises = { 10 };
	public static double[] delayStdNoises = { 3 };
	/* do we ignore messages that where overlapped? */
	public static boolean[] withTimeStamps = {true,false};
	/* Random variables */
	//public static Random dealyConstantSparsityRand, dealyNoiseSparsityRand;

	// -------------DOWN PARAMETERS-------------
	/* should csp be withdrawn or copied from dcsp */
	public static boolean copySparsityProb = false;// used in market
	/*
	 * probability for communication to be blocked between buyers and goods, with it
	 * create matrix of buyers and goods or not , false = no blocks, true = block
	 * may be created. Note a buyer that holds a good causes for value in matrix to
	 * be false regardless of probability withdrawn.
	 */
	public static double[] downSparsityProbs = { 0 };// set with market
	/* counter for possible block each k computation */
	public static int[] downKs = { 5 };
	/*
	 * probability for a buyer to be completely cut completely. If buyer holds a
	 * good, the good is also cut off.
	 */
	public static double[] downInfProbs = { 0 };
	/*
	 * if buyer is not completely cut of, probability for a buyer to be cut of for a
	 * number of iterations.
	 */
	public static double[] downNumIterProbs = { 0 };
	/*
	 * if buyer is cut of for number of iterations, parameter for withdrawing number
	 * of iterations.
	 */
	public static double[] downNumIterParameters = { 0 };
	

	static List<List<FisherSolver>>fisherSolvers = new ArrayList<List<FisherSolver>>();
	
	public static void main(String[] args) {


		List<Market> markets = createMarkets();
		List<Mailer> mailers = createCommunicationProtocols();
		
		for (Mailer mailer : mailers) {
			List<FisherSolver>list = new ArrayList<FisherSolver>();
			for (Market market : markets) {
				mailer.setSeeds(market.getId());
				market.meetMailer(mailer);
				FisherSolver fs = new FisherSolverDistributed(market);
				list.add(fs);
			}
			fisherSolvers.add(list);
		}
		createExcel();
	}




/*
	private static void MarketsMeetMailers(List<MarketReps> marketReps, List<Mailer> mailers) {
		for (MarketReps marketRep : marketReps) {
			marketRep.meetMailers(mailers);
		}
		
	}
*/




	private static List<Market> createMarkets() {
		//for (Integer b : buyers) {
		//	currBuyersNum = b;
		//	for (Integer s : goods) {
			//	currGoodsNum = s;
			List<Market> markets = new ArrayList<Market>();
				// List<Market> repsMarkets = new ArrayList<Market>();
				for (int i = meanRepsStart; i < meanRepsEnd; i++) {
					//restartRandomUtilAndType(buyersNum, goodsNum, i);
					Market market = new Market(buyersNum, goodsNum, i);
					markets.add(market);
				}			
		return markets;
	}

	private static List<Mailer> createCommunicationProtocols() {
		List<Mailer> ans = new ArrayList<Mailer>();

		List<CommunicationProtocolDelay> cpDelays = createCPDelay();
		List<CommunicationProtocolDown> cpDown = createCPDown();

		for (CommunicationProtocolDelay delay : cpDelays) {
			for (CommunicationProtocolDown down : cpDown) {
				ans.add(new Mailer(delay, down));
			}
		}

		return ans;
	}

	private static List<CommunicationProtocolDown> createCPDown() {
		List<CommunicationProtocolDown> ans = new ArrayList<CommunicationProtocolDown>();
		for (double downSparsity : downSparsityProbs) {// set with market
			if (downSparsity == 0) {
				ans.add(new CommunicationProtocolDown());
			}

			else {
				for (int downK : downKs) {
					for (double downInf : downInfProbs) {
						for (double downNumIterProb : downNumIterProbs) {
							for (double downNumIterParameter : downNumIterParameters) {
								ans.add(new CommunicationProtocolDown(downSparsity, downK, downInf, downNumIterProb,
										downNumIterParameter, copySparsityProb));
							}
						} // downNumIterProb;
					} // downInfProb
				} // downK
			} // else
		} // downSparsity(
		return ans;
	}

	private static List<CommunicationProtocolDelay> createCPDelay() {
		List<CommunicationProtocolDelay> ans = new ArrayList<CommunicationProtocolDelay>();
		CommunicationProtocolDelay cpDelay = null;
		for (double dealyConstantSparsity : dealyConstantSparsityProbs) {// set with market
			if (dealyConstantSparsity == 0) {
				ans.add(new CommunicationProtocolDelay());
			} else {
				for (double delayConstant : delayConstants) {
					for (double dealyNoiseSparsity : dealyNoiseSparsityProbs) {// set with market
						for (double mu : delayMuNoises) {
							for (double std : delayStdNoises) {
								for (boolean isWithTimeStamp : withTimeStamps) {
									ans.add(new CommunicationProtocolDelay(dealyConstantSparsity, delayConstant,
											dealyNoiseSparsity, mu, std, isWithTimeStamp));
								}
							} // std
						} // mu
					} // dealyNoiseSparsity
				} // delayConstant
			} // else zero
		} // dealyConstantSparsity
		return ans;
	}
	
	
	public static double getRandomNormUtil(int goodType, Random randomUtil) {
		return getRandomNormal(randomUtil, ((double)goodType)*muUtil,stdUtil);
	}

	private static double getRandomNormal(Random randomUtil, double mu, double std) {
		return randomUtil.nextGaussian()*std+mu;
	}
	
	
/*
	private static void setSparsityForMarkets(List<List<Market>> markets) {
		for (List<Market> list : markets) {
			for (Market market : list) {
				setSparsityPerSingleMarket(market);
			}
		}
	}
	*/
/*
	private static void setSparsityPerSingleMarket(Market market) {
		
		long[] seeds = composeSeeds(market);

		for (double p : dealyConstantSparsityProbs) {
			dealyConstantSparsityRand = new Random(seeds[0]);
			market.setDealyConstantSparsity(p, dealyConstantSparsityRand);
		}

		for (double p : dealyNoiseSparsityProbs) {
			dealyNoiseSparsityRand = new Random(seeds[1]);
			market.setDealyNoiseSparsity(p, dealyNoiseSparsityRand);
		}

		for (double p : downSparsityProbs) {
			downSparsityRand = new Random(seeds[2]);
			market.setDownSparsityRand(p, downSparsityRand);
		}

	}
	*/
/*
	private static long[] composeSeeds(Market market) {
		long[] ans = new long[3];

		int id = market.getId();
		int numBuyers = market.getBuyers().size();
		int numGoods = market.getGoods().size();

		ans[0] = id * 1000 + numBuyers * 100 + numGoods * 10;
		ans[1] = numBuyers * 1000 + id * 100 + numGoods * 10;
		ans[2] = numGoods * 1000 + numBuyers * 100 + id * 10;

		return ans;
	}
	*/

	/*
	 * private static void handleDistributions() { if (distributionDelayType == 1) {
	 * distributionDelayString = "Uniform"; } else { distributionDelayString =
	 * "Exponential"; }
	 * 
	 * if (distributionParameterType == 1) { distributionParameterString =
	 * "Uniform"; } else { distributionParameterString = "Exponential"; }
	 * 
	 * }
	 */
	/*
	 * private static void setParametersForMarkets(List<List<Market>> markets) {
	 * RandomNumberGenerator rng = null; if (distributionParameterType == 1) {
	 * 
	 * rng = RandomNumberGenerator.Uniform; }
	 * 
	 * if (distributionParameterType == 2) { rng =
	 * RandomNumberGenerator.Exponential; }
	 * 
	 * for (List<Market> list : markets) { for (Market market : list) { for (int
	 * parameter : distributionParameters) { currParameter = parameter;
	 * market.createParameterMatrix(parameter, rng); } } } }
	 */
	
	
	private static void createExcel() {

		for (int i = 1; i <= 4; i++) {

			if (i == 1) {
				createFile(averageData, i);
			}
			if (i == 2) {
				createFile(allData, i);
			}
			if (i == 3) {
				createFile(averageDataLast, i);
			}
			if (i == 4) {
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
		if (central) {
			return FisherData.header();
		}

		else {
			return Market.header()+","+Mailer.header()+","+FisherSolver.header();
		}
	}

	private static String createFileName(int input) {

		String typeFile = null;
		if (input == 1) {
			typeFile = "averageAll";
		}
		if (input == 2) {
			typeFile = "fullAll";
		}
		if (input == 3) {
			typeFile = "averageLast";
		}
		if (input == 4) {
			typeFile = "fullLast";
		}

		typeFile = "typeFile_" + typeFile + ",";
		String central1 = "central_" + central + ",";
		String meanRepsStart1 = "start_" + meanRepsStart + ",";
		String meanRepsEnd1 = "end_" + meanRepsEnd;

		return typeFile + central1 + meanRepsStart1 + meanRepsEnd1;
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
/*
	private static List<FisherData> getLastFromToBeAverage(List<List<FisherData>> toBeAverage) {
		List<FisherData> ans = new ArrayList<FisherData>();
		for (List<FisherData> list : toBeAverage) {
			FisherData last = list.get(list.size() - 1);
			ans.add(last);
		}
		return ans;
	}
	*/
/*
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

*/
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
	/*
	private static void checkIfToBeAverageIsValid(List<List<FisherData>> toBeAverage, int max) {
		for (List<FisherData> list : toBeAverage) {
			int sizeOfList = list.size();
			if (sizeOfList != max) {
				System.err.println("size of a list is " + sizeOfList + " and max is " + max);
				throw new RuntimeException();
			}
		}

	}
	*/
/*
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
	*/
	/*
	 * private static Double[][] turnUtilToR(Utility[][] rutil) { Double[][] ans =
	 * new Double[rutil.length][rutil[0].length];
	 * 
	 * for (int i = 0; i < rutil.length; i++) { for (int j = 0; j < rutil[i].length;
	 * j++) { ans[i][j] = rutil[i][j].getUtility(1); } } return ans; }
	 */

	private static void runDistributed(List<Mailer> communicationProtocols, List<List<Market>> markets) {

		for (List<Market> marketReps : markets) {
			runDifferentCommunicationOnMarketReps(communicationProtocols, marketReps);
		}

	}

	private static void runDifferentCommunicationOnMarketReps(List<Mailer> communicationProtocols,
			List<Market> marketReps) {

		for (Mailer cp : communicationProtocols) {
			int max = 0;
			List<List<FisherData>> toBeAverage = new ArrayList<List<FisherData>>();
			List<FisherDataDelay> averageDelayPerMarket = new ArrayList<FisherDataDelay>();

			for (Market market : marketReps) {
				MailerZZZZ mailer = updateMarket(market, cp);
				List<FisherData> data = runFisher(market);
				max = updateMaxIteration(max, data.size());
				handleData(data, toBeAverage, averageDelayPerMarket, mailer, market, parameter);
				System.out.println(market);
			}
			// FisherDataDelay averageDelay =
			// createWeightedDealyData(averageDelayPerMarket);
			// averageWeightedDelayPerParameter.add(averageDelay);
			handleAverageData(toBeAverage, max);

		}

	}
/*
	private static void handleAverageData(List<List<FisherData>> toBeAverage, int max) {
		List<FisherData> lastToBeAverage = getLastFromToBeAverage(toBeAverage);
		averageDataLast.add(calculateAverageLast(lastToBeAverage));
		toBeAverage = fixAverage(toBeAverage, max);
		List<FisherData> average = calculateAverage(toBeAverage, max);
		averageData.addAll(average);

	}
	*/

	/*
	 * private static FisherDataDelay createWeightedDealyData(List<FisherDataDelay>
	 * averageDelayPerMarket) { new return null; }
	 */

	/*
	private static void handleData(List<FisherData> data, List<List<FisherData>> toBeAverage,
			List<FisherDataDelay> averageDelayPerMarket, MailerZZZZ mailer, Market market, int inputParameter) {
		toBeAverage.add(data);
		allData.addAll(data);
		allDataLast.add(data.get(data.size() - 1));
		handleDelayData(mailer, market, averageDelayPerMarket, inputParameter);
	}
*/
	/*
	private static void handleDelayData(MailerZZZZ mailer, Market market, List<FisherDataDelay> averageDelayPerMarket,
			int inputParameter) {
		List<Double> delays = mailer.getDelays();
		int numberMessages = delays.size();
		double averageDelay = calcAverage(delays);
		FisherDataDelay fdDelay = new FisherDataDelay(market, distributionParameterString, distributionDelayString,
				inputParameter, averageDelay, numberMessages);
		averageDelayPerMarket.add(fdDelay);

	}
	*/
/*
	private static List<FisherData> runFisher(Market market) {
		FisherSolver f = new FisherSolverDistributed(market);
		return f.algorithm();
	}
*/
	/*
	private static MailerZZZZ updateMarket(Market market, Mailer cp) {
		// double[][] parameterMatrix = market.getParametersMatrix(parameter);
		MailerZZZZ mailer = new MailerZZZZ(cp);
		market.restartMarketBetweenRuns(mailer);
		return mailer;
	}
	*/

	/*
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
				// System.out.println(market);
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
*/
	
	/*
	private static int updateMaxIteration(int maxIteration, int currentMaxIter) {
		if (maxIteration < currentMaxIter) {
			return currentMaxIter;
		} else {
			return maxIteration;
		}
	}

*/






}
