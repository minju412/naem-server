package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
