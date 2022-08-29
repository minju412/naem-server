package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Tag;
import naem.server.domain.member.Member;
import naem.server.domain.post.Image;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
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
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    @Transactional
    public Post save(PostSaveReqDto requestDto) {

        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        List<Tag> tags = new ArrayList<>(requestDto.getTag());
        PostTag postTag = null;
        List<PostTag> postTags = new ArrayList<>();
        int tagListSize = 3;

        if (tags.size() > tagListSize) {
            throw new CustomException(TAG_LIST_SIZE_ERROR);
        }

        for (Tag tag : tags) {
            // 포스트태그 생성
            postTag = PostTag.createPostTag(tag);
            postTags.add(postTag);
        }
        // 게시글 생성
        Post post = Post.createPost(member, requestDto.getTitle(), requestDto.getContent(), postTags);

        postRepository.save(post);

        return post;

    }

    // 게시글 단건 조회
    @Override
    @Transactional
    public PostResDto getPost(Long id) {

        Post post = postRepository.findById(id)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if (post.getIsDeleted()) {
            throw new CustomException(POST_NOT_FOUND);
        }

        return new PostResDto(post);
    }

    // 게시글 수정
    @Override
    @Transactional
    public void update(Long postId, PostUpdateReqDto updateRequestDto, UserDetails userDetails) {

        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (!member.getId().equals(getAuthorId(postId))) {
            throw new CustomException(ACCESS_DENIED);
        }

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        // 포스트 태그 제거
        List<PostTag> postTags = post.getPostTags();
        if (!postTags.isEmpty()) {
            // 해당 게시글의 PostTag 목록에서 postTag 삭제
            for (PostTag postTag : postTags) {
                PostTag.removePostTag(postTag);
            }
            // 관계가 끊어졌고, 해당 게시글의 postTags를 삭제한다
            postTagRepository.deleteAll(post.getPostTags());
        }

        List<PostTag> newPostTags = new ArrayList<>();

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
                newPostTags.add(postTag);
            }
        }

        // 게시글 수정
        post.updatePost(updateRequestDto.getTitle(), updateRequestDto.getContent(), newPostTags);

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
    public Post delete(Long postId, UserDetails userDetails) {

        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (!member.getId().equals(getAuthorId(postId))) {
            throw new CustomException(ACCESS_DENIED);
        }

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if (post.getIsDeleted()) {
            throw new CustomException(POST_NOT_FOUND);
        }

        // 포스트 태그 제거
        List<PostTag> postTags = post.getPostTags();
        if (!postTags.isEmpty()) {
            // 해당 게시글의 PostTag 목록에서 postTag 삭제
            for (PostTag postTag : postTags) {
                PostTag.removePostTag(postTag);
            }
            // 관계가 끊어졌고, 해당 게시글의 postTags를 삭제한다
            postTagRepository.deleteAll(post.getPostTags());
        }

        return post;
    }

    @Override
    @Transactional
    public Slice<BriefPostInfoDto> getPostList(Long cursor, PostReadCondition condition, Pageable pageRequest) {
        return postRepository.getBriefPostInfoScroll(cursor, condition, pageRequest);
    }

}
