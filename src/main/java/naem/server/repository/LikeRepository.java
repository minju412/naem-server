package naem.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.Like;
import naem.server.repository.custom.CustomLikeRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, CustomLikeRepository {

    Optional<Like> findByMemberIdAndPostId(long memberId, long postId);
}
