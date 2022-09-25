package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Like;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.exception.CustomException;
import naem.server.repository.LikeRepository;
import naem.server.repository.MemberRepository;
import naem.server.repository.PostRepository;
import naem.server.service.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void postsLike(long postId) {

        String loginUsername = SecurityUtil.getLoginUsername();
        Member member = memberRepository.findByUsername(loginUsername)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if (post.getIsDeleted() == true) {
            throw new CustomException(POST_NOT_FOUND);
        }

        int flag = 0; // postId의 게시글이 한번이라도 좋아요 눌렸던 게시글(1)인지, 처음 누르는 게시글(0)인지
        for (Like like : member.getLikes()) {
            if (like.getPost().getId() == postId) {
                flag = 1;
                break;
            }
        }

        // 해당 게시글에 처음 좋아요 누름 -> like 로직
        if (flag == 0) {
            Like like = Like.createLike(member, post);
            likeRepository.save(like);
            likeLogic(member, post, like);
        } else { // 해당 게시글에 한번이라도 좋아요 누른 적 있음 -> like 혹은 dislike 로직
            Like like = likeRepository.findByPostId(postId)
                .orElseThrow(() -> new CustomException(LIKE_ERROR));

            if (like.getClickCnt() % 2 == 0) {
                likeLogic(member, post, like);
            } else {
                dislikeLogic(member, post, like);
            }
        }
    }

    private void likeLogic(Member member, Post post, Like like) {
        member.getLikes().add(like); // 해당 멤버가 좋아요 한 게시글에 추가
        like.setClickCnt(like.getClickCnt() + 1); // 좋아요를 클릭한 횟수 += 1
        post.setLikeCnt(post.getLikeCnt() + 1); // 해당 게시글의 좋아요 갯수 += 1
        like.setIsLike(true); // like
    }

    private void dislikeLogic(Member member, Post post, Like like) {
        member.getLikes().remove(like); // 해당 멤버가 좋아요 한 게시글에서 삭제
        like.setClickCnt(like.getClickCnt() + 1); // 좋아요를 클릭한 횟수 += 1
        post.setLikeCnt(post.getLikeCnt() - 1); // 해당 게시글의 좋아요 갯수 -= 1
        like.setIsLike(false); // dislike
    }
}
