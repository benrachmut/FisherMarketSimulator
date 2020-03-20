
public class FisherDataCentralistic extends FisherData {

	public FisherDataCentralistic(Double[][] X, Utility[][] rUtil, int iterations, Market market) {
		super(X, rUtil, iterations, market, "centralistic");
		
	}

	public FisherDataCentralistic(FisherData copiedFisherData, int iterationOfCopied) {
		super(copiedFisherData,iterationOfCopied);
	}

	public FisherDataCentralistic(int idF, int numByuersF, int numGoodsF, int iterationF, String algoF,
			boolean considerDecisionCounterF, int maxIterationF, double avgR, double avgX, double avgRX) {
		super( idF,  numByuersF,  numGoodsF,  iterationF,  algoF,
				 considerDecisionCounterF,  maxIterationF,  avgR,  avgX,  avgRX);
	}

}
