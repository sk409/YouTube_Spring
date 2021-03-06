package sk409.youtube.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sk409.youtube.models.Subscriber;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {
}