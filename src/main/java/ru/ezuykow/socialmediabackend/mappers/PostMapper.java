package ru.ezuykow.socialmediabackend.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.ezuykow.socialmediabackend.dto.PostDTO;
import ru.ezuykow.socialmediabackend.dto.PostPropertiesDTO;
import ru.ezuykow.socialmediabackend.entities.Post;

/**
 * @author ezuykow
 */
@Component
@RequiredArgsConstructor
public class PostMapper {

    private final ModelMapper modelMapper;

    //-----------------API START-----------------

    public Post mapPostPropertiesDtoToPost(PostPropertiesDTO dto) {
        return modelMapper.map(dto, Post.class);
    }

    public PostDTO mapPostToPostDto(Post post) {
        TypeMap<Post, PostDTO> propertyMapper = modelMapper.createTypeMap(Post.class, PostDTO.class);
        propertyMapper.addMappings(
                mapper -> mapper.map(p -> p.getAuthor().getUserId(), PostDTO::setAuthorId)
        );
        propertyMapper.addMappings(
                mapper -> mapper.map(p -> p.getAuthor().getUsername(), PostDTO::setAuthorUsername)
        );

        return modelMapper.map(post, PostDTO.class);
    }

    //-----------------API END-----------------
}
