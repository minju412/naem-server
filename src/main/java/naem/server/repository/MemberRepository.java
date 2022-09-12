package naem.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.member.Member;
import naem.server.repository.custom.CustomMemberRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByRecommenderCode(String recommenderCode);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);
}
