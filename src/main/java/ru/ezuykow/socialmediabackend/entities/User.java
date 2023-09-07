package ru.ezuykow.socialmediabackend.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(targetEntity = Subscribe.class, mappedBy = "subscriber", cascade = CascadeType.ALL)
    private Set<User> subscribedTo;

    @OneToMany(targetEntity = Subscribe.class, mappedBy = "subscribedToUser", cascade = CascadeType.ALL)
    private Set<User> subscribers;
}
