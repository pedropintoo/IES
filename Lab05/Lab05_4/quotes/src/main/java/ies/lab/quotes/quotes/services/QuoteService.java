package ies.lab.quotes.quotes.services;

import java.util.List;

import ies.lab.quotes.quotes.entities.Quote;
import ies.lab.quotes.quotes.exceptions.ResourceNotFoundException;

public interface QuoteService {

    Quote createQuote(Quote quote);

    Quote updateQuote(Quote quote);
    
    void deleteQuote(Long quoteId);
    
    Quote getQuoteById(Long quoteId) throws ResourceNotFoundException;
    
    List<Quote> getAllQuotes();

    Quote getRandomQuote();

    List<Quote> getQuotesForMovieId(Long movieId) throws ResourceNotFoundException;

    List<Quote> getQuotesInRealTime(int limit);

}
