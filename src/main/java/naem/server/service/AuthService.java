package naem.server.service;

import naem.server.domain.member.Member;
import naem.server.domain.member.dto.ResponseMemberDto;

public interface AuthService {

    void signUpMember(Member member) throws Exception;

    Member loginMember(String id, String password) throws Exception;

    ResponseMemberDto getMyUserWithAuthorities();
}
