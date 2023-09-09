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

    @ManyToMany
    @JoinTable(
            name = "subscribes",
            joinColumns = @JoinColumn(name = "subscriber_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribe_to_user_id", referencedColumnName = "user_id")
    )
    private Set<User> subscribedTo;

    @ManyToMany
    @JoinTable(
            name = "subscribes",
            joinColumns = @JoinColumn(name = "subscribe_to_user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id", referencedColumnName = "user_id")
    )
    private Set<User> subscribers;

    @ManyToMany
    @JoinTable(
            name = "friends_requests",
            joinColumns = @JoinColumn(name = "request_from_user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "request_to_user_id", referencedColumnName = "user_id")
    )
    private Set<User> outcomeFriendsRequests;

    @ManyToMany
    @JoinTable(
            name = "friends_requests",
            joinColumns = @JoinColumn(name = "request_to_user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "request_from_user_id", referencedColumnName = "user_id")
    )
    private Set<User> incomeFriendsRequests;
}
