package sk409.youtube.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import sk409.youtube.models.Subscriber;
import sk409.youtube.models.Subscriber_;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.SubscriberSpecifications;
import sk409.youtube.repositories.SubscriberRepository;

@Service
public class SubscriberService extends QueryService<Subscriber> {

    private final EntityManager entityManager;
    private final SubscriberRepository subscriberRepository;

    public SubscriberService(final EntityManager entityManager, final SubscriberRepository subscriberRepository) {
        super(entityManager);
        this.entityManager = entityManager;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public Class<Subscriber> classLiteral() {
        return Subscriber.class;
    }

    public Long countByChannelId(final Long channelId) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<Subscriber> root = query.from(Subscriber.class);
        final Path<Long> channelIdPath = root.get(Subscriber_.CHANNEL_ID);
        final Expression<Long> countExpression = builder.count(root);
        query.select(countExpression).where(builder.equal(channelIdPath, channelId));
        final Long count = entityManager.createQuery(query).getSingleResult().longValue();
        return count;
    }

    public Long countByUserId(final Long userId) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<Subscriber> root = query.from(Subscriber.class);
        final Path<Long> userIdPath = root.get(Subscriber_.USER_ID);
        final Expression<Long> countExpression = builder.count(root);
        query.select(countExpression).where(builder.equal(userIdPath, userId));
        final Long count = entityManager.createQuery(query).getSingleResult().longValue();
        return count;
    }

    public Optional<List<Subscriber>> findByUserId(final Long userId) {
        final SubscriberSpecifications subscriberSpecifications = new SubscriberSpecifications();
        subscriberSpecifications.setUserIdEqual(userId);
        final QueryComponents<Subscriber> subscriberQueryComponents = new QueryComponents<>();
        subscriberQueryComponents.setSpecifications(subscriberSpecifications);
        final Optional<List<Subscriber>> _subscribers = findAll(subscriberQueryComponents);
        return _subscribers;
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