package naem.server.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.BoardType;
import naem.server.domain.Response;
import naem.server.domain.member.Member;
import naem.server.domain.post.Image;
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.DetailedPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;
import naem.server.service.MemberService;
import naem.server.service.PostService;
import naem.server.service.S3Service;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/board")
@Slf4j
public class BoardController {

    private final MemberService memberService;
    private final PostService postService;
    private final S3Service s3Service;

    @ApiOperation(value = "게시글 등록", notes = "Amazon S3에 파일 업로드")
    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response save(@RequestPart @Valid PostSaveReqDto requestDto,
        @ApiParam("파일들 (여러 파일 업로드 가능)") @RequestPart(required = false) List<MultipartFile> multipartFile) {

        Post post = postService.save(requestDto);
        if (multipartFile != null) {
            s3Service.uploadImage(multipartFile, "Post", post);
        }

        return new Response("OK", "게시글 등록에 성공했습니다");
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글 단건 조회")
    @GetMapping("/detail/{id}")
    public DetailedPostInfoDto getPost(@PathVariable("id") long postId) {
        return postService.getDetailedPostInfo(postId);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 수정")
    @PatchMapping("/{id}")
    public Response update(@PathVariable("id") long postId, @RequestPart @Valid PostUpdateReqDto updateRequestDto,
        @ApiParam("파일들 (여러 파일 업로드 가능)") @RequestPart(required = false) List<MultipartFile> multipartFile,
        @AuthenticationPrincipal UserDetails userDetails) {

        postService.checkPrivileges(postId, userDetails); // 접근 권한 확인

        // 이미지 제거
        Post post = postService.getPost(postId);
        List<Image> images = post.getImg();
        if (!images.isEmpty()) {
            s3Service.deleteImageList(images);
        }
        Image.deleteImages(images);

        // 이미지 저장
        if (multipartFile != null) {
            s3Service.uploadImage(multipartFile, "Post", post);
        }

        postService.update(postId, updateRequestDto);
        return new Response("OK", "게시글 수정에 성공했습니다");
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제")
    @DeleteMapping("{id}")
    public Response delete(@PathVariable("id") long postId,
        @AuthenticationPrincipal UserDetails userDetails) {

        postService.checkPrivileges(postId, userDetails); // 접근 권한 확인

        Post deletedPost = postService.deletePost(postId);
        List<Image> images = deletedPost.getImg();
        if (!images.isEmpty()) {
            s3Service.deleteImageList(images);
        }
        deletedPost.deletePost();
        postService.save(deletedPost);

        return new Response("OK", "게시글 삭제에 성공했습니다");
    }

    @ApiOperation(value = "게시글 조회 및 검색 (무한 스크롤)", notes = "게시글 리스트 조회 및 검색 (무한 스크롤)")
    @GetMapping("/list")
    public Slice<BriefPostInfoDto> getPostList(Long cursor, String keyword, BoardType boardType,
        @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {

        if (StringUtils.hasText(keyword)) { // 검색
            if (boardType == null) {
                return postService.getPostList(cursor, new PostReadCondition(keyword), pageRequest); // 전체 게시글 검색
            } else {
                return postService.getPostList(cursor, new PostReadCondition(boardType, keyword), pageRequest); // 게시판 타입별 검색
            }
        } else { // 조회
            if (boardType == null) {
                return postService.getPostList(cursor, new PostReadCondition(), pageRequest); // 전체 게시글 조회
            } else {
                return postService.getPostList(cursor, new PostReadCondition(boardType), pageRequest); // 게시판 타입별 조회
            }
        }
    }

    @ApiOperation(value = "내가 작성한 게시글 조회", notes = "내가 작성한 게시글 조회")
    @GetMapping("/my")
    public Slice<BriefPostInfoDto> getMyPostList(@AuthenticationPrincipal UserDetails userDetails, Long cursor,
        @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {

        Member commentAuthor = memberService.getLoginMember(userDetails);
        return postService.getMyPostList(cursor, new PostReadCondition(commentAuthor), pageRequest);
    }

}
