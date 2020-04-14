
public class CommunicationProtocol {
	
	CommunicationProtocolDelay delay;
	CommunicationProtocolDown down;
	
	public CommunicationProtocol(CommunicationProtocolDelay delay, CommunicationProtocolDown down) {
		super();
		this.delay = delay;
		this.down = down;
	}
	
	public void setSeeds(long seed) {
		this.delay.setSeed(seed);
		this.down.setSeed(seed);
	}
	@Override
	public String toString() {
		return delay.toString()+","+down.toString();
	}
	public static String header(){
		return CommunicationProtocolDelay.header()+","+CommunicationProtocolDown.header();
	}
	
	
	
	
}
