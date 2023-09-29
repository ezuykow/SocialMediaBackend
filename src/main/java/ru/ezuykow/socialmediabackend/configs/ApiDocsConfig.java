package ru.ezuykow.socialmediabackend.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ezuykow
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SocialMediaBackend",
                description = "SocialMedia",
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
public class ApiDocsConfig {

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            var schemas = openApi.getComponents().getSchemas();

            var friendsRequestsSchema = new ObjectSchema()
                    .name("FriendsRequests")
                    .title("Заявки в друзья")
                    .description("Списки входящих и исходящих заявок в друзья")
                    .addProperty("to_me", new ArraySchema().items(schemas.get("FriendDTO")))
                    .addProperty("from_me", new ArraySchema().items(schemas.get("FriendDTO")));
            schemas.put(friendsRequestsSchema.getName(), friendsRequestsSchema);

            var userSubscriberDtoSchema = new ObjectSchema()
                    .name("UserSubscriberDTO")
                    .title("Подписчик")
                    .addProperty("userId", new IntegerSchema().description("ID пользователя")
                            .readOnly(true).example("123"))
                    .addProperty("username", new StringSchema().description("Username пользователя")
                            .readOnly(true).example("Ivan"));
            schemas.put(userSubscriberDtoSchema.getName(), userSubscriberDtoSchema);

            var subscribesSchema = new ObjectSchema()
                    .name("Subscribes")
                    .title("Подписки")
                    .description("Списки входящих и исходящих подписок")
                    .addProperty("to_me", new ArraySchema().items(schemas.get("UserSubscriberDTO")))
                    .addProperty("from_me", new ArraySchema().items(schemas.get("UserSubscriberDTO")));
            schemas.put(subscribesSchema.getName(), subscribesSchema);
        };
    }
}
