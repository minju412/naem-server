package naem.server.repository;

import static naem.server.domain.QComment.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import naem.server.domain.comment.Comment;
import naem.server.domain.comment.dto.CommentReadCondition;
import naem.server.domain.comment.dto.CommentResDto;
import naem.server.domain.member.Member;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    // 게시글 조회 및 검색
    @Override
    public Slice<CommentResDto> getCommentScroll(Long cursorId, CommentReadCondition condition, Pageable pageable) {

        List<Comment> commentList = queryFactory
            .select(comment)
            .from(comment)
            .where(
                eqIsDeleted(condition.getIsDeleted()), // 삭제되지 않은 댓글만 조회
                eqIsMine(condition.getMember()), // 내가 작성한 댓글만 조회
                eqCursorId(cursorId)
            )
            .limit(pageable.getPageSize() + 1) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
            .fetch();

        List<CommentResDto> commentResDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResDtos.add(new CommentResDto(comment));
        }

        boolean hasNext = false;
        if (commentResDtos.size() > pageable.getPageSize()) {
            commentResDtos.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(commentResDtos, pageable, hasNext);
    }

    //동적 쿼리를 위한 BooleanExpression
    private BooleanExpression eqCursorId(Long cursorId) {
        return (cursorId == null) ? null : comment.id.gt(cursorId);
    }

    // 삭제된 댓글인지 필터링
    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return (isDeleted == null) ? null : comment.isDeleted.eq(isDeleted);
    }

    // 내가 쓴 댓글인지 필터링
    private BooleanExpression eqIsMine(Member member) {
        return (member == null) ? null : comment.member.eq(member);
    }
}
