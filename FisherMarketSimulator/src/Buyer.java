import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;


public class Buyer implements Messageable , Comparable<Buyer> {

	private int id;
	private SortedMap <Good,Utility> utilitiesMap;
	private Random randomUtil;
	private Mailer mailer;
	private int decisionCounter;
	private SortedMap<Good, Message> messageRecived;
	private SortedMap<Good, Double> bidsMap;
	private SortedMap<Good, Double> updatedUtilitiesMap;
	//private HashMap<Good, Utility> utilitiesMap;
	private SortedSet<Good> goodsResponsibility;

	
	public Buyer(int i, List<Good> goods, Random r) {
		this.id=i;
		this.randomUtil = r;
		this.utilitiesMap = createUtils(goods);
		this.goodsResponsibility=new TreeSet<Good>();

	}
	public int getGoodsResponsibilitySize() {
		return goodsResponsibility.size();
	}
	
	public boolean addToGoodsResponsibility(Good g) {
		return goodsResponsibility.add(g);
	}


	public SortedMap<Good, Utility> createUtils(List<Good> goods) {
		SortedMap<Good, Utility> ans = new TreeMap<Good, Utility>();
		for (int j = 0; j < goods.size(); j++) {
			Good g = goods.get(j);	
			int goodId = g.getId();
			int goodType = g.getType();
			double value =MainSimulator.getRandomNorm(goodType, randomUtil);
			Utility util = new UtilityLinear(this,g,value);
			ans.put(g, util);
		}
		return ans;
		
	}


	public Utility getUtility(int j) {
		for (Good g : utilitiesMap.keySet()) {
			if (g.getId()==j) {
				return utilitiesMap.get(g);
			}
		}
		return null;
	}





	@Override
	public void updateMailer(Mailer mailer) {
		this.mailer=mailer;
		
	}
	
	
	
	public void resetBuyerBetweenRuns() {
		initMapsInVariables();
		placeUtilitiesInMap();
		placeBidsWithUpdatedUtilityInMap();	
		sendBidsToGoods();
	}

	private void initMapsInVariables() {
		this.decisionCounter= 0;
		this.messageRecived = new TreeMap<Good, Message>();
		this.bidsMap = new TreeMap<Good, Double>();
		//this.utilitiesMap = new HashMap<Good, Utility>();
		this.updatedUtilitiesMap = new TreeMap<Good, Double>();

	}

	
	private void sendBidsToGoods() {	
		for (Entry<Good, Double> e : bidsMap.entrySet()) {
			Good good = e.getKey();
			Double bid = e.getValue();
			this.createMessage(good,bid);
		}
	}



	private void placeBidsWithUpdatedUtilityInMap() {
		double sumUtilities = calcSumUtils();
		for (Entry<Good, Double> e : updatedUtilitiesMap.entrySet()) {
			Good task = e.getKey();
			Double updetedUtility  = e.getValue();
			Double bid = updetedUtility/sumUtilities;
			this.bidsMap.put(task,bid);
		}	
	}

	private double calcSumUtils() {
		double ans = 0.0;
		for (Double d: updatedUtilitiesMap.values()) {
			ans = ans+ d;
		}
		return ans;
	}

	private void placeUtilitiesInMap() {
	
		for (Good task : this.utilitiesMap.keySet()) {
			
			Utility u = this.utilitiesMap.get(task);
			
			//this.utilitiesMap.put(task, u);
			this.updatedUtilitiesMap.put(task,u.getUtility(1));
		}
	}

	

	private void updateUtilitesUsingAllocation(List<Message> msgs) {
		
		for (Message m : msgs) {
			boolean ignoreMessage = shouldIgnore(m);	
		//-------- extract info from msg	
			Messageable sender= m.getSender();
			checkIfBug(sender);
			Good t = (Good)sender;

			if (!ignoreMessage || !MainSimulator.considerDecisionCounter) {		
				this.messageRecived.put(t, m);
				double allocation  = m.getContext();
				Utility u = utilitiesMap.get(t);
				Double updatedUtility= u.getUtility(allocation);
				this.updatedUtilitiesMap.put(t, updatedUtility);
			}
		}
		
	}

	private boolean shouldIgnore(Message m) {
		Messageable sender= m.getSender();
		checkIfBug(sender);
		Good t = (Good)sender;
		int messageDecisionCounter = m.getDecisionCounter();
		if (!messageRecived.containsKey(t)) {
			return false;
		}else {
			
			int currentMessageDecisionCounter = messageRecived.get(t).getDecisionCounter();
			if (currentMessageDecisionCounter < messageDecisionCounter) {
				return false;
			}else {
				return true;
			}	
		}

	}

	private void checkIfBug(Messageable sender) {
		if (!(sender instanceof Good)) {
			System.err.println("logical bug in creating message in task");
		}
		
	}

	@Override
	public void createMessage(Messageable task, double bid) {
		this.mailer.createMessage(this, this.decisionCounter, task, bid);
	}


	


	
	@Override
	public void recieveMessage(List<Message> msgs) {
		this.decisionCounter++;
		updateUtilitesUsingAllocation(msgs);
		placeBidsWithUpdatedUtilityInMap();	
		sendBidsToGoods();
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "b"+this.id;
	}
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	

	@Override
	public int compareTo(Buyer o) {
		// TODO Auto-generated method stub
		return this.id - o.getId();
	}


	public Map<Good,Double> getBids() {
		// TODO Auto-generated method stub
		return this.bidsMap;
	}


	public Map<Good, Double> getCurrentUtil() {
		// TODO Auto-generated method stub
		return this.updatedUtilitiesMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Buyer) {
			return this.id == ((Buyer)obj).getId();
		}
		else {
			return false;
		}
	
	}
	
	
	
/*
	public Map<Good, Double> getAllocation() {
		Map<Good, Double> ans = new HashMap<Good, Double>();
		for (Entry<Good, Message> e : this.messageRecived.entrySet()) {
			ans.put(e.getKey(), e.getValue().value);
		}
		
		messageRecived;


		return ;
	}
	*/

	
	

}
