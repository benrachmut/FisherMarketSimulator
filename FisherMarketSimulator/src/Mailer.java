
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Mailer {
	private List<Message> messageBox;
	private CommunicationProtocol communitcationProtocol;
	
	
	
	// private double p3;
	// private double p4;
	// private int delayUb;
	// private Random randomDelay;
	// private RandomNumberGenerator randomMaker;
	// private double[][] parameters;
	// private int currentParameter;
	// private List<Double> delays;

	/*
	 * public Mailer(int distributionType, int parameter, double[][] parametersss) {
	 * 
	 * //randomMaker = RandomNumberGenerator.;
	 * 
	 * if (distributionType == 1) { randomMaker = RandomNumberGenerator.Uniform; }
	 * if (distributionType == 2) { randomMaker = RandomNumberGenerator.Exponential;
	 * }
	 * 
	 * //this.currentParameter = parameter; //randomDelay = new Random(parameter);
	 * //this.parameters = parametersss; //delays = new ArrayList<Double>();
	 * 
	 * }
	 */

	public Mailer(CommunicationProtocol cp) {
		this.messageBox = new ArrayList<Message>();
		this.communitcationProtocol = cp;
	}

	/*
	 * public void updateParameters(double [][] input) { this.parameters = input; }
	 */

	public void setSeeds(long s) {
		this.communitcationProtocol.setSeeds(s);
	}

	/*
	 * public void updateParameter(int parameter) { this.currentParameter =
	 * parameter; }
	 */
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

	/*
	 * private int createDelay(int buyerId, int goodId) {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * double parameter = this.parameters[buyerId][goodId]; if (parameter == 0 ) {
	 * this.delays.add(0.0); return 0; } int delay =
	 * (int)this.randomMaker.getRandom(this.randomDelay, parameter);
	 * this.delays.add((double)delay); return delay; }
	 */

	/*
	 * private static int getRandomInt(Random r, int min, int max) { return
	 * r.nextInt(max - min + 1) + min; }
	 */

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
	/*
	 * public void restart(long i) { this.messageBox= new ArrayList<Message>();
	 * this.updateSeeds(i); }
	 */

	public void setParameterMatrix(double[][] intput) {
		parameters = intput;
	}

	public int getParameter() {
		// TODO Auto-generated method stub
		return currentParameter;
	}
	/*
	 * public int getDistributionDelay() { // TODO Auto-generated method stub return
	 * MainSimulator.distributionDelayType; }
	 */

	/*
	 * public int getDistributionParameter() { // TODO Auto-generated method stub
	 * return MainSimulator.distributionParameterType; }
	 */

	public void printMailBox() {
		for (int i = 0; i < this.messageBox.size(); i++) {
			System.out.println("index: " + i + ", message: " + messageBox.get(i));
		}
	}

	public List<Double> getDelays() {
		// TODO Auto-generated method stub
		return this.delays;
	}

	/*
	 * public double getP3() { return this.p3; } public double getP4() { return
	 * this.p4; } public int getUB() { return this.delayUb; }
	 */
}
