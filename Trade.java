
public class Trade {
	
	private String symbol;
	private int time_trade;
	private String side;
	private double price;
	private int quantity;
	
	public Trade(String s,int t,String si,double p,int q){
		this.setSymbol(s);
		this.setTime_trade(t);
		this.setSide(si);
		this.setPrice(p);
		this.setQuantity(q);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getTime_trade() {
		return time_trade;
	}

	public void setTime_trade(int time_trade) {
		this.time_trade = time_trade;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
