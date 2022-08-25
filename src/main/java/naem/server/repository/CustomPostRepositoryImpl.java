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
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<BriefPostInfoDto> getBriefPostInfoScroll(Long cursorId, Pageable pageable) {

        List<Post> postList = queryFactory
            .select(post)
            .from(post)
            .where(
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
        if (cursorId != null) {
            return post.id.gt(cursorId);
        }
        return null;
    }

    private BooleanExpression ltPostId(Long cursor) {
        return cursor == null ? null : post.id.lt(cursor);
    }

}
