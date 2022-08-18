package naem.server.domain.post.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Tag;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveReqDto {

    private String title;
    private String content;
    private List<Tag> tag;

    public Post toEntity(Member author, List<PostTag> postTags) {
        return Post.builder()
            .title(title)
            .content(content)
            .member(author)
            .postTag(postTags)
            .build();
    }
}
