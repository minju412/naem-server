package naem.server.service;

import static naem.server.exception.ErrorCode.*;

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
}
