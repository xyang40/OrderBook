import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class TS {

	static HashMap<String, Record> symbol_table = new HashMap<String, Record>();

	public static Quote getQuote(String symbol, int time) {
		// O(N) anyway
		Record r = symbol_table.get(symbol);
		LinkedList<Quote> list_quote = r.getList_quote();
		Quote latest = null;
		for (Quote q : list_quote) {
			if (q.getTime_quote() <= time) {
				latest = q;
			} else {
				break;
			}
		}
		return latest;
	}

	public static void main(String[] args) throws FileNotFoundException {
		//get command line parameter: file of quotes, file of trades, and file for output
		String quotefile = args[0];
		String tradefile = args[1];
		String outputfile = args[2];
		
		// processing quotes file
		Scanner scan_quotes = new Scanner(new File(quotefile));
		scan_quotes.nextLine();

		while (scan_quotes.hasNextLine()) {
			String[] quote = scan_quotes.nextLine().split(",");
			int time_quote = Integer.parseInt(quote[0]);
			String symbol = quote[1];
			double bid = Double.parseDouble(quote[2]);
			double ask = Double.parseDouble(quote[3]);
			Quote cur_quote = new Quote(symbol, time_quote, bid, ask);
			
			if (symbol_table.containsKey(symbol)) {
				Record r = symbol_table.get(symbol);
				r.getList_quote().add(cur_quote);
			} else {
				Record r = new Record(symbol);
				LinkedList<Quote> list = new LinkedList<Quote>();
				list.add(cur_quote);
				r.setList_quote(list);
				symbol_table.put(symbol, r);
			}
		}
		scan_quotes.close();

		//output formatter
		PrintWriter pw = new PrintWriter(new File(tradefile));
		pw.println("OPEN_TIME,CLOSE_TIME,SYMBOL,QUANTITY,PNL,OPEN_SIDE,CLOSE_SIDE,OPEN_PRICE,CLOSE_PRICE,OPEN_BID,CLOSE_BID,OPEN_ASK,CLOSE_ASK,OPEN_LIQUIDITY,CLOSE_LIQUIDITY");

		// processing trades file
		Scanner scan_trades = new Scanner(new File(outputfile));
		scan_trades.nextLine();
		
		while (scan_trades.hasNextLine()) {
			String[] trade = scan_trades.nextLine().split(",");
			int time_trade = Integer.parseInt(trade[0]);
			String symbol = trade[1];
			String side = trade[2];
			double price = Double.parseDouble(trade[3]);
			int quantity = Integer.parseInt(trade[4]);
			Trade cur_trade = new Trade(symbol, time_trade, side, price, quantity);
			Record r = symbol_table.get(symbol);

			if (side.equals("B")) {
				r.getList_trade().add(cur_trade);
			} else {// Sell
				int cur_quantity = quantity;

				ListIterator<Trade> iterator = r.getList_trade().listIterator();
				while (iterator.hasNext()) {
					Trade t = iterator.next();
					
					//solution construction
					int open_time = t.getTime_trade();
					int close_time = time_trade;
					double PNL = cur_quantity * (price - t.getPrice());
					String open_side = "B";
					String close_side = "S";
					double open_price = t.getPrice();

					Quote open_quote = getQuote(symbol, open_time);
					Quote close_quote = getQuote(symbol, close_time);
					double open_bid = open_quote.getBid();
					double close_bid = close_quote.getBid();
					double open_ask = open_quote.getAsk();
					double close_ask = close_quote.getAsk();

					//Liquidity calculation
					String open_liquidity = null;
					if (open_price <= open_bid) {
						open_liquidity = "P";
					} else if (open_price >= open_ask) {
						open_liquidity = "A";
					}

					String close_liquidity = null;
					if (price >= close_ask) {
						close_liquidity = "P";
					} else if (price <= close_bid) {
						close_liquidity = "A";
					}

					if (t.getQuantity() > cur_quantity) {
						// case 1: closing trade smaller than opening trade
						t.setQuantity(t.getQuantity() - cur_quantity);
						String to_write = open_time + "," + close_time + "," + symbol + "," + cur_quantity + "," + String.format("%.2f", PNL) + "," + open_side + "," + close_side + "," + open_price + "," + price + "," + open_bid + "," + close_bid + "," + open_ask + "," + close_ask + "," + open_liquidity + "," + close_liquidity;
						pw.println(to_write);
						cur_quantity = 0;
						break;

					} else if (t.getQuantity() == cur_quantity) {
						// case 2: closing trade equal to opening trade		
						String to_write = open_time + "," + close_time + "," + symbol + "," + cur_quantity + "," + String.format("%.2f", PNL) + "," + open_side + "," + close_side + "," + open_price + "," + price + "," + open_bid + "," + close_bid + "," + open_ask + "," + close_ask + "," + open_liquidity + "," + close_liquidity;
						pw.println(to_write);
						iterator.remove();
						cur_quantity = 0;
						break;
					} else {
						// case 3: closing trade bigger than opening trade
						String to_write = open_time + "," + close_time + "," + symbol + "," + t.getQuantity() + "," + String.format("%.2f", PNL) + "," + open_side + "," + close_side + "," + open_price + "," + price + "," + open_bid + "," + close_bid + "," + open_ask + "," + close_ask + "," + open_liquidity + "," + close_liquidity;
						pw.println(to_write);
						iterator.remove();
						cur_quantity -= t.getQuantity();
					}
				}
				if (cur_quantity > 0) {
					//extra case: closing trade bigger than all opening trades: flip side and claim new inventory
					Trade newtrade = new Trade(symbol, time_trade, "B", price, cur_quantity);
					iterator.add(newtrade);
				}
			}
		}
		scan_trades.close();
		pw.close();
	}
}
