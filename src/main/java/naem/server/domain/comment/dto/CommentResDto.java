package naem.server.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.comment.Comment;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResDto {

    private String member;
    private String content;

    public CommentResDto(Comment entity) {
        this.member = entity.getMember().getUsername();
        this.content = entity.getContent();
    }
}
