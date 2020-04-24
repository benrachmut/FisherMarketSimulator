package SimulatorCreators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Communication.ProtocolDelay;
import Communication.ProtocolDown;

public abstract class CreatorDowns {
	protected boolean[] perfectDown = { true };

	public List<ProtocolDown> createProtocolDowns() {
		List<ProtocolDown> ans = new ArrayList<ProtocolDown>();
		for (boolean perfectP : perfectDown) {
			if (perfectP == true) {
				ans.add(createDefultProtocol());
			} else {
				ans.addAll(createCombinationsDown());

			}
		}
		return ans;
	}

	protected abstract Collection<? extends ProtocolDown> createCombinationsDown() ;

	protected abstract ProtocolDown createDefultProtocol() ;
	
	public String header() {
		return "Perfect Down";
	}
}
