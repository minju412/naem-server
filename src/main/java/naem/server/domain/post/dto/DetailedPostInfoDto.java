package naem.server.domain.post.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.BoardType;
import naem.server.domain.Tag;
import naem.server.domain.comment.Comment;
import naem.server.domain.comment.dto.CommentResDto;
import naem.server.domain.post.PostImage;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedPostInfoDto {

    private BoardType boardType;
    private String title;
    private String content;
    private List<Tag> tags;
    private List<String> imgUrls;
    private List<CommentResDto> comments;

    public DetailedPostInfoDto(Post entity) {
        this.boardType = entity.getBoard().getBoardType();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        for (PostTag postTag : entity.getPostTags()) {
            this.tags.add(Tag.getTagFromPostTag(postTag));
        }
        for (PostImage image : entity.getImg()) {
            this.imgUrls.add(image.getImgUrl());
        }
        for (Comment comment : entity.getComments()) {
            this.comments.add(new CommentResDto(comment));
        }
    }
}
