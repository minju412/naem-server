package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.comment.Comment;
import naem.server.domain.comment.dto.CommentSaveDto;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.exception.CustomException;
import naem.server.repository.CommentRepository;
import naem.server.repository.MemberRepository;
import naem.server.repository.PostRepository;
import naem.server.service.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void checkCommentPrivileges(long commentId) {

        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (!member.getId().equals(getCommentAuthorId(commentId))) {
            throw new CustomException(ACCESS_DENIED);
        }
    }

    @Override
    @Transactional
    public Comment checkCommentExist(long commentId) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        if (comment.getIsDeleted() == true) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }
        return comment;
    }

    // commentId 로 작성자의 memberId 반환
    @Override
    public Long getCommentAuthorId(Long commentId) {
        Optional<Comment> oComment = commentRepository.findById(commentId);
        if (oComment.isEmpty()) {
            return null;
        }
        Comment comment = oComment.get();
        return comment.getMember().getId();
    }

    // commentId 로 게시글 찾아서 반환
    @Override
    public Post getCommentPost(Long commentId) {
        Optional<Comment> oComment = commentRepository.findById(commentId);
        if (oComment.isEmpty()) {
            return null;
        }
        Comment comment = oComment.get();
        return comment.getPost();
    }

    @Override
    public void save(Long postId, CommentSaveDto commentSaveDto) {

        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if (post.getIsDeleted()) {
            throw new CustomException(POST_NOT_FOUND);
        }

        Comment comment = Comment.createComment(post, member, commentSaveDto.getContent());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {

        checkCommentPrivileges(commentId);
        Comment comment = checkCommentExist(commentId);
        Post post = getCommentPost(commentId);
        comment.deleteComment(post);
        commentRepository.save(comment);
    }
}
