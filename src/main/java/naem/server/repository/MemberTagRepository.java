package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.member.MemberTag;

public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {
}
