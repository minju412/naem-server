package naem.server.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import naem.server.domain.comment.Comment;
import naem.server.domain.comment.dto.CommentReadCondition;
import naem.server.domain.comment.dto.CommentResDto;
import naem.server.domain.comment.dto.CommentSaveDto;
import naem.server.domain.comment.dto.CommentUpdateDto;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;

public interface CommentService {

    void checkCommentPrivileges(long commentId);

    Comment checkCommentExist(long commentId);

    Member getCommentAuthor(Long commentId);

    Post getCommentPost(Long commentId);

    void save(Long postId, CommentSaveDto commentSaveDto);

    void deleteComment(Long commentId);

    void updateComment(Long commentId, CommentUpdateDto commentUpdateDto);

    Slice<CommentResDto> getMyCommentList(Long cursor, CommentReadCondition condition, Pageable pageRequest);
}
