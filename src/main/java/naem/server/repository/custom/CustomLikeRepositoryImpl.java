package naem.server.repository.custom;

import static naem.server.domain.QLike.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import naem.server.domain.Like;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;

@Repository
@RequiredArgsConstructor
public class CustomLikeRepositoryImpl implements CustomLikeRepository {

    private final JPAQueryFactory queryFactory;

    // 좋아요한 게시글 조회
    @Override
    public Slice<BriefPostInfoDto> getMyLikedPostScroll(Long cursorId, PostReadCondition condition, Pageable pageable) {

        List<Like> likeList = queryFactory
            .select(like)
            .from(like)
            .where(
                eqIsDeleted(condition.getIsDeleted()), // 삭제되지 않은 게시글만 조회
                eqIsLiked(condition.getMember()), // 내가 좋아요한 게시글만 조회
                eqCursorId(cursorId)
            )
            .limit(pageable.getPageSize() + 1) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
            .fetch();

        List<BriefPostInfoDto> briefPostInfoDtos = new ArrayList<>();
        for (Like like : likeList) {
            Post post = like.getPost();
            briefPostInfoDtos.add(new BriefPostInfoDto(post));
        }

        boolean hasNext = false;
        if (briefPostInfoDtos.size() > pageable.getPageSize()) {
            briefPostInfoDtos.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(briefPostInfoDtos, pageable, hasNext);
    }

    //동적 쿼리를 위한 BooleanExpression
    private BooleanExpression eqCursorId(Long cursorId) {
        return (cursorId == null) ? null : like.id.gt(cursorId);
    }

    // 좋아요한 게시글이 삭제된 게시글인지 필터링
    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return (isDeleted == null) ? null : like.post.isDeleted.eq(isDeleted);
    }

    // 내가 좋아요한 게시인지 필터링
    private BooleanExpression eqIsLiked(Member member) {
        return (member == null) ? null : like.member.eq(member);
    }

}
