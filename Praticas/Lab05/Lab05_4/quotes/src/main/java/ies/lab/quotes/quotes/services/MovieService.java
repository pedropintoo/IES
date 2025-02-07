package ies.lab.quotes.quotes.services;

import java.util.List;

import ies.lab.quotes.quotes.entities.Movie;
import ies.lab.quotes.quotes.exceptions.ResourceNotFoundException;

public interface MovieService {

    Movie createMovie(Movie movie);
    
    Movie updateMovie(Movie movie);
    
    void deleteMovie(Long movieId);
    
    Movie getMovieById(Long movieId) throws ResourceNotFoundException;
    
    List<Movie> getAllMovies();

    Movie getMovieByTitleAndYear(String title, int year) throws ResourceNotFoundException;

}
