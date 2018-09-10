package videostore;
import java.util.Enumeration;
import java.util.Vector;

public class Customer {
	private String _name;
	private Vector _rentals = new Vector();

	public Customer(String name) {
		_name = name;
	}

	public String statement() {
		double totalAmount = 0;
		int frequentRenterPoints = 0;
		Enumeration rentals = _rentals.elements();
		String result = "Rental Record for " + getName() + "\n";
		while (rentals.hasMoreElements()) {
			double thisAmount = 0;
			Rental each = (Rental) rentals.nextElement();
			Movie movie = each.getMovie();

			thisAmount = each.determineAmount();
			
			int priceCode = movie.getPriceCode();
			int daysRented = each.getDaysRented();

			frequentRenterPoints = movie.getFrequentRenterPoints(frequentRenterPoints, priceCode, daysRented);
			
			String title = movie.getTitle();
			result += printFiguresForRental(thisAmount, title);
			totalAmount += thisAmount;
		}
		result += addFooterLines(totalAmount, frequentRenterPoints);
		return result;
	}

	private String printFiguresForRental(double thisAmount, String title) {
		return "\t" + title + "\t" + String.valueOf(thisAmount) + "\n";
	}

	private String addFooterLines(double totalAmount, int frequentRenterPoints) {
		return "Amount owed is " + String.valueOf(totalAmount) + "\n"
				+ "You earned " + String.valueOf(frequentRenterPoints) + " frequent renter points";
	}

	public void addRental(Rental arg) {
		_rentals.addElement(arg);
	}

	public String getName() {
		return _name;
	}
}
