package videostore;
public class Movie {
    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;
    private int _priceCode;

    public Movie(String title, int priceCode) {
        _title = title;
        _priceCode = priceCode;
    }

    public int getPriceCode() {
        return _priceCode;
    }

    public void setPriceCode(int _priceCode) {
        this._priceCode = _priceCode;
    }

    public String getTitle() {
        return _title;
    }
    
	int getFrequentRenterPoints(int frequentRenterPoints, int priceCode, int daysRented) {
		frequentRenterPoints++;
		// add bonus for a two day new release rental
		if ((priceCode == Movie.NEW_RELEASE) && daysRented > 1)
			frequentRenterPoints++;
		return frequentRenterPoints;
	}
}
