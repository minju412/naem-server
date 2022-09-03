package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.comment.Comment;
import naem.server.repository.custom.CustomCommentRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
}
