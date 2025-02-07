package ies.lab.quotes.quotes.dto;

public class QuoteConsumerRecord {
    private String quote;
    private String movie;
    private int year;

    // required by Jackson
    public QuoteConsumerRecord() {
    }

    public QuoteConsumerRecord(String quote, String movie, int year) {
        this.quote = quote;
        this.movie = movie;
        this.year = year;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
