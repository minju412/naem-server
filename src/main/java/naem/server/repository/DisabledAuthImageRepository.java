package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.member.DisabledAuthImage;

public interface DisabledAuthImageRepository extends JpaRepository<DisabledAuthImage, Long> {
}
