package naem.server.service;

import java.util.List;
import java.util.Optional;

import naem.server.domain.member.Member;

public interface MemberService {

    /**
     * 모든 유저 리스트를 반환
     * @return 유저 리스트
     */
    List<Member> findAll();

    /**
     * 아이디를 통해 유저 조회
     * @param username
     * @return 조회된 유저
     */
    Optional<Member> findByUsername(String username);
}
