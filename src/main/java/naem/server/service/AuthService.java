package naem.server.service;

import naem.server.domain.member.Member;

public interface AuthService {

    void signUpMember(Member member);

    Member loginMember(String id, String password) throws Exception;
}
