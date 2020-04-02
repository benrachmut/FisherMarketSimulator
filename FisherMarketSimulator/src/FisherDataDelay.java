
public class FisherDataDelay {

	private int marketId;
	private int numByuers;
	private int numGoods;
	private String parameterDistribution;
	private String DelayDistribution;
	private int inputDelay;
	private double averageDelay;
	private int numberOfMessages;

	public FisherDataDelay(Market m, String parameterDistribution, 
			String DelayDistribution, int inputDelay, double averageDelay, int numberOfMessages) {
		this.marketId = m.getId();
		this.numByuers=m.getBuyers().size();
		this.numGoods=m.getGoods().size();
		this.parameterDistribution =  parameterDistribution;
		this.DelayDistribution=DelayDistribution;
		this.inputDelay=inputDelay;
		this.averageDelay=averageDelay;
		this.numberOfMessages = numberOfMessages;
	}
	
	public static String header() {
		return 	"Market Id, Buyuers, Goods, "
				+ "Parameter Distribution, Delay Distribution, "
				+ "Input Delay, Average Delay, Number Of Messages"; 
	}

	
	
	
}
