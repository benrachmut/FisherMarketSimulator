package Market;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import Communication.CommunicationProtocolDelay;
import Communication.ProtocolDelay;
import Communication.ProtocolDelayEl;
import Communication.ProtocolDown;
import Fisher.FisherData;
import Fisher.FisherDataCentralistic;
import Fisher.FisherDataDelay;
import Fisher.FisherDataDistributed;
import Fisher.FisherSolver;
import Fisher.FisherSolverDistributed;

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
	public static double stdUtil = 500;
	public static double muUtil = 50;
	// public static int numberTypes = 4;
	/* market size */
	public static int buyersNum = 6;
	public static int goodsNum = 9;
	/* number of trials, start and ends */
	public static int meanRepsStart = 0;
	public static int meanRepsEnd = 1;
	/*
	 * if algorithm does not converge what is the upper bound avoid inf loop
	 */
	public static int maxIteration = 10000;
	/* Random variables */
	// public static Random utilRandom, goodTypesRandom;
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

	// --- type delay
	public static int typeCommunication = 1; // 1 = el
	// ----For all type delay
	public static boolean[] perfectCommunications = { true, false };
	public static boolean[] isTimeStamps = { true, false };

	// ----For el delay
	public static double[] gammas = { 0.02 }; // aka gamma
	public static double[] sigmas = { 10 };
	public static double[] ks = { 10 }; // aka k
	public static double[] hs = { 5 };
	public static double[] deltas = { 10 };
	public static double[] lambdas = { 0.5 };
	public static double[] lambda_tags = { 0.66 };

	// public static Random dealyConstantSparsityRand, dealyNoiseSparsityRand;

	// -------------DOWN PARAMETERS-------------
	/* should csp be withdrawn or copied from dcsp */
	/*
	 * public static boolean copySparsityProb = false;// used in market public
	 * static double[] downSparsityProbs = { 0 };// set with market public static
	 * int[] downKs = { 5 }; public static double[] downInfProbs = { 0 }; public
	 * static double[] downNumIterProbs = { 0 }; public static double[]
	 * downNumIterParameters = { 0 };
	 */

	public static Map<Mailer, List<FisherSolver>> fisherSolvers = new HashMap<Mailer, List<FisherSolver>>();

	public static List<String> rawData;
	public static List<String> avgData;
	public static List<String> rawLast;
	public static List<String> avgLast;

	public static void main(String[] args) {

		List<Market> markets = createMarkets();
		List<Mailer> mailers = createCommunicationProtocols();

		for (Mailer singleMailer : mailers) {
			iterateMailerOverAllMarkets(singleMailer, markets);
		}
		createData();
		createExcel();
	}

	private static void iterateMailerOverAllMarkets(Mailer mailer, List<Market> markets) {
		List<FisherSolver> list = new ArrayList<FisherSolver>();
		for (Market market : markets) {
			mailer.resetMailer(market.getId(), market.getImperfectCommunicationMatrix());
			market.meetMailer(mailer);
			FisherSolver fs = new FisherSolverDistributed(market, maxIteration, THRESHOLD);
			list.add(fs);
		}
		fisherSolvers.put(mailer, list);
	}

	private static void createData() {
		createRawData();
		createAvgData();
		createRawDataLast();
		createAvgDataLast();
	}

	private static void createRawData() {

		/*
		 * for (Entry<Mailer, List<FisherSolver>> differentMarketsPerSingleMailer :
		 * fisherSolvers.entrySet()) { String mailerData =
		 * differentMarketsPerSingleMailer.getKey().toString();
		 * 
		 * for (int i = 0; i < maxIteration; i++) {
		 * 
		 * } }
		 */

	}

	private static double calcAverage(List<Double> vector) {
		double sum = 0.0;
		for (Double d : vector) {
			sum += d;
		}
		return sum / vector.size();
	}

	private static List<Market> createMarkets() {
		List<Market> markets = new ArrayList<Market>();
		for (int i = meanRepsStart; i < meanRepsEnd; i++) {
			Market market = new Market(buyersNum, goodsNum, i);
			markets.add(market);
		}
		return markets;
	}

	private static List<Mailer> createCommunicationProtocols() {
		List<Mailer> ans = new ArrayList<Mailer>();

		List<ProtocolDelay> cpDelays = createCPDelay();
		List<ProtocolDown> cpDown = createCPDown();

		for (CommunicationProtocolDelay delay : cpDelays) {
			for (ProtocolDown down : cpDown) {
				ans.add(new Mailer(delay, down));
			}
		}

		return ans;
	}

	private static List<ProtocolDelay> createCPDelay() {
		List<ProtocolDelay> ans = new ArrayList<ProtocolDelay>();
		for (boolean perfectP : perfectCommunications) {
			if (perfectP == true) {
				ans.add(createDefultProtocolDelay());
			} else {
				ans.addAll(createCombinationsDelay());
			}
		}
		return ans;
	}

	private static List<ProtocolDelay> createCombinationsDelay() {
		List<ProtocolDelay> ans = new ArrayList<ProtocolDelay>();

		for (boolean timestampBoolean : isTimeStamps) {
			if (typeCommunication == 1) {
				ans.addAll(createCPDelayEl(false, timestampBoolean));
			}
		}
		return ans;
	}

	private static ProtocolDelay createDefultProtocolDelay() {
		if (typeCommunication == 1) {
			return new ProtocolDelayEl();
		} else {
			return null;
		}

	}

	private static List<ProtocolDelay> createCPDelayEl(boolean perfectP, boolean timestampBoolean) {
		List<ProtocolDelay> ans = new ArrayList<ProtocolDelay>();
		// ----For el delay

		for (double gamma : gammas) {
			for (double sigma : sigmas) {
				for (double k : ks) {
					for (double h : hs) {
						for (double delta : deltas) {
							for (double lambda : lambdas) {
								for (double lambda_tag : lambda_tags) {
									ans.add(new ProtocolDelayEl(perfectP, timestampBoolean, gamma, sigma, k, h, delta,
											lambda, lambda_tag));
								}
							} // lambda
						} // delta
					} // h
				} // k
			} // sigma
		} // gamma

		return ans;
	}

}
