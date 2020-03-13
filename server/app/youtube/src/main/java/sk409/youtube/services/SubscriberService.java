package sk409.youtube.services;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Subscriber;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.SubscriberSpecifications;
import sk409.youtube.repositories.SubscriberRepository;

@Service
public class SubscriberService extends QueryService<Subscriber> {

    private final SubscriberRepository subscriberRepository;

    public SubscriberService(final EntityManager entityManager, final SubscriberRepository subscriberRepository) {
        super(entityManager);
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public Class<Subscriber> classLiteral() {
        return Subscriber.class;
    }

    public Optional<Subscriber> delete(final Long id) {
        final SubscriberSpecifications subscriberSpecifications = new SubscriberSpecifications();
        subscriberSpecifications.setIdEqual(id);
        final QueryComponents<Subscriber> subscriberQueryComponents = new QueryComponents<>();
        subscriberQueryComponents.setSpecifications(subscriberSpecifications);
        final Optional<Subscriber> _subscriber = findOne(subscriberQueryComponents);
        if (!_subscriber.isPresent()) {
            return Optional.ofNullable(null);
        }
        final Subscriber subscriber = _subscriber.get();
        subscriberRepository.delete(subscriber);
        return Optional.of(subscriber);
    }

    public Subscriber save(final Long userId, final Long channelId) {
        final Subscriber subscriber = new Subscriber(userId, channelId);
        subscriberRepository.save(subscriber);
        return subscriber;
    }

}