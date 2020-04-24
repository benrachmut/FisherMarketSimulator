package SimulatorCreators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Communication.ProtocolDelay;
import Communication.ProtocolDelayEl;
import Communication.ProtocolDown;



public class CreatorDelaysEl extends CreatorDelays{

	
	private  double[] gammas = { 0.02 }; // aka gamma
	private  double[] sigmas = { 10 };
	private  double[] ks = { 10 }; // aka k
	private  double[] hs = { 5 };
	private  double[] deltas = { 10 };
	private  double[] lambdas = { 0.5 };
	private  double[] lambda_tags = { 0.66 };
	
	
	
	
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
					for (double h : hs) {
						for (double delta : deltas) {
							for (double lambda : lambdas) {
								for (double lambda_tag : lambda_tags) {
									ans.add(new ProtocolDelayEl(timestampBoolean, gamma, sigma, k, h, delta,
											lambda, lambda_tag));
								}
							} // lambda
						} // delta
					} // h
				} // k
			} // sigma
		} // gamma
		return ans;
	}
	
	public String header() {
		return super.header()+",gamma,sigma,k,h,delta,lambda,lambda_tag"; 
	}
}
