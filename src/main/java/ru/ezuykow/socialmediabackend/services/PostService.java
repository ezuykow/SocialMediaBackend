package ru.ezuykow.socialmediabackend.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ezuykow.socialmediabackend.dto.PostDTO;
import ru.ezuykow.socialmediabackend.dto.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;
import ru.ezuykow.socialmediabackend.exceptions.UserNotFoundException;
import ru.ezuykow.socialmediabackend.mappers.PostMapper;
import ru.ezuykow.socialmediabackend.repositories.PostRepository;
import ru.ezuykow.socialmediabackend.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageService imageService;

    //-----------------API START-----------------

    public PostDTO createPost(String username, MultipartFile image, @Valid PostPropertiesDTO postPropertiesDTO) {
        Post post = postMapper.mapPostPropertiesDtoToPost(postPropertiesDTO);
        post.setAuthor(userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new));
        post.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        post = postRepository.save(post);

        post.setImage(imageService.uploadImage(post.getId(), image));
        postRepository.save(post);

        return postMapper.mapPostToPostDto(post);
    }

    public Page<Post> getPosts(int page, int count) {
        Pageable postsPage = PageRequest.of(page, count);
        return postRepository.findAll(postsPage);
    }

    public List<PostDTO> getPostDTOs(int page, int count) {
        return getPosts(page, count).stream()
                .map(postMapper::mapPostToPostDto)
                .collect(Collectors.toList());
    }

    //-----------------API START-----------------
}
