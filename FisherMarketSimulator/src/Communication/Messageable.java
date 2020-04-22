package Communication;
import java.util.List;

import Market.Mailer;

public interface Messageable {

	public void recieveMessage(List<Message> msgs);
	public void createMessage(Messageable reciver, double context);
	public void updateMailer(Mailer mailer);
	public int getId();

}
