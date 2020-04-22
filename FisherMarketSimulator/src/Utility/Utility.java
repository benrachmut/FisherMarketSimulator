package Utility;
import Market.Buyer;
import Market.Good;

public abstract class Utility {

	Buyer buyer;
	Good good;
	
	public Utility(Buyer buyer, Good good){
		this.buyer = buyer;
		this.good = good;
	}
	
	public Utility(){
	
	}
	
	public abstract double getUtility(double ratio);	
	public abstract Object clone();
}
