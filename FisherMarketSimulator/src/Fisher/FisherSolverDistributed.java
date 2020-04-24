package Fisher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Communication.Message;
import Communication.Messageable;
import Market.Buyer;
import Market.Good;
import Market.Mailer;
import Market.Market;

import java.util.SortedMap;
import java.util.TreeMap;

public class FisherSolverDistributed extends FisherSolver {

	private Mailer mailer;
	private List<Good> goods;
	private List<Buyer> buyers;
	private Double[][] allocation;

	public FisherSolverDistributed(Market m, Mailer mailer,int maxIteration, double threshold) {
		super(m, maxIteration, threshold);
		this.goods = m.getGoods();
		this.buyers = m.getBuyers();
		this.algorithm();
		
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

	public Double[][] iterate() {
		//System.out.println("______" + iterations + "______");
		//mailer.printMailBox();
		
		List<Message> msgToSend = mailer.handleDelay();
		sendMessages(msgToSend);
		this.allocation = createCentralisticAllocation();
		updateChange();
		//FisherData ans = new FisherDataDistributed(this.allocation, this.R, this.iterations, this.market, this.mailer);
		return this.allocation;
	}

	private void sendMessages(List<Message> msgToSend) {
		SortedMap<Buyer, List<Message>> recieversMapBuyer = createReciversMapBuyer(msgToSend);
		sendMessagesBuyers(recieversMapBuyer);
		SortedMap<Good, List<Message>> recieversMapGood = createReciversMapGood(msgToSend);
		sendMessagesGoods(recieversMapGood);
	}

	private void sendMessagesGoods(SortedMap<Good, List<Message>> recieversMapGood) {
		for (Entry<Good, List<Message>> e : recieversMapGood.entrySet()) {
			Messageable reciever = e.getKey();
			List<Message> msgsRecieved = e.getValue();
			reciever.recieveMessage(msgsRecieved);
		}
	}

	private SortedMap<Good, List<Message>> createReciversMapGood(List<Message> msgToSend) {
		
		SortedMap<Good, List<Message>> ans = new TreeMap<Good, List<Message>>();
		for (Message m : msgToSend) {
			Messageable reciever = m.getReciever();
			if (reciever instanceof Good) {
				if (!ans.containsKey(reciever)) {
					List<Message> l = new ArrayList<Message>();
					ans.put((Good) reciever, l);
				}
				ans.get((Good) reciever).add(m);
			}
		}
		return ans;
	}

	private void sendMessagesBuyers(SortedMap<Buyer, List<Message>> recieversMapBuyer) {
		for (Entry<Buyer, List<Message>> e : recieversMapBuyer.entrySet()) {
			Messageable reciever = e.getKey();
			List<Message> msgsRecieved = e.getValue();
			reciever.recieveMessage(msgsRecieved);
		}

	}

	private SortedMap<Buyer, List<Message>> createReciversMapBuyer(List<Message> msgToSend) {
		SortedMap<Buyer, List<Message>> ans = new TreeMap<Buyer, List<Message>>();

		for (Message m : msgToSend) {
			Messageable reciever = m.getReciever();
			if (reciever instanceof Buyer) {
				if (!ans.containsKey(reciever)) {
					List<Message> l = new ArrayList<Message>();
					ans.put((Buyer) reciever, l);
				}
				ans.get((Buyer) reciever).add(m);
			}
		}
		return ans;
	}
/*
	private void printMsgsToSend(List<Message> msgToSend) {
		int count = 0;
		for (Message message : msgToSend) {

			System.out.println("message number: " + count + ", " + message);
			count++;
		}

	}
*/
	private void updateChange() {
		double ans = 0.0;
		for (Good g : this.goods) {
			double gChange = g.getGoodChanges();
			if (gChange == -1) {
				this.change = Double.MAX_VALUE;
				return;
			}
			ans += gChange;
		}
		this.change = ans;

	}
/*
	private void updateStability() {
		this.change = 0;
		for (Good t : goods) {
			this.change = this.change + t.getGoodChanges();
		}
	}
*/
	/*
	private void sendMessages(Map<Messageable, List<Message>> receiversMap) {
		for (Entry<Messageable, List<Message>> e : receiversMap.entrySet()) {
			Messageable reciever = e.getKey();
			List<Message> msgsRecieved = e.getValue();
			reciever.recieveMessage(msgsRecieved);
		}
	}
*/

	/*
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
*/
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

	@Override
	protected double[][] getBids() {
		double[][] ans = new double[this.buyers.size()][this.goods.size()];
		Collections.sort(this.buyers);
		for (int i = 0; i < buyers.size(); i++) {
			Map<Good, Double> bidsI = buyers.get(i).getBids();
			for (Entry<Good, Double> e : bidsI.entrySet()) {
				Good g = e.getKey();
				int id = g.getId();
				ans[i][id] = e.getValue();
			}

		}
		return ans;
	}

	@Override
	protected double[][] getCurrentUtil() {
		double[][] ans = new double[this.buyers.size()][this.goods.size()];
		Collections.sort(this.buyers);
		for (int i = 0; i < buyers.size(); i++) {
			Map<Good, Double> utilI = buyers.get(i).getCurrentUtil();
			for (Entry<Good, Double> e : utilI.entrySet()) {
				Good g = e.getKey();
				int id = g.getId();
				ans[i][id] = e.getValue();
			}

		}
		return ans;
	}

	@Override
	protected double[][] getAllocation() {
		double[][] ans = new double[this.buyers.size()][this.goods.size()];
		Collections.sort(this.goods);
		for (int j = 0; j < goods.size(); j++) {
			Map<Buyer, Double> bidsJ = goods.get(j).getAllocation();

			for (Entry<Buyer, Double> e : bidsJ.entrySet()) {
				Buyer g = e.getKey();
				int id = g.getId();
				ans[id][j] = e.getValue();
			}

		}
		return ans;
	}

	@Override
	protected double[] getPricers() {
		Collections.sort(this.goods);
		double[] ans = new double[this.goods.size()];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = this.goods.get(i).getPrice();
		}
		return ans;
	}

}
