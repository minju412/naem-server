package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.post.Post;
import naem.server.repository.custom.CustomPostRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

    // Page<Post> findAll(Pageable pageable);
}
