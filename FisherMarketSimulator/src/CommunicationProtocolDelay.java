
public class CommunicationProtocolDelay {
	
	private double dealyConstantSparsity, 
	delayConstant,dealyNoiseSparsity,
	mu,std;
	private boolean perfectCommunication;
	
	public CommunicationProtocolDelay() {
		perfectCommunication = true;
	}

	public CommunicationProtocolDelay(double dealyConstantSparsity, double delayConstant, double dealyNoiseSparsity,
			double mu, double std) {
		super();
		this.dealyConstantSparsity = dealyConstantSparsity;
		this.delayConstant = delayConstant;
		this.dealyNoiseSparsity = dealyNoiseSparsity;
		this.mu = mu;
		this.std = std;
		this.perfectCommunication = false;
	}
	
	


}
