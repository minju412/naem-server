package naem.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import naem.server.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    /**
     * 이메일 중복 여부를 확인
     *
     * @param username
     * @return true | false
     */
    boolean existsByUsername(String username);
}
