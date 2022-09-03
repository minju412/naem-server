package naem.server.service;

import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.member.Member;
import naem.server.domain.member.dto.MemberWithdrawDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.domain.member.dto.ProfileResDto;

public interface MemberService {

    Member getLoginMember(UserDetails userDetails);

    ProfileResDto getMyInfo(UserDetails userDetails);

    void patch(long id, PatchMemberDto patchMemberDto, UserDetails userDetails);
    
    void withdraw(long memberId, MemberWithdrawDto memberWithdrawDto, UserDetails userDetails) throws Exception;
}
