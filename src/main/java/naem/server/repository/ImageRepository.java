package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import naem.server.domain.post.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
