package naem.server.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import naem.server.domain.comment.dto.CommentReadCondition;
import naem.server.domain.comment.dto.CommentResDto;

public interface CustomCommentRepository {

    Slice<CommentResDto> getMyCommentScroll(Long cursorId, CommentReadCondition condition, Pageable pageable);
}
