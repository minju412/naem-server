package naem.server.service;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import naem.server.domain.member.Member;
import naem.server.domain.member.dto.PatchMemberDto;

public interface MemberService {

    Optional<Member> findByUsername(String username);

    void patch(long id, PatchMemberDto patchMemberDto);
}
