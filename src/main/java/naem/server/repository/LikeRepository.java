package naem.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPost(long postId);
}
