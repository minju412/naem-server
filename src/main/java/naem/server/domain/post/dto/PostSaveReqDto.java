package naem.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;

@Getter
@NoArgsConstructor
public class PostSaveReqDto {

    private  String title;
    private  String content;

    @Builder
    public PostSaveReqDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity(Member author) {
        return Post.builder()
            .title(title)
            .content(content)
            .member(author)
            .build();
    }
}
