package SimulatorCreators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Communication.ProtocolDelay;
import Communication.ProtocolDelayEl;
import Communication.ProtocolDown;



public class CreatorDelaysEl extends CreatorDelays{

	
	private  double[] gammas = { 0 }; // aka gamma
	private  double[] sigmas = { 10 };
	private  double[] ks = { 10,25,50,75,100 }; // aka k
	
	
	private  double n1=0;
	private  double n2=0;
	private  double n3=0;
	private  double n4=0;
	
	private  double p1=1;
	private  double p2=0;
	private  double p3=0;
	
	
	
	
	
	
	
	@Override
	protected ProtocolDelay createDefultProtocol() {
		return new ProtocolDelayEl();
	}
	
	@Override
	protected  Collection<? extends ProtocolDelay> createCombinationsDelay(boolean timestampBoolean) {
		List<ProtocolDelay> ans = new ArrayList<ProtocolDelay>();
		// ----For el delay

		for (double gamma : gammas) {
			for (double sigma : sigmas) {
				for (double k : ks) {
					ans.add(new ProtocolDelayEl(timestampBoolean, gamma, sigma, k, n1,n2,n3,n4,p1,p2,p3));
				} // k
			} // sigma
		} // gamma
		return ans;
	}
	
	public String header() {
		return super.header()+",gamma,sigma,k,n1,n2,n3,n4,p1,p2,p3"; 
		
	}
}
