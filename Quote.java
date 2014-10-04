
public class Quote {
	
	private String symbol;
	private int time_quote;
	private double bid;
	private double ask;
	
	public Quote(String s,int t,double b,double a){
		this.setSymbol(s);
		this.setTime_quote(t);
		this.setBid(b);
		this.setAsk(a);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getTime_quote() {
		return time_quote;
	}

	public void setTime_quote(int time_quote) {
		this.time_quote = time_quote;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}
	

}
