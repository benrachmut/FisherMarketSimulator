package Communication;

import java.util.Random;

public class ProtocolDelayEl extends ProtocolDelay {

	private double msgLostProb; // aka gamma
	private double sigma;
	private double muLB; // aka k
	private double noiseLB;
	private double noiseUB;
	private Random msgLostRandom, delayNormalRandom, noiseRandom;

	public ProtocolDelayEl(boolean perfectCommunication, boolean isTimeStamp, double msgLostProb, double sigma,
			double muLB, double noiseLB, double noiseUB) {
		super(perfectCommunication, isTimeStamp);
		this.msgLostProb = msgLostProb;
		this.sigma = sigma;
		this.muLB = muLB;
		this.noiseLB = noiseLB;
		this.noiseUB = noiseUB;

	}

	public ProtocolDelayEl(boolean perfectCommunication) {
		super(true, false);
		this.msgLostProb = 0;
		this.sigma = 0;
		this.muLB = 0;
		this.noiseLB = 0;
		this.noiseUB = 0;
	}

	public void setSeeds(int marketId) {
		long seed1 = marketId * 1000 + (int) ((1 - msgLostProb) * 100) + (int) (sigma * 10);
		long seed2 = marketId * 1000 + (int) (muLB * 100) + (int) (noiseUB * 10);
		long seed3 = marketId * 1000 + (int) (sigma * 100) + (int) (muLB * 10);

		delayNormalRandom = new Random(seed1);
		noiseRandom = new Random(seed2);
		msgLostRandom = new Random(seed3);
	}

	@Override
	public Integer createDelay(double q_ij, double p_ij) {

		if (perfectCommunication || q_ij == 0) {
			return 0;
		}

		double pLost = msgLostRandom.nextDouble();
		if (pLost < this.msgLostProb) {
			return null;
		}

		else {

			double n = this.noiseLB + this.noiseRandom.nextDouble() * (this.noiseUB - this.noiseLB);
			double mu = muLB + p_ij * n;
			double z = delayNormalRandom.nextGaussian();
			double ans = z * this.sigma + mu;
			return (int) ans;
		}

	}

	/*
	 * public ProtocolDelayEl() { perfectCommunication = true; }
	 * 
	 * public ProtocolDelay(double dealyConstantSparsity, double delayConstant,
	 * double dealyNoiseSparsity, double mu, double std, boolean
	 * considerDecisionCounter) { super(); this.dealyConstantSparsity =
	 * dealyConstantSparsity; this.delayConstant = delayConstant;
	 * this.dealyNoiseSparsity = dealyNoiseSparsity; this.mu = mu; this.std = std;
	 * this.perfectCommunication = false; this.timeStamp=considerDecisionCounter;
	 * 
	 * }
	 * 
	 * 
	 * 
	 */

}
