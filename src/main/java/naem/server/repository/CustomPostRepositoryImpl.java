package naem.server.repository;

import static naem.server.domain.post.QPost.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import naem.server.domain.BoardType;
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    // 게시글 조회 및 검색
    @Override
    public Slice<BriefPostInfoDto> getBriefPostInfoScroll(Long cursorId, PostReadCondition condition,
        Pageable pageable) {

        List<Post> postList = queryFactory
            .select(post)
            .from(post)
            .where(
                eqIsDeleted(condition.getIsDeleted()), // 삭제되지 않은 게시글만 조회
                eqBoardType(condition.getBoardType()), // 게시판 타입 별 조회
                eqTitle(condition.getKeyword()),
                eqContent(condition.getKeyword()),
                eqCursorId(cursorId)
            )
            .limit(pageable.getPageSize() + 1) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
            .fetch();

        List<BriefPostInfoDto> briefPostInfos = new ArrayList<>();
        for (Post post : postList) {
            briefPostInfos.add(new BriefPostInfoDto(post));
        }

        boolean hasNext = false;
        if (briefPostInfos.size() > pageable.getPageSize()) {
            briefPostInfos.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(briefPostInfos, pageable, hasNext);
    }

    //동적 쿼리를 위한 BooleanExpression
    private BooleanExpression eqCursorId(Long cursorId) {
        return (cursorId == null) ? null : post.id.gt(cursorId);
    }

    // 삭제된 게시글인지 필터링
    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return (isDeleted == null) ? null : post.isDeleted.eq(isDeleted);
    }

    // 제목에 keyword 포함되어있는지 필터링
    private BooleanExpression eqTitle(String keyword) {
        return (keyword == null) ? null : post.title.contains(keyword);
    }

    // 내용에 keyword 포함되어있는지 필터링
    private BooleanExpression eqContent(String keyword) {
        return (keyword == null) ? null : post.content.contains(keyword);
    }

    // 게시판 타입 필터링
    private BooleanExpression eqBoardType(BoardType boardType) {
        return (boardType == null) ? null : post.board.boardType.eq(boardType);
    }

}
