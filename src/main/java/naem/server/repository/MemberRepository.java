package naem.server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import naem.server.domain.member.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

    Member findByNickname(String nickname);

    Member findByPhoneNumber(String phoneNumber);

    // @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByUsername(String username);
}
