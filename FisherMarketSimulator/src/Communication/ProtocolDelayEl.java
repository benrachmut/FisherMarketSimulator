package Communication;

import java.util.Random;

public class ProtocolDelayEl extends ProtocolDelay {
	//public static double counterAll; //  for debug
	//public static double counterGamma;// for debug
	private double gamma; // aka gamma
	private  double sigma;
	private  double k; // aka k
	private  double n1;
	private  double n2;
	private  double n3;
	private  double n4;
	
	private  double p1;
	private  double p2;
	private  double p3;
	
	private Random msgLostRandom, delayNormalRandom, noiseRandom;


	public ProtocolDelayEl( boolean isTimeStamp, double gamma, double sigma, double k,
			double n1, double n2, double n3, double n4, double p1, double p2, double p3) {
		super(false, isTimeStamp);
		this.gamma = gamma;
		this.sigma = sigma;
		this.k = k;
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.n4 = n4;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}


	
	public ProtocolDelayEl() {
		super(true, true);
		gamma=0; // aka gamma
		sigma=0;
		k=0; // aka k
		
		n1=0;
		n2=0;
		n3=0;
		n4=0;
		
		p1=0;
		p2=0;
		p3=0;

	}
	
	
	@Override
	public String toString() {
		
		String ans = 
		super.toString()+","+
		this.gamma+","+
		this.sigma+","+
		this.k+","+
		
		this.n1+","+
		this.n2+","+
		this.n3+","+
		this.n4+","+
		
		this.p1+","+
		this.p2+","+
		this.p3;
		
		
		return ans;
	}

	
	
	
	public void setSeeds(int marketId) {
		long seed1 = marketId * 1000 + (int) ((1 - gamma) * 100) + (int) (sigma * 10);
		long seed2 = marketId * 1000 + (int) (k * 100) + (int) (n4 * 10);
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
		//counterAll ++;

		double pLost = msgLostRandom.nextDouble();
		if (pLost < this.gamma) {
		//	counterGamma++;
			return null;
		}

		else {

			double n = getNij();
			double mu = k + p_ij * n;
			double z = delayNormalRandom.nextGaussian();
			double ans = z * this.sigma + mu;
			return (int) ans;
		}

	}

	private double getNij() {

		double rnd = this.noiseRandom.nextDouble();
		
		double prob1 = this.p1;
		double prob2 = this.p1+this.p2;
		double prob3 = this.p1+this.p2+this.p3;
		
		if (rnd < prob1) {
			return n1;
		}
		if (rnd >= prob1 && rnd < prob2) {
			return n2;
		} 
		if (rnd >= prob2 && rnd < prob3) {
			return n3;
		} else {
			return n4;

		}
		
		
	}
	
	// for debug
	public double getGamma(){
		return this.gamma;
	}



}
