package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.member.DisabledMemberInfo;

public interface DisabledMemberInfoRepository extends JpaRepository<DisabledMemberInfo, Long> {
}
