package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Board;
import naem.server.domain.BoardType;
import naem.server.domain.Tag;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.DetailedPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;
import naem.server.exception.CustomException;
import naem.server.repository.BoardRepository;
import naem.server.repository.MemberRepository;
import naem.server.repository.PostRepository;
import naem.server.repository.PostTagRepository;
import naem.server.service.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public void checkPrivileges(long postId, UserDetails userDetails) {

        Member member = memberRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (!member.getId().equals(getAuthorId(postId))) {
            throw new CustomException(ACCESS_DENIED);
        }
    }

    @Override
    @Transactional
    public Post checkPostExist(long postId) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if (post.getIsDeleted() == true) {
            throw new CustomException(POST_NOT_FOUND);
        }
        return post;
    }

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

        // 게시판 생성
        BoardType boardType = requestDto.getBoard().getBoardType();
        Optional<Board> oBoard = boardRepository.findByBoardType(boardType);
        if (oBoard.isPresent()) {
            Board board = oBoard.get();
            board.addPostToBoard(post); // 게시판에 게시글 추가
        } else {
            Board board = Board.createBoard(boardType, post);
            boardRepository.save(board);
        }

        postRepository.save(post);
        return post;
    }

    // 게시글 단건 조회
    @Override
    @Transactional
    public DetailedPostInfoDto getDetailedPostInfo(Long postId) {

        Post post = getPost(postId);
        post.setViewCnt(post.getViewCnt() + 1);

        return new DetailedPostInfoDto(post);
    }

    @Override
    @Transactional
    public Post getPost(Long id) {

        Post post = postRepository.findById(id)
            .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if (post.getIsDeleted()) {
            throw new CustomException(POST_NOT_FOUND);
        }

        return post;
    }

    // 게시글 수정
    @Override
    @Transactional
    public void update(Long postId, PostUpdateReqDto updateRequestDto) {

        // 존재하는 게시글인지 확인
        Post post = checkPostExist(postId);

        List<PostTag> newPostTags = null;
        List<PostTag> postTags = post.getPostTags();

        if (updateRequestDto.getTag() != null) {
            newPostTags = new ArrayList<>();
            // 포스트 태그 제거
            if (!postTags.isEmpty()) {
                PostTag.removePostTag(postTags); // 해당 게시글의 PostTag 목록에서 postTag 삭제
                postTagRepository.deleteAll(postTags); // 관계가 끊어졌고, 해당 게시글의 postTags를 삭제한다
            }

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

        // 게시판 생성
        if (updateRequestDto.getBoard() != null) {
            BoardType boardType = updateRequestDto.getBoard().getBoardType();
            Optional<Board> oBoard = boardRepository.findByBoardType(boardType);
            Board board;
            if (oBoard.isPresent()) {
                board = oBoard.get();
                board.deletePostFromBoard(post); // 게시판에 게시글 삭제
            } else {
                board = Board.createBoard(boardType, post);
                boardRepository.save(board);
            }
            post.updatePost(updateRequestDto.getTitle(), updateRequestDto.getContent(), newPostTags); // 게시글 수정
            board.addPostToBoard(post); // 게시판에 게시글 추가
        } else {
            post.updatePost(updateRequestDto.getTitle(), updateRequestDto.getContent(), newPostTags);
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
    public Post deletePost(Long postId) {

        // 존재하는 게시글인지 확인
        Post post = checkPostExist(postId);

        // 포스트 태그 제거
        List<PostTag> postTags = post.getPostTags();
        if (!postTags.isEmpty()) {
            PostTag.removePostTag(postTags); // 해당 게시글의 PostTag 목록에서 postTag 삭제
            postTagRepository.deleteAll(postTags); // 관계가 끊어졌고, 해당 게시글의 postTags를 삭제한다
        }

        // 게시판에서 삭제된 게시글 제거
        BoardType boardType = post.getBoard().getBoardType();
        Board board = boardRepository.findByBoardType(boardType)
            .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        board.deletePostFromBoard(post);

        return post;
    }

    @Override
    @Transactional
    public Slice<BriefPostInfoDto> getPostList(Long cursor, PostReadCondition condition, Pageable pageRequest) {
        return postRepository.getBriefPostInfoScroll(cursor, condition, pageRequest);
    }

    @Override
    @Transactional
    public Slice<BriefPostInfoDto> getMyPostList(Long cursor, PostReadCondition condition, Pageable pageRequest) {
        return postRepository.getMyPostScroll(cursor, condition, pageRequest);
    }
}
