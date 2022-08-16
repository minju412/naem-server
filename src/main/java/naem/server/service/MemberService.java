package naem.server.service;

import java.util.Optional;

import naem.server.domain.member.Member;

public interface MemberService {

    /**
     * 아이디를 통해 유저 조회
     * @param username
     * @return 조회된 유저
     */
    Optional<Member> findByUsername(String username);

    void withdraw(String checkPassword) throws Exception;
}
