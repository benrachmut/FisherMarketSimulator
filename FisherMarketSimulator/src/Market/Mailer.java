package Market;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Communication.CommunicationProtocolDelay;
import Communication.ProtocolDown;
import Communication.Message;
import Communication.Messageable;
import Communication.ProtocolDelay;

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
	public static String header(){
		return CommunicationProtocolDelay.header()+","+ProtocolDown.header();
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
/*
	public List<Double> getDelays() {
		// TODO Auto-generated method stub
		return this.delays;
	}
	*/
/*
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
	
	*/
	
}
