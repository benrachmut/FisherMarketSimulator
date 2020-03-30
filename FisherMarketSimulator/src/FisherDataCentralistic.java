
public class FisherDataCentralistic extends FisherData {

	public FisherDataCentralistic(Double[][] X, Utility[][] rUtil, int iterations, Market market) {
		super(X, rUtil, iterations, market, "centralistic");
		
	}

	public FisherDataCentralistic(FisherData copiedFisherData, double iterationOfCopied) {
		super(copiedFisherData,iterationOfCopied);
	}

	public FisherDataCentralistic(int idF, int numByuersF, int numGoodsF, double iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF,  double avgRX, double envyFree) {
		
		
		super( idF,  numByuersF,  numGoodsF,  iterationF,  algoF,
				 considerDecisionCounterF,  maxIterationF,   avgRX, envyFree);
	}

}
