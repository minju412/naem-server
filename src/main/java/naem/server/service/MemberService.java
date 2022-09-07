package naem.server.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.member.Member;
import naem.server.domain.member.dto.MemberReadCondition;
import naem.server.domain.member.dto.MemberWithdrawDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.domain.member.dto.ProfileResDto;

public interface MemberService {

    Member getLoginMember(UserDetails userDetails);

    ProfileResDto getMyInfo(UserDetails userDetails);

    void patch(PatchMemberDto patchMemberDto, UserDetails userDetails);
    
    void withdraw(MemberWithdrawDto memberWithdrawDto, UserDetails userDetails) throws Exception;

    Slice<ProfileResDto> getMemberList(Long cursor, MemberReadCondition condition, Pageable pageRequest);
}
