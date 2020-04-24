package SimulatorCreators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import Communication.ProtocolDelay;
import Communication.ProtocolDelayEl;
import Communication.ProtocolDown;
import Data.CreatorData;
import Data.CreatorExcel;
import Fisher.FisherSolver;
import Fisher.FisherSolverDistributed;
import Market.Mailer;
import Market.Market;

public class MainSimulator {

	public static boolean printForDebug = false;
	public static boolean envyDebug = true;

	// -------------MARKET PARAMETERS-------------
	/* run the algorithm synchronous or asynchronous */
	public static boolean central = false;
	/* delta of converges */
	public static final double THRESHOLD = 1E-10;
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
	public static int maxIteration = 1000;
	public static double epsilonEnvyFree = 0.05;

	// --- type delay
	public static int typeCommunication = 1; // 1 = el

	public static CreatorDelays creatorDelay;
	public static CreatorDowns creatorDown;

	public static Map<Mailer, List<FisherSolver>> fisherSolvers = new HashMap<Mailer, List<FisherSolver>>();



	public static void main(String[] args) {

		List<Market> markets = createMarkets();
		List<Mailer> mailers = createCommunicationProtocols();

		for (Mailer singleMailer : mailers) {
			iterateMailerOverAllMarkets(singleMailer, markets);
		}
		createExcel();
		
		
	}

	

	private static void createExcel() {
		CreatorExcel excel = new CreatorExcel(fisherSolvers);
		excel.createRawData();
		excel.createAvgData();
		excel.createRawLast();
		excel.createAvgLast();
		
		
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



	
	



	

	

	private static List<Market> createMarkets() {
		List<Market> markets = new ArrayList<Market>();
		for (int i = meanRepsStart; i < meanRepsEnd; i++) {
			markets.add(new Market(buyersNum, goodsNum, i));
		}
		return markets;
	}

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

	private static CreatorDowns getCreatorDown() {
		if (typeCommunication == 1) {
			return new CreatorDownsEl();
		}
		return null;
	}

	private static CreatorDelays getCreatorDelays() {
		if (typeCommunication == 1) {
			return new CreatorDelaysEl();
		}
		return null;
	}
	
}
