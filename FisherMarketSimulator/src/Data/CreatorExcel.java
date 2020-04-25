package Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import Fisher.FisherSolver;
import Market.Mailer;
import Market.Market;
import SimulatorCreators.MainSimulator;

public class CreatorExcel {
	private CreatorData data;
	private String partName;

	public CreatorExcel(Map<Mailer, List<FisherSolver>> fisherSlovers) {
		super();
		this.data = new CreatorData(fisherSlovers);
		this.partName = "reps_"+(MainSimulator.end-MainSimulator.start)+","+"buyers_" + MainSimulator.buyersNum + "," + "goods_" + MainSimulator.goodsNum + ","
				+ "thrashold_" + MainSimulator.THRESHOLD + "," + "rnd_" + Math.random();
	}

	private static String createHeader() {
		String ans = MainSimulator.creatorDelay.header()+",";
		ans= ans+MainSimulator.creatorDown.header() + ",";
		ans = ans + Market.header()+ ",";
		ans = ans + FisherSolver.header();

		return  ans;
	}

	public void createRawData() {
		createExcel("RawData",this.data.getRawData());
	}

	
	public void createAvgData() {
		createExcel("AvgData",this.data.getAvgData());		
	}
	
	public void createRawLast() {
		createExcel("RawLast",this.data.getRawLast());		
		
	}
	
	public void createAvgLast() {
		createExcel("AvgLast",this.data.getAvgLast());		
		
	}
	public void createExcel(String name, List<String> data) {
		BufferedWriter out = null;
		try {
			FileWriter s = new FileWriter(name+","+this.partName+ ".csv");
			out = new BufferedWriter(s);
			if (MainSimulator.central==false) {
				String header = createHeader();
				out.write(header);
				out.newLine();
			}
			
			for (String o : data) {
				out.write(o);
				out.newLine();
			}
			out.close();
		} catch (Exception e) {
			System.err.println("Couldn't open the file");
		}
	}

	

	


}
