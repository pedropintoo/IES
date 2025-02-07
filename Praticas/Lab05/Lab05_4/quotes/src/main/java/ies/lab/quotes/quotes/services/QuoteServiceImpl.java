package ies.lab.quotes.quotes.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ies.lab.quotes.quotes.entities.Quote;
import ies.lab.quotes.quotes.exceptions.ResourceNotFoundException;
import ies.lab.quotes.quotes.repositories.QuotesRepository;

@Service
public class QuoteServiceImpl implements QuoteService {

    private QuotesRepository quotesRepository;
    private SimpMessagingTemplate messagingTemplate;

    public QuoteServiceImpl(QuotesRepository quotesRepository, SimpMessagingTemplate messagingTemplate) {
        this.quotesRepository = quotesRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Quote createQuote(Quote quote) {
        Quote insertedQuote = quotesRepository.save(quote);
        messagingTemplate.convertAndSend("/quotes", insertedQuote); // update websocket to notify clients
        return insertedQuote;
    }

    @Override
    public Quote getQuoteById(Long quoteId) throws ResourceNotFoundException {
        Quote quote = quotesRepository.findById(quoteId)
                        .orElseThrow(() -> new ResourceNotFoundException("Quote with ID " + quoteId + " not found"));
        return quote;
    }

    @Override
    public Quote updateQuote(Quote quote) {
        Quote existingQuote = quotesRepository.findById(quote.getId()).get();
        existingQuote.setQuote(quote.getQuote());
        existingQuote.setMovie(quote.getMovie());
        Quote updateQuote = quotesRepository.save(existingQuote);
        return updateQuote;
    }

    @Override
    public void deleteQuote(Long quoteId) {
        quotesRepository.deleteById(quoteId);
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quotesRepository.findAll();
    }

    @Override
    public Quote getRandomQuote() {
        return quotesRepository.getRandomQuote();
    }

    @Override
    public List<Quote> getQuotesForMovieId(Long movieId) throws ResourceNotFoundException {
        return quotesRepository.findByMovieId(movieId);
    }

    @Override
    public List<Quote> getQuotesInRealTime(int limit) {
        return quotesRepository.findAll().stream()
            .sorted((q1, q2) -> Long.compare(q2.getId(), q1.getId()))
            .limit(5)
            .collect(Collectors.toList());
    }
    
}
