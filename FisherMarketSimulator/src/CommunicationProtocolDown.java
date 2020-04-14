import java.util.Random;

public class CommunicationProtocolDown {
	private double downSparsity;
	private int downK;
	private double downInf, downNumIterProb,downNumIterParameter;
	private boolean copySparsityProb;
	private Random downInfRand, downNumIterRand;
	
	public CommunicationProtocolDown(double downSparsity, int downK, double downInf, double downNumIterProb,
			double downNumIterParameter, boolean copySparsityProb) {
		super();
		this.downSparsity = downSparsity;
		this.downK = downK;
		this.downInf = downInf;
		this.downNumIterProb = downNumIterProb;
		this.downNumIterParameter = downNumIterParameter;
		this.copySparsityProb = copySparsityProb;
	}
	
	public CommunicationProtocolDown() {
		// TODO Auto-generated constructor stub
	}
	
	public void setSeed(long seed) {
		downInfRand = new Random(seed); 
		downNumIterRand= new Random(seed);
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

	public double getDownSparsity() {
		return downSparsity;
	}

	public void setDownSparsity(double downSparsity) {
		this.downSparsity = downSparsity;
	}

	public int getDownK() {
		return downK;
	}

	public void setDownK(int downK) {
		this.downK = downK;
	}

	public double getDownInf() {
		return downInf;
	}

	public void setDownInf(double downInf) {
		this.downInf = downInf;
	}

	public double getDownNumIterProb() {
		return downNumIterProb;
	}

	public void setDownNumIterProb(double downNumIterProb) {
		this.downNumIterProb = downNumIterProb;
	}

	public double getDownNumIterParameter() {
		return downNumIterParameter;
	}

	public void setDownNumIterParameter(double downNumIterParameter) {
		this.downNumIterParameter = downNumIterParameter;
	}

	public boolean isCopySparsityProb() {
		return copySparsityProb;
	}

	public void setCopySparsityProb(boolean copySparsityProb) {
		this.copySparsityProb = copySparsityProb;
	}
	
	
	
	
}
