import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Mailer {
	
	private CommunicationProtocolDelay delay;
	private CommunicationProtocolDown down;
	private List<Message> messageBox;
	

	public Mailer(CommunicationProtocolDelay delay, CommunicationProtocolDown down) {
		super();
		this.delay = delay;
		this.down = down;
		this.messageBox = new ArrayList<Message>();

	}
	
	public void setSeeds(long marketId) {
		emptyMessageBox();
		this.delay.setSeed(marketId);
		this.down.setSeed(marketId);
	}
	
	@Override
	public String toString() {
		return delay.toString()+","+down.toString();
	}
	public static String header(){
		return CommunicationProtocolDelay.header()+","+CommunicationProtocolDown.header();
	}
	
	
	public void createMessage(Messageable sender, int decisionCounter, Messageable reciever, double context) {
		int buyerId, goodId;
		if (sender instanceof Buyer) {
			buyerId = sender.getId();
			goodId = reciever.getId();
		} else {// (sender instanceof Good) {
			buyerId = reciever.getId();
			goodId = sender.getId();
		}

		int delay = createDelay(buyerId, goodId);
		Message m = new Message(sender, decisionCounter, reciever, context, delay);
		this.messageBox.add(m);
	}
	
	public List<Message> handleDelay() {
		Collections.sort(this.messageBox);
		List<Message> msgToSend = new ArrayList<Message>();
		Iterator it = this.messageBox.iterator();

		while (it.hasNext()) {
			Message msg = (Message) it.next();
			if (msg.getDelay() == 0) {
				msgToSend.add(msg);
				it.remove();
			} else {
				msg.setDelay(msg.getDelay() - 1);
			}
		}
		return msgToSend;
	}

	private void emptyMessageBox() {
		this.messageBox = new ArrayList<Message>();

	}
	
	public void printMailBox() {
		for (int i = 0; i < this.messageBox.size(); i++) {
			System.out.println("index: " + i + ", message: " + messageBox.get(i));
		}
	}
/*
	public List<Double> getDelays() {
		// TODO Auto-generated method stub
		return this.delays;
	}
	*/

	public boolean isPerfectCommunication() {
		// TODO Auto-generated method stub
		return this.delay.isPerfectCommunication();
	}
	
	public double getDealyConstantSparsityProb() {
		return this.delay.getDealyConstantSparsity();
	}
	
	public Random getDelayConstantSparsityRand() {
		return this.delay.getDelayConstantSparsityRand();
	}

	public double getDealyNoiseSparsityProb() {
		// TODO Auto-generated method stub
		return this.delay.getDealyNoiseSparsity();
	}
	
	public Random getDelayNoiseSparsityRand() {
		return this.delay.getDelayNoiseSparsityRand();
	}

	public boolean perfectFitNeverDown() {
		// TODO Auto-generated method stub
		return this.down.isPerfectFitNeverDown();
	}

	public double getDownSparseProb() {
		// TODO Auto-generated method stub
		return this.down.downSparseProb();
	}

	public Random getDownSparseRand() {
		// TODO Auto-generated method stub
		return this.down.downSparseRand();
	}

	public boolean isCopyFromConstantSparse() {
		// TODO Auto-generated method stub
		return this.down.isCopyFromConstantSparse();
	}

	public boolean isWithTimeStamp() {
		// TODO Auto-generated method stub
		return this.delay.isWithTimeStamp();
	}
	
	
	
}
