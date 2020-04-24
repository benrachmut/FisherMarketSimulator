package Communication;
import java.util.Random;

public abstract class ProtocolDown {

	private boolean isDown;
	
	
	
	public ProtocolDown(boolean isDown) {
		super();
		this.isDown = isDown;
	}
	public static String header() {
		// TODO Auto-generated method stub
		return "Is Down";
	}
	@Override
	public String toString() {
		return Boolean.toString(isDown);
	}
	public abstract void setSeeds(int marketId);
	
	/*
	private double downSparsity;
	private int downK;
	private double downInf, downNumIterProb,downNumIterParameter;
	private boolean copySparsityProb, perfectFitNeverDown;
	
	
	//private Random downInfRand, downNumIterRand,downSparsityRand;
	
	public ProtocolDown(double downSparsity, int downK, double downInf, double downNumIterProb,
			double downNumIterParameter, boolean copySparsityProb) {
		super();
		this.downSparsity = downSparsity;
		this.downK = downK;
		this.downInf = downInf;
		this.downNumIterProb = downNumIterProb;
		this.downNumIterParameter = downNumIterParameter;
		this.copySparsityProb = copySparsityProb;
		this.perfectFitNeverDown = false;
	}
	
	
	public ProtocolDown() {
		perfectFitNeverDown = true;
	}
	
	public void setSeed(long marketId) {
		
		long seed1 = marketId*10000+ (int)(downSparsity*1000)+ (int)(downNumIterParameter*100);
		long seed2 = marketId*10000+ (int)(downInf*1000)+ (int)(downInf*100);
		long seed3 = marketId*10000+ (int)(downNumIterProb*1000)+ (int)(downK*100);

		downInfRand = new Random(seed1); 
		downNumIterRand= new Random(seed2);
		downSparsityRand = new Random(seed3);
	}
	
	

	
	
	@Override
	public String toString() {
		String spars;	
		if (copySparsityProb) {
			spars = downSparsity+",";
		}else {
			spars = "-1"+",";
		}
		String ans = 
		spars+
		downK +","+
		downInf+","+
		downNumIterProb+","+
		downNumIterParameter;
		return ans;
	}
	
	public static String header() {
		String ans =
		"downSparsity"+","+
		"downK" +","+
		"downInf"+","+
		"downNumIterProb"+","+
		"downNumIterParameter";
		return ans;
	}


	public boolean isPerfectFitNeverDown() {
		return this.perfectFitNeverDown;
	}

	public double downSparseProb() {
		return this.downSparsity;
	}
	public Random downSparseRand() {
		return this.downSparsityRand;
	}

	public boolean isCopyFromConstantSparse() {
		// TODO Auto-generated method stub
		return this.copySparsityProb;
	}
	*/
	
	
	
}
