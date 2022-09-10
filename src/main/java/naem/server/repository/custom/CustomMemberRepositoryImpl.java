package naem.server.repository.custom;

import static naem.server.domain.member.QMember.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.ProfileResDto;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    // 가입된 회원 조회
    @Override
    public Slice<ProfileResDto> getProfileResDtoScroll(Long cursorId, MemberReadCondition condition, Pageable pageable) {

        List<Member> memberList = queryFactory
            .select(member)
            .from(member)
            .where(
                eqIsDeleted(condition.getIsDeleted()), // 탈퇴하지 않은 회원만 조회
                eqIsUser(condition.getMemberRole()), // 인증된 회원만 조회
                eqCursorId(cursorId)
            )
            .limit(pageable.getPageSize() + 1) // limit 보다 데이터를 1개 더 들고와서, 해당 데이터가 있다면 hasNext 변수에 true 를 넣어 알림
            .fetch();

        List<ProfileResDto> profileResDtos = new ArrayList<>();
        for (Member member : memberList) {
            profileResDtos.add(new ProfileResDto(member));
        }

        boolean hasNext = false;
        if (profileResDtos.size() > pageable.getPageSize()) {
            profileResDtos.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(profileResDtos, pageable, hasNext);
    }

    //동적 쿼리를 위한 BooleanExpression
    private BooleanExpression eqCursorId(Long cursorId) {
        return (cursorId == null) ? null : member.id.gt(cursorId);
    }

    // 탈퇴한 회원인지 필터링
    private BooleanExpression eqIsDeleted(Boolean isDeleted) {
        return (isDeleted == null) ? null : member.isDeleted.eq(isDeleted);
    }

    // 회원 role 필터링 (어드민은 제외)
    private BooleanExpression eqIsUser(MemberRole memberRole) {
        return (memberRole == null) ? null : member.role.eq(memberRole);
    }

}
