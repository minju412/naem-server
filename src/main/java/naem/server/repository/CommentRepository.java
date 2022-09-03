package naem.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naem.server.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
}
