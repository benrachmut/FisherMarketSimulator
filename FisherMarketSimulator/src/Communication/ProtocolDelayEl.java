package Communication;

import java.util.Random;

public class ProtocolDelayEl extends ProtocolDelay {

	private double gamma; // aka gamma
	private double sigma;
	private double k; // aka k
	private double h;
	private double delta;
	private double lambda;
	private double lambda_tag;

	private Random msgLostRandom, delayNormalRandom, noiseRandom;

	public ProtocolDelayEl( boolean isTimeStamp, double gamma, // aka gamma
			double sigma, double k, // aka k
			double h, double delta, double lambda, double lambda_Tag) {

		super(false, isTimeStamp);
		this.gamma = gamma;
		this.sigma = sigma;
		this.k = k;

		this.gamma = gamma;
		this.sigma = sigma;
		this.k = k; // aka k
		this.h = h;
		this.delta = delta;
		this.lambda = lambda;
		this.lambda_tag = lambda_Tag;

	}

	public ProtocolDelayEl() {
		super(true, false);
		this.gamma = 0;
		this.sigma = 0;
		this.k = 0;
		this.h = 0;
		this.delta = 0;
		this.lambda = 0;
		this.lambda_tag = 0;

	}
	
	@Override
	public String toString() {
		
		String ans = 
		super.toString()+","+
		this.gamma+","+
		this.sigma+","+
		this.k+","+
		this.h+","+
		this.delta+","+
		this.lambda+","+
		this.lambda_tag;
		
		
		return ans;
	}

	
	
	
	public void setSeeds(int marketId) {
		long seed1 = marketId * 1000 + (int) ((1 - gamma) * 100) + (int) (sigma * 10);
		long seed2 = marketId * 1000 + (int) (k * 100) + (int) (delta * 10);
		long seed3 = marketId * 1000 + (int) (sigma * 100) + (int) (k * 10);

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
		if (pLost < this.gamma) {
			return null;
		}

		else {

			double n = this.h + getNij(p_ij);

			double mu = k + p_ij * n;
			double z = delayNormalRandom.nextGaussian();
			double ans = z * this.sigma + mu;
			return (int) ans;
		}

	}

	private double getNij(double p_ij) {

		double rnd = this.noiseRandom.nextDouble();
		if (rnd < this.lambda * p_ij) {
			return this.h;
		}
		if (rnd >= this.lambda * p_ij && rnd < (1 - this.lambda * p_ij) * this.lambda_tag) {
			return this.h * delta;
		} else {
			return this.h * delta * delta;

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
