package ru.ezuykow.socialmediabackend.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.ezuykow.socialmediabackend.dto.post.PostDTO;
import ru.ezuykow.socialmediabackend.dto.post.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;

/**
 * @author ezuykow
 */
@Component
public class PostMapper {

    private final ModelMapper modelMapper;

    public PostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        createPostToPostDtoTypeMap();
    }

    //-----------------API START-----------------

    public Post mapPostPropertiesDtoToPost(PostPropertiesDTO dto) {
        return modelMapper.map(dto, Post.class);
    }

    public PostDTO mapPostToPostDto(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

    //-----------------API END-----------------

    private void createPostToPostDtoTypeMap() {
        modelMapper.createTypeMap(Post.class, PostDTO.class)
                .addMappings(mapper -> mapper.map(
                        p -> p.getAuthor().getUserId(),
                        PostDTO::setAuthorId))
                .addMappings(mapper -> mapper.map(
                        p -> p.getAuthor().getUsername(),
                        PostDTO::setAuthorUsername));
    }
}
