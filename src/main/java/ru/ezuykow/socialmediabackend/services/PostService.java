package ru.ezuykow.socialmediabackend.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ezuykow.socialmediabackend.dto.post.EditPostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;
import ru.ezuykow.socialmediabackend.entities.User;
import ru.ezuykow.socialmediabackend.mappers.PostMapper;
import ru.ezuykow.socialmediabackend.repositories.PostRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author ezuykow
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final UserService userService;
    private final PostRepository postRepository;
    private final ImageService imageService;

    //-----------------API START-----------------

    public PostDTO createPost(String username, MultipartFile image, @Valid PostPropertiesDTO postPropertiesDTO) {
        Post post = postMapper.mapPostPropertiesDtoToPost(postPropertiesDTO);
        post.setAuthor(userService.findUserByUsername(username));
        post.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        post = postRepository.save(post);

        if (image != null) {
            post.setImage(imageService.uploadImage(post.getId(), image));
            postRepository.save(post);
        }

        return postMapper.mapPostToPostDto(post);
    }

    public Optional<Post> findById(long postId) {
        return postRepository.findById(postId);
    }

    public Page<Post> getPosts(int page, int count) {
        Pageable postsPage = PageRequest.of(page, count);
        return postRepository.findAll(postsPage);
    }

    public Page<Post> getIdolsPosts(Set<User> idols, int page, int count) {
        Pageable postsPage = PageRequest.of(page, count, Sort.by("createdAt"));
        return postRepository.findPostsByAuthorIn(idols, postsPage);
    }

    public List<PostDTO> getIdolsPostDTOs(String initiatorUsername, int page, int count) {
        User initiator = userService.findUserByUsername(initiatorUsername);
        return getIdolsPosts(initiator.getSubscribedTo(), page, count)
                .map(postMapper::mapPostToPostDto)
                .toList();
    }

    public List<PostDTO> getPostDTOs(int page, int count) {
        return getPosts(page, count).stream()
                .map(postMapper::mapPostToPostDto)
                .toList();
    }

    public PostDTO patchPost(Post targetPost, MultipartFile image, @Valid EditPostDTO editPostDTO) {

        if (editPostDTO.getTitle() != null && editPostDTO.getTitle().length() > 2) {
            targetPost.setTitle(editPostDTO.getTitle());
        }
        if (editPostDTO.getText() != null && editPostDTO.getText().length() > 2) {
            targetPost.setText(editPostDTO.getText());
        }

        postRepository.save(targetPost);

        if (image != null) {
            imageService.uploadImage(targetPost.getId(), image);
        }

        return postMapper.mapPostToPostDto(targetPost);
    }

    public void deletePost(long postId) {
        imageService.deletePostImage(postId);
        deleteById(postId);
    }

    public void deleteById(long postId) {
        postRepository.deleteById(postId);
    }

    public List<String> validateRequestParams(int page, int count) {
        List<String> errors = new LinkedList<>();
        if (page < 0) {
            errors.add("Page number must be 0 or more!");
        }
        if (count < 1) {
            errors.add("Posts count on page must be 1 or more!");
        }
        return errors;
    }

    //-----------------API END-----------------

}
