import java.util.Random;

public class CommunicationProtocolDelay {
	
	private double dealyConstantSparsity, 
	delayConstant,dealyNoiseSparsity,
	mu,std;
	private boolean perfectCommunication, timeStamp;
	
	private Random dealyNormRand;
	
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
	
	public void setSeed(long seed) {
		dealyNormRand =  new Random(seed);
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

	public double getDealyConstantSparsity() {
		return dealyConstantSparsity;
	}

	public void setDealyConstantSparsity(double dealyConstantSparsity) {
		this.dealyConstantSparsity = dealyConstantSparsity;
	}

	public double getDelayConstant() {
		return delayConstant;
	}

	public void setDelayConstant(double delayConstant) {
		this.delayConstant = delayConstant;
	}

	public double getDealyNoiseSparsity() {
		return dealyNoiseSparsity;
	}

	public void setDealyNoiseSparsity(double dealyNoiseSparsity) {
		this.dealyNoiseSparsity = dealyNoiseSparsity;
	}

	public double getMu() {
		return mu;
	}

	public void setMu(double mu) {
		this.mu = mu;
	}

	public double getStd() {
		return std;
	}

	public void setStd(double std) {
		this.std = std;
	}

	public boolean isPerfectCommunication() {
		return perfectCommunication;
	}

	public void setPerfectCommunication(boolean perfectCommunication) {
		this.perfectCommunication = perfectCommunication;
	}

	public boolean isTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(boolean timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
	


}
