package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import naem.server.domain.post.PostImage;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
