import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class Good implements Messageable, Comparable<Good> {

	private int type;
	private int id;
	private Mailer mailer;
	private SortedMap<Buyer, Double> bidsRecieved;
	private int decisionCounter;
	private SortedMap<Buyer, Message> messageRecived;
	private double goodChange;
	private SortedMap<Buyer, Double> allocation;

	public Good(int type, int i) {
		this.type = type;
		this.id = i;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "g" + this.id;
	}

	public int getType() {
		return this.type;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public void updateMailer(Mailer mailer) {
		this.mailer = mailer;

	}

	public void resetGoodsBetweenRuns() {
		this.bidsRecieved = new TreeMap<Buyer, Double>();
		this.decisionCounter = 0;
		this.messageRecived = new TreeMap<Buyer, Message>();
		this.goodChange = -1;
		this.allocation = new TreeMap<Buyer, Double>();

	}

	public Map<Buyer, Double> getAllocation() {
		return this.allocation;
	}

	@Override
	public void recieveMessage(List<Message> msgs) {
		this.decisionCounter++;
		updateBidsInMap(msgs);
		double price = calculatePrice();
		Map<Buyer, Double> lastAllocation = this.allocation;
		this.allocation = reallocation(price);
		updateChange(lastAllocation);
		sendAllocation(allocation);
	}

	private void sendAllocation(Map<Buyer, Double> allocation) {
		for (Entry<Buyer, Double> e : allocation.entrySet()) {
			Buyer p = e.getKey();
			Double allocationPerBuyer = e.getValue();
			this.createMessage(p, allocationPerBuyer);
		}

	}

	private void updateChange(Map<Buyer, Double> lastAllocation) {

		if (lastAllocation.isEmpty()) {
			this.goodChange = Double.MAX_VALUE;
		} else {
			Double changeToUpdate = 0.0;
			for (Buyer buyer : lastAllocation.keySet()) {
				double currentBuyerAllocation = this.allocation.get(buyer);
				double lastBuyerAllocation = lastAllocation.get(buyer);
				double delta = currentBuyerAllocation - lastBuyerAllocation;
				changeToUpdate += Math.abs(delta);
			}
			this.goodChange = changeToUpdate;
		}

		// double sumAllocation = 0;
		/*
		 * for (Double singleAllocation : allocation.values()) { sumAllocation =
		 * sumAllocation + singleAllocation; }
		 */
		/*
		 * if (this.goodChange == -1) { this.goodChange = Double.MAX_VALUE; } else {
		 * goodChange = 0;
		 * 
		 * for (Entry<Buyer, Double> e : this.allocation.entrySet()) { Buyer b =
		 * e.getKey(); double bid = this.bidsRecieved.get(b); double currentAllocation =
		 * bid/ price; double pastAllocation = e.getValue(); double t= currentAllocation
		 * - pastAllocation; if (t<0) { t=t*(-1); }
		 * 
		 * goodChange += t; }
		 * 
		 * /*
		 * 
		 * change += Math .abs(((bids[i][j]/ prices[j]) -
		 * currentAllocation[i][j]));///aaaa
		 */

	}

	private SortedMap<Buyer, Double> reallocation(double price) {
		SortedMap<Buyer, Double> ans = new TreeMap<Buyer, Double>();
		for (Entry<Buyer, Double> e : this.bidsRecieved.entrySet()) {
			Buyer p = e.getKey();
			Double bid = e.getValue();

			Double resingleAllocation = bid / price;
			if (resingleAllocation > MainSimulator.THRESHOLD) {
				ans.put(p, resingleAllocation);
			} else {
				ans.put(p, 0.0);
			}
		}
		return ans;
	}

	private double calculatePrice() {
		double price = 0;
		for (Double singleBid : bidsRecieved.values()) {
			price = price + singleBid;
		}
		return price;
	}

	private void updateBidsInMap(List<Message> msgs) {
		for (Message m : msgs) {
			boolean ignoreMessage = shouldIgnore(m);
			Messageable sender = m.getSender();
			Buyer p = (Buyer) sender;

			if (!ignoreMessage || !MainSimulator.considerDecisionCounter) {
				this.messageRecived.put(p, m);
				double bid = m.getContext();
				this.bidsRecieved.put(p, bid);
			}
		}

	}

	private boolean shouldIgnore(Message m) {
		Messageable sender = m.getSender();
		Buyer p = (Buyer) sender;
		int messageDecisionCounter = m.getDecisionCounter();
		if (!messageRecived.containsKey(p)) {
			return false;
		} else {

			int currentMessageDecisionCounter = messageRecived.get(p).getDecisionCounter();
			if (currentMessageDecisionCounter < messageDecisionCounter) {
				return false;
			} else {
				return true;
			}
		}

	}

	@Override
	public void createMessage(Messageable p, double allocation) {
		this.mailer.createMessage(this, this.decisionCounter, p, allocation);
	}

	public double getGoodChanges() {
		return this.goodChange;
	}
	/*
	 * public void resetBuyerBetweenRuns() { // TODO Auto-generated method stub
	 * 
	 * }
	 */

	@Override
	public int compareTo(Good o) {
		// TODO Auto-generated method stub
		return this.id - o.getId();
	}

	public double getPrice() {
		// TODO Auto-generated method stub
		return this.calculatePrice();
	}
}
