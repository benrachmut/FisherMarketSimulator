package SimulatorCreators;

import java.util.Collection;

import Communication.ProtocolDown;
import Communication.ProtocolDownEl;

public class CreatorDownsEl extends CreatorDowns{

	@Override
	protected Collection<? extends ProtocolDown> createCombinationsDown() {
		System.err.println("For now el does not have protocol down");
		return null;
	}

	@Override
	protected ProtocolDown createDefultProtocol() {
		return new ProtocolDownEl();
	}

}
