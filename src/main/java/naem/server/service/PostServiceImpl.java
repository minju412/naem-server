package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Tag;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;
import naem.server.repository.PostRepository;
import naem.server.repository.PostTagRepository;
import naem.server.service.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
// @org.springframework.transaction.annotation.Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    @Override
    @Transactional
    public void save(PostSaveReqDto requestDto) {

        Optional<Member> oMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername());
        List<Tag> tags = new ArrayList<>(requestDto.getTag());
        PostTag postTag = null;
        List<PostTag> postTags = new ArrayList<>();
        int tagListSize = 3;

        if (oMember.isPresent()) {

            if (tags.size() > tagListSize) {
                throw new CustomException(TAG_LIST_SIZE_ERROR);
            }

            Member member = oMember.get();

            for (Tag tag : tags) {
                // 포스트태그 생성
                postTag = PostTag.createPostTag(tag);
                postTags.add(postTag);
            }
            // 게시글 생성
            Post post = Post.createPost(member, requestDto.getTitle(), requestDto.getContent(), postTags);

            postRepository.save(post);

        } else {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }

    // 게시글 단건 조회
    @Override
    @Transactional
    public PostResDto getPost(Long id) {

        Optional<Post> oPost = postRepository.findById(id);

        if (oPost.isPresent()) {

            Post post = oPost.get();

            if (post.getIsDeleted()) {
                throw new CustomException(POST_NOT_FOUND);
            }

            return new PostResDto(post);

        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }


    // 게시글 수정
    @Override
    @Transactional
    public void update(Long id, PostUpdateReqDto updateRequestDto) {

        Optional<Post> oPost = postRepository.findById(id);

        if (oPost.isPresent()) {

            Post post = oPost.get();

            if (!post.getPostTags().isEmpty()) {

                // 해당 게시글의 PostTag 목록에서 postTag 삭제
                for (PostTag postTag : post.getPostTags()) {
                    PostTag.removePostTag(postTag);
                }
                // 관계가 끊어졌고, 해당 게시글의 postTags를 삭제한다
                postTagRepository.deleteAll(post.getPostTags());
            }

            List<PostTag> postTags = new ArrayList<>();

            if (!updateRequestDto.getTag().isEmpty()) {

                List<Tag> tags = new ArrayList<>(updateRequestDto.getTag());
                PostTag postTag = null;
                int tagListSize = 3;

                if (tags.size() > tagListSize) {
                    throw new CustomException(TAG_LIST_SIZE_ERROR);
                }
                for (Tag tag : tags) {
                    // 포스트태그 생성
                    postTag = PostTag.createPostTag(tag);
                    postTags.add(postTag);
                }
            }

            // 게시글 수정
            post.updatePost(updateRequestDto.getTitle(), updateRequestDto.getContent(), postTags);

        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }
    
    /*
    post_id를 받아서 member_id를 반환한다
    */
    @Override
    public Long getAuthorId(Long id) {
        Optional<Post> oPost = postRepository.findById(id);
        if (oPost.isEmpty()) {
            return null;
        }
        Post post = oPost.get();
        return post.getMember().getId();
    }

    @Override
    public void delete(Long id) {
        Optional<Post> oPost = postRepository.findById(id);

        if (oPost.isPresent()) {

            Post post = oPost.get();

            if (post.getIsDeleted()) {
                throw new CustomException(POST_NOT_FOUND);
            }

            post.deletePost();
            postRepository.save(post);

        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }

}
