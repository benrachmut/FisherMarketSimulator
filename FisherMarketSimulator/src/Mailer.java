

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class Mailer {
	private List<Message> messageBox;
	private double p3;
	private double p4;
	private int delayUb;
	private Random rP3,rP4,delayUbR;

	public Mailer( double p3, double p4, int delayUb) {
		this.p3 = p3;
		this.p4 = p4;
		this.delayUb = delayUb;			
		this.messageBox= new ArrayList<Message>();
	}

	public void updateSeeds(long s) {
		this.rP3= new Random(s+100);
		this.rP4= new Random(s+200);
		this.delayUbR.setSeed(s+300);
	}
	
	
	public void createMessage(Messageable sender, int decisionCounter, Messageable reciever, double context) {
		int delay = createDelay();
		Message m = new Message(sender,decisionCounter,reciever, context, delay);
		this.messageBox.add(m);	
	}

	
	
	
	private int createDelay() {
		int rndDelay;
		rndDelay = 0;	
		double rnd = rP3.nextDouble();
		if (rnd < this.p3) {
			rndDelay =getRandomInt(this.delayUbR, 1, this.delayUb);
			rnd = rP4.nextDouble();
			if (rnd < this.p4) {
				rndDelay = Integer.MAX_VALUE;
			}
		}
		return rndDelay;
	}
	
	
	private static int getRandomInt(Random r, int min, int max) {
		return r.nextInt(max - min + 1) + min;
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

	public void emptyMessageBox() {
		this.messageBox = new ArrayList<Message>();
		
	}

	public void restart(long i) {		
		this.messageBox= new ArrayList<Message>();
		this.updateSeeds(i);
	}
}
