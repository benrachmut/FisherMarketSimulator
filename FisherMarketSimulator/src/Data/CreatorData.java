package Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Fisher.FisherSolver;
import Market.Mailer;
import SimulatorCreators.MainSimulator;

/**
 * class to create data from experiments, creating avgs and performance at the
 * converges
 * 
 * @author Ben
 *
 */
public class CreatorData {
	private Map<Mailer, List<FisherSolver>> fisherSolvers;

	private List<String> rawData, avgData, rawLast, avgLast;

	public CreatorData(Map<Mailer, List<FisherSolver>> fisherSolvers) {
		super();
		this.fisherSolvers = fisherSolvers;
		initDataLists();
		createData();

	}

	
	
	
	public List<String> getRawData() {
		return rawData;
	}




	public List<String> getAvgData() {
		return avgData;
	}




	public List<String> getRawLast() {
		return rawLast;
	}




	public List<String> getAvgLast() {
		return avgLast;
	}




	private void initDataLists() {
		rawData = new ArrayList<String>();
		avgData = new ArrayList<String>();
		rawLast = new ArrayList<String>();
		avgLast = new ArrayList<String>();

	}

	private void createData() {
		createRawData();
		createAvgData();
		createRawLast();
		createAvgLast();
	}

	private void createAvgLast() {
		String marketData = MainSimulator.buyersNum + "," + MainSimulator.goodsNum;
		
		for (Entry<Mailer, List<FisherSolver>> differentMarketsPerSingleMailer : fisherSolvers.entrySet()) {
			String mailerData = differentMarketsPerSingleMailer.getKey().toString();
			List<FisherSolver> toAverage = differentMarketsPerSingleMailer.getValue();

				Collection<Integer> iterations = new ArrayList<Integer>();
				Collection<Double> rxs = new ArrayList<Double>();
				Collection<Double> envyFrees = new ArrayList<Double>();
				for (FisherSolver fs : toAverage) {
					
					Integer lastIter = fs.getLastIteration();
					
					iterations.add(lastIter);
					rxs.add(fs.getRx(lastIter));
					envyFrees.add(fs.getEnvyFree(lastIter));
				}
				double iterationAvg = calcAverageInteger(iterations);
				double rxAvg = calcAverageDouble(rxs);
				double envyFreesAvg = calcAverageDouble(envyFrees);
				
				String fisherData = MainSimulator.maxIteration + "," + MainSimulator.THRESHOLD + "," + iterationAvg + "," + rxAvg
						+ "," + envyFreesAvg;

				avgLast.add(mailerData + "," + marketData + "," + fisherData);

			
		}
		
	}

	private void createRawLast() {
		for (Entry<Mailer, List<FisherSolver>> differentMarketsPerSingleMailer : fisherSolvers.entrySet()) {
			String mailerData = differentMarketsPerSingleMailer.getKey().toString();
			for (FisherSolver fs : differentMarketsPerSingleMailer.getValue()) {
				String marketData = fs.getMarket().toString();
				String fisherData = fs.getLastInfo();
				rawLast.add(mailerData + "," + marketData + "," + fisherData);
			}
		}
	}

	private void createAvgData() {
		String marketData = MainSimulator.buyersNum + "," + MainSimulator.goodsNum;
		for (Entry<Mailer, List<FisherSolver>> differentMarketsPerSingleMailer : fisherSolvers.entrySet()) {
			String mailerData = differentMarketsPerSingleMailer.getKey().toString();
			List<FisherSolver> toAverage = differentMarketsPerSingleMailer.getValue();

			for (int i = 0; i < MainSimulator.maxIteration; i++) {
				Collection<Double> rxs = new ArrayList<Double>();
				Collection<Double> envyFrees = new ArrayList<Double>();
				for (FisherSolver fs : toAverage) {
					rxs.add(fs.getRx(i));
					envyFrees.add(fs.getEnvyFree(i));
				}
				double rxAvg = calcAverageDouble(rxs);
				double envyFreesAvg = calcAverageDouble(envyFrees);
				String fisherData = MainSimulator.maxIteration + "," + MainSimulator.THRESHOLD + "," + -1 + "," + rxAvg
						+ "," + envyFreesAvg;

				avgData.add(mailerData + "," + marketData + "," + fisherData);

			}
		}

	}



	private void createRawData() {
		for (Entry<Mailer, List<FisherSolver>> differentMarketsPerSingleMailer : fisherSolvers.entrySet()) {
			String mailerData = differentMarketsPerSingleMailer.getKey().toString();
			for (FisherSolver fs : differentMarketsPerSingleMailer.getValue()) {
				String marketData = fs.getMarket().toString();

				for (int i = 0; i < MainSimulator.maxIteration; i++) {

					String fisherData = fs.getInfoGivenIteration(i);
					rawData.add(mailerData + "," + marketData + "," + fisherData);
				}
			}

		}
	}
	
	
	public static double calcAverageDouble(Collection<Double> vector) {
		double sum = 0.0;
		for (Double d : vector) {
			sum = sum + d;
		}
		return sum / vector.size();
	}
	
	public static double calcAverageInteger(Collection<Integer> vector) {
		double sum = 0.0;
		for (Integer d : vector) {
			sum = sum + (double)d;
		}
		return sum / vector.size();
	}

}
