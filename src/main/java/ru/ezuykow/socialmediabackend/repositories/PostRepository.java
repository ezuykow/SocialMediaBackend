package ru.ezuykow.socialmediabackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ezuykow.socialmediabackend.entities.Post;

/**
 * @author ezuykow
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
