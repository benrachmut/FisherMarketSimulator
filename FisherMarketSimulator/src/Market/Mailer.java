package Market;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Communication.ProtocolDown;
import SimulatorCreators.MainSimulator;
import Communication.Message;
import Communication.Messageable;
import Communication.ProtocolDelay;
import Communication.ProtocolDelayEl;

public class Mailer {
	
	private ProtocolDelay delay;
	private ProtocolDown down;

	private List<Message> messageBox;
	private int[][] impCommmunicationMatrix;
	

	public Mailer(ProtocolDelay delay, ProtocolDown down) {
		super();
		this.delay = delay;
		this.down = down;
		this.messageBox = new ArrayList<Message>();

	}
	
	public void resetMailer(int marketId, int[][]impComMatrix) {
		this.messageBox = new ArrayList<Message>();
		this.impCommmunicationMatrix = impComMatrix;
		this.delay.setSeeds(marketId);
		this.down.setSeeds(marketId);
	}
	
	@Override
	public String toString() {
		return delay.toString()+","+down.toString();
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
	
		
		int q_ij =  this.impCommmunicationMatrix[buyerId][goodId];
		double p_ij = 1.0;
		try {
			Integer randomDelay = delay.createDelay(q_ij,p_ij);
			Message m = new Message(sender, decisionCounter, reciever, context, randomDelay);
			this.messageBox.add(m);
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
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

	
	
	public void printMailBox() {
		for (int i = 0; i < this.messageBox.size(); i++) {
			System.out.println("index: " + i + ", message: " + messageBox.get(i));
		}
	}
	
	public boolean isWithTimeStamp() {
		return this.delay.isWithTimeStamp();
	}
}
