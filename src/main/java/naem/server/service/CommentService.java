package naem.server.service;

import naem.server.domain.comment.Comment;
import naem.server.domain.comment.dto.CommentSaveDto;
import naem.server.domain.comment.dto.CommentUpdateDto;
import naem.server.domain.post.Post;

public interface CommentService {

    void checkCommentPrivileges(long commentId);

    Comment checkCommentExist(long commentId);

    Long getCommentAuthorId(Long commentId);

    Post getCommentPost(Long commentId);

    void save(Long postId, CommentSaveDto commentSaveDto);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, CommentUpdateDto commentUpdateDto);
}
