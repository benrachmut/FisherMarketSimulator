package Communication;
import java.util.Random;

public abstract class ProtocolDelay {
	
	protected boolean perfectCommunication;
	protected boolean isTimeStamp;
	

	public ProtocolDelay(boolean perfectCommunication, boolean isTimeStamp) {
		
		this.perfectCommunication = perfectCommunication;
		this.isTimeStamp = isTimeStamp;
	}
	
	public abstract Integer createDelay(double q_ij, double p_ij);
	abstract public void setSeeds(int marketId);

	public boolean isWithTimeStamp() {
		// TODO Auto-generated method stub
		return isTimeStamp;
	}

	public  static String header() {
		return "Perfect Communication, Time Stamp Use";
	}

	

}
