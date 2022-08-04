package naem.server.service;

import naem.server.domain.member.Member;
import naem.server.domain.member.dto.ResponseMemberDto;

public interface AuthService {

    void signUpMember(Member member);

    Member loginMember(String id, String password);

    ResponseMemberDto getMyUserWithAuthorities();
}
