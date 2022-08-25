package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Tag;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;
import naem.server.repository.PostRepository;
import naem.server.service.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Post save(PostSaveReqDto requestDto) {

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

            return post;

        } else {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public PostResDto getPost(Long id) {

        Optional<Post> oPost = postRepository.findById(id);

        if (oPost.isPresent()) {

            Post post = oPost.get();

            return new PostResDto(post);

        } else {
            throw new CustomException(POST_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public List<PostResDto> getPostList(@PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {

        List<PostResDto> postResDtos = new ArrayList<>();
        Page<Post> all = postRepository.findAll(pageRequest);

        for (Post post : all) {
            postResDtos.add(new PostResDto(post));
        }

        return postResDtos;
    }
}
