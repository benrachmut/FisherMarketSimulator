import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FisherSolverDistributed extends FisherSolver {

	private Mailer mailer;
	private List<Good> goods;
	private List<Buyer> buyers;
	private Double[][] allocation;

	public FisherSolverDistributed(Market m) {
		super(m);
		this.mailer = m.getMailer();
		this.goods = m.getGoods();
		this.buyers = m.getBuyers();
		/*
		 * for (Good g: this.goods) { g.resetGoodsBetweenRuns(); } for (Buyer b:
		 * this.buyers) { b.resetBuyerBetweenRuns(); }
		 */
	}

	private Double[][] createCentralisticAllocation() {
		Double[][] ans = new Double[this.buyers.size()][goods.size()];

		for (int j = 0; j < goods.size(); j++) {
			Good t = goods.get(j);
			Map<Buyer, Double> jAllocation = t.getAllocation();
			for (Entry<Buyer, Double> e : jAllocation.entrySet()) {
				Buyer p = e.getKey();
				int i = findI(p);
				double allocation = e.getValue();
				ans[i][j] = allocation;
			}
		}

		return ans;
	}

	private int findI(Buyer p) {
		int ans = -1;
		for (int i = 0; i < buyers.size(); i++) {
			if (p.equals(buyers.get(i))) {
				ans = i;
			}
		}
		return ans;
	}

	public FisherData iterate() {
		List<Message> msgToSend = mailer.handleDelay();
		Map<Messageable, List<Message>> receiversMap = createReciversMap(msgToSend);
		sendMessages(receiversMap);
		updateStability();
		this.allocation = createCentralisticAllocation();
		FisherData = new DistrbutedData(this.allocation, this.R, this.iterations,);
		return allocation;
	}

	private void updateStability() {
		this.change = 0;
		for (Good t : goods) {
			this.change = this.change + t.getGoodChanges();
			;
		}
	}

	private void sendMessages(Map<Messageable, List<Message>> receiversMap) {
		for (Entry<Messageable, List<Message>> e : receiversMap.entrySet()) {
			Messageable reciever = e.getKey();
			List<Message> msgsRecieved = e.getValue();
			reciever.recieveMessage(msgsRecieved);
		}
	}

	private Map<Messageable, List<Message>> createReciversMap(List<Message> msgToSend) {
		Map<Messageable, List<Message>> ans = new HashMap<Messageable, List<Message>>();
		for (Message m : msgToSend) {
			Messageable reciever = m.getReciever();
			if (!ans.containsKey(reciever)) {
				List<Message> l = new ArrayList<Message>();
				ans.put(reciever, l);
			}
			ans.get(reciever).add(m);
		}
		return ans;
	}

	// next iteration: calculates prices, calculates current valuation and
	// update the bids

	/*
	 * public Double[][] iterate() { updateBidsUsingValutations();
	 * updatePriceVectorUsingBids(); updateCurrentChanges();
	 * updateCurrentAllocationMatrix();
	 * 
	 * //updateCurrentAllocationMatrixAndChanges(); //generateAllocations(); return
	 * currentAllocation; }
	 */

	// -----------METHODS OF iterate------

	// -----------METHODS OF updateCurrentAllocationMatrixAndChanges------

	// -----------Getters and Object methods------

	public Double[][] getAllocations() {
		return allocation;
	}

}
