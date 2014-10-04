import java.util.LinkedList;

public class Record {

	private String symbol;
	private LinkedList<Quote> list_quote;
	private LinkedList<Trade> list_trade;

	public Record(String s) {
		this.setSymbol(s);
		list_quote = new LinkedList<Quote>();
		list_trade = new LinkedList<Trade>();
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public LinkedList<Quote> getList_quote() {
		return list_quote;
	}

	public void setList_quote(LinkedList<Quote> list_quote) {
		this.list_quote = list_quote;
	}

	public LinkedList<Trade> getList_trade() {
		return list_trade;
	}

	public void setList_trade(LinkedList<Trade> list_trade) {
		this.list_trade = list_trade;
	}
}
