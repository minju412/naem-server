package naem.server.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.ProfileResDto;

public interface CustomMemberRepository {

    Slice<ProfileResDto> getProfileResDtoScroll(Long cursorId, MemberReadCondition condition, Pageable pageable);
}
