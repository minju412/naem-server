package naem.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.post.Post;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BriefPostInfoDto {

    private Long postId;
    private String title;
    private String content;
    private String writerName;
    private String createdDate;

    public BriefPostInfoDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerName = post.getMember().getNickname();
        this.createdDate = post.getCreateAt().toString();
    }
}
