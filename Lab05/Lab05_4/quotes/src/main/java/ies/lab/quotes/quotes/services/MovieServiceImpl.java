package ies.lab.quotes.quotes.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ies.lab.quotes.quotes.entities.Movie;
import ies.lab.quotes.quotes.exceptions.ResourceNotFoundException;
import ies.lab.quotes.quotes.repositories.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long movieId) throws ResourceNotFoundException {
        Movie movie = movieRepository.findById(movieId)
                     .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " not found"));
        return movie;
    }

    @Override
    public Movie updateMovie(Movie movie) {
        Movie existingMovie = movieRepository.findById(movie.getId()).get();
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setYear(movie.getYear());
        Movie updatedMovie = movieRepository.save(existingMovie);
        return updatedMovie;
    }

    @Override
    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieByTitleAndYear(String title, int year) throws ResourceNotFoundException {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                     .orElseThrow(() -> new ResourceNotFoundException("Movie with title " + title + " and year " + year + " not found"));
        return movie;
    }
    
}
