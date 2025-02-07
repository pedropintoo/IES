package ies.lab.quotes.quotes.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import ies.lab.quotes.quotes.dto.QuoteConsumerRecord;
import ies.lab.quotes.quotes.entities.Movie;
import ies.lab.quotes.quotes.entities.Quote;
import ies.lab.quotes.quotes.exceptions.ResourceNotFoundException;
import ies.lab.quotes.quotes.services.MovieService;
import ies.lab.quotes.quotes.services.QuoteService;

import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Service
public class QuotesConsumer {

    private final QuoteService quoteService;
    private final MovieService movieService;

    public QuotesConsumer(QuoteService quoteService, MovieService movieService) {
        this.quoteService = quoteService;
        this.movieService = movieService;
    }

    @Bean
    public NewTopic quoteConsumerBean() {
        return TopicBuilder.name("quotes")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(topics = "quotes", containerFactory = "quoteKafkaListenerContainerFactory")
    public void listenQuotes(QuoteConsumerRecord in) {
        Movie movie = new Movie();
        movie.setTitle(in.getMovie());
        movie.setYear(in.getYear());
        try {
            movie = movieService.getMovieByTitleAndYear(movie.getTitle(), movie.getYear());
        } catch (ResourceNotFoundException e) {
            movie = movieService.createMovie(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Quote quote = new Quote();
        quote.setQuote(in.getQuote());
        quote.setMovie(movie);
        quoteService.createQuote(quote);

        System.out.println("Received: " + in);
    }
}
