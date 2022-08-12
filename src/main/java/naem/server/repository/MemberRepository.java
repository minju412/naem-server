package naem.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);
}
