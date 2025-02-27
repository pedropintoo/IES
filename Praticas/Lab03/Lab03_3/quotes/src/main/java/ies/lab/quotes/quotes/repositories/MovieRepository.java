package ies.lab.quotes.quotes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.lab.quotes.quotes.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {}
