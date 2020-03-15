import java.util.List;

public abstract class FisherSolver {

	protected static final double THRESHOLD = 1E-4;
	protected Market market;
	protected int iterations;
	protected double change;
	protected Utility[][]R;

	
	protected List<FisherData> data;

	public FisherSolver(Market m) {
		this.market=m;
		this.R = m.getR();
		this.iterations = 0;
		this.change = Double.MAX_VALUE;
	}

	public List<FisherData> algorithm() {
		data.add(iterate());
		while (!isStable()) {
			this.iterations = this.iterations + 1;
			data.add(iterate());
		}

		return this.data;
	}
	
	public abstract FisherData iterate();

	public boolean isStable() {
		return change < THRESHOLD;
	}
}
