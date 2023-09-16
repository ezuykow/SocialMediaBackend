package ru.ezuykow.socialmediabackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ezuykow.socialmediabackend.entities.Post;
import ru.ezuykow.socialmediabackend.entities.User;

import java.util.Set;

/**
 * @author ezuykow
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByAuthorIn(Set<User> authors, Pageable pageable);
}
