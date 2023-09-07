package ru.ezuykow.socialmediabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ezuykow.socialmediabackend.entities.Subscribe;
import ru.ezuykow.socialmediabackend.entities.User;

/**
 * @author ezuykow
 */
@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    void deleteBySubscriberAndSubscribedToUser(User initiator, User target);
}
