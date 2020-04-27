package SimulatorCreators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Communication.ProtocolDelay;
import Communication.ProtocolDelayEl;
import Communication.ProtocolDown;
import Communication.ProtocolDownEl;
import Data.CreatorExcel;
import Fisher.FisherSolver;
import Fisher.FisherSolverCentralistic;
import Fisher.FisherSolverDistributed;
import Market.Mailer;
import Market.Market;

public class MainSimulator {

	static int counter;
	// -------------**MARKET PARAMETERS**-------------
	public static boolean central = false;// run the algorithm synchronous or asynchronous
	public static final double THRESHOLD = 1E-8;// delta of converges
	public static double stdUtil = 150;// parameters for withdrawing utilities
	public static double muUtil = 500;// parameters for withdrawing utilities
	public static int buyersNum = 6;// number of buyers in market
	public static int goodsNum = 15;// number of goods in market

	// -------------**SIMULATOR**-------------
	public static int start = 0;// number of trials, start
	public static int end = 100;// number of trials, end
	public static int maxIteration = 5000;// if algorithm does not converge what is the upper bound avoid inf loop
	public static double epsilonEnvyFree = 0.5;
	public static boolean printForDebug = false;
	public static boolean envyDebug = false;

	// -------------**COMMUNICATION PROTOCOL**-------------
	public static int typeCommunication = 1; // 1 = el
	public static CreatorDelays creatorDelay;
	public static CreatorDowns creatorDown;

	// -------------**RESULTS PER MAILER**-------------
	public static Map<Mailer, List<FisherSolver>> fisherSolvers = new HashMap<Mailer, List<FisherSolver>>();

	/**
	 * create markets + create mailer-->solve each market given mailer--> produce
	 * results--> create excels
	 */
	public static void main(String[] args) {

		List<Market> markets = createMarkets();
		if (central) {
			runCentralistic(markets);
		} else {
			List<Mailer> mailers = createCommunicationProtocols();
			for (Mailer singleMailer : mailers) {
				iterateMailerOverAllMarkets(singleMailer, markets);
			}
		}	
		createExcel();
	}

	private static void runCentralistic(List<Market> markets) {
		List<FisherSolver> list = new ArrayList<FisherSolver>();
		for (Market market : markets) {
			if (market.getId() == 33) {
				System.out.println();
			}
			FisherSolver fs = new FisherSolverCentralistic(market, maxIteration, THRESHOLD);
			list.add(fs);
		}
		
		Mailer m = new Mailer(new ProtocolDelayEl(), new ProtocolDownEl());
		fisherSolvers.put(m, list);
	}

	/**
	 * create markets end-start times
	 * 
	 * @return List<Market>
	 */
	private static List<Market> createMarkets() {
		List<Market> markets = new ArrayList<Market>();
		for (int i = start; i < end; i++) {
			markets.add(new Market(buyersNum, goodsNum, i));
		}
		return markets;
	}

	/**
	 * create mailers according to all communication protocols, can update
	 * parameters in creator down and creator delay
	 * 
	 * @return List<Market>
	 */

	private static List<Mailer> createCommunicationProtocols() {
		List<Mailer> ans = new ArrayList<Mailer>();
		try {

			creatorDelay = getCreatorDelays();
			List<ProtocolDelay> delays = creatorDelay.createProtocolDelays();
			creatorDown = getCreatorDown();
			List<ProtocolDown> downs = creatorDown.createProtocolDowns();

			for (ProtocolDelay delay : delays) {
				for (ProtocolDown down : downs) {
					ans.add(new Mailer(delay, down));
				}
			}
		} catch (NullPointerException e) {
			System.err.println("type communication does not exsits");
		}
		return ans;
	}

	/**
	 * create Downs combinations give type communication
	 * 
	 * @return
	 */
	private static CreatorDowns getCreatorDown() {
		if (typeCommunication == 1) {
			return new CreatorDownsEl();
		}
		return null;
	}

	/**
	 * create delays combinations give type communication
	 * 
	 * @return
	 */
	private static CreatorDelays getCreatorDelays() {
		if (typeCommunication == 1) {
			return new CreatorDelaysEl();
		}
		return null;
	}

	/**
	 * solve each market given different mailer and produce fisherSolvers map of
	 * lists of solutions per mailer
	 * 
	 * @param mailer
	 * @param markets
	 */
	private static void iterateMailerOverAllMarkets(Mailer mailer, List<Market> markets) {
		List<FisherSolver> list = new ArrayList<FisherSolver>();
		for (Market market : markets) {
			mailer.resetMailer(market.getId(), market.getImperfectCommunicationMatrix());
			market.meetMailer(mailer);
			FisherSolver fs = new FisherSolverDistributed(market, mailer, maxIteration, THRESHOLD);
			list.add(fs);
		}
		System.out.println("done reps "+ (counter++));

		fisherSolvers.put(mailer, list);
	}

	/**
	 * create all types excel with help of creator excel class
	 */
	private static void createExcel() {
		CreatorExcel excel = new CreatorExcel(fisherSolvers);
		excel.createRawData();
		excel.createAvgData();
		excel.createRawLast();
		excel.createAvgLast();

	}

}
