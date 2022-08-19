package naem.server.domain.post.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Tag;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResDto {

    private String title;
    private String content;
    private List<Tag> tags = new ArrayList<>();

    public PostResDto(Post entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        for (PostTag postTag : entity.getPostTags()) {
            this.tags.add(Tag.createTag(postTag));
        }
    }
}
