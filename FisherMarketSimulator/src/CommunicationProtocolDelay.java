import java.util.Random;

public class CommunicationProtocolDelay {
	
	private double dealyConstantSparsity, 
	delayConstant,dealyNoiseSparsity,
	mu,std;
	private boolean perfectCommunication, timeStamp;
	private Random dealyNormRand, dealyConstantSparsityRand, dealyNoiseSparsityRand;
	
	public CommunicationProtocolDelay() {
		perfectCommunication = true;
	}

	public CommunicationProtocolDelay(double dealyConstantSparsity, double delayConstant, double dealyNoiseSparsity,
			double mu, double std, boolean considerDecisionCounter) {
		super();
		this.dealyConstantSparsity = dealyConstantSparsity;
		this.delayConstant = delayConstant;
		this.dealyNoiseSparsity = dealyNoiseSparsity;
		this.mu = mu;
		this.std = std;
		this.perfectCommunication = false;
		this.timeStamp=considerDecisionCounter;
	
	}
	
	public void setSeed(long marketId) {
		long seed1 = marketId*1000+ (int)(dealyConstantSparsity*100)+ (int)(delayConstant*10);
		long seed2 = marketId*1000+ (int)(mu*100)+ (int)(std *10);
		long seed3 = marketId*1000+ (int)(dealyNoiseSparsity*100)+ (int)(mu*10);

		dealyNormRand =  new Random(seed1);
		dealyConstantSparsityRand = new Random(seed2);
		dealyNoiseSparsityRand= new Random(seed3);
	}
	
	
	@Override
	public String toString() {
		String ans =
		this.dealyConstantSparsity+","+
		this.delayConstant +","+
		this.dealyNoiseSparsity+","+
		this.mu+","+
		this.std +","+
		this.perfectCommunication +","+
		this.timeStamp;
		return ans;
	}
	
	public static String header() {
		String ans =
		"dealyConstantSparsity"+","+
		"delayConstant" +","+
		"dealyNoiseSparsity"+","+
		"mu"+","+
		"std" +","+
		"perfectCommunication" +","+
		"timeStamp";
		return ans;
	}

	public boolean isPerfectCommunication() {
		return this.perfectCommunication;
	}

	public double getDealyConstantSparsity() {
		return this.dealyConstantSparsity;
	}

	public Random getDelayConstantSparsityRand() {
		return this.dealyConstantSparsityRand;
	}

	public double getDealyNoiseSparsity() {
		return this.dealyNoiseSparsity;
	}

	public Random getDelayNoiseSparsityRand() {
		return this.dealyNoiseSparsityRand;
	}

	public boolean isWithTimeStamp() {
		// TODO Auto-generated method stub
		return timeStamp;
	}
	
	
	


}
