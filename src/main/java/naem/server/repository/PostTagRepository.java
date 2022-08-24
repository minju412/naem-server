package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
