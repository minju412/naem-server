package naem.server.web;

import static naem.server.exception.ErrorCode.*;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.domain.post.dto.PostUpdateReqDto;
import naem.server.exception.CustomException;
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
            s3Service.uploadImage(multipartFile, "test3", post);
        }

        return new Response("OK", "게시글 등록에 성공했습니다");
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글 단건 조회")
    @GetMapping("/detail/{id}")
    public PostResDto getPost(@PathVariable("id") long id) {
        return postService.getPost(id);
    }
    
    // 게시글 수정
    @PatchMapping("/update/{id}")
    public Response update(@PathVariable("id") long id, @Valid @RequestBody PostUpdateReqDto updateRequestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Member> oUserDetail = memberService.findByUsername(userDetails.getUsername());
        if (oUserDetail.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member userDetail = oUserDetail.get();

        if (!userDetail.getId().equals(postService.getAuthorId(id))) {
            throw new CustomException(ACCESS_DENIED);
        }

        postService.update(id, updateRequestDto);
        return new Response("OK", "게시글 수정에 성공했습니다");
    }
    
    // 게시글 삭제
    @DeleteMapping("{id}")
    public Response delete(@PathVariable("id") long postId,
        @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Member> oUserDetail = memberService.findByUsername(userDetails.getUsername());
        if (oUserDetail.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member userDetail = oUserDetail.get();

        if (!userDetail.getId().equals(postService.getAuthorId(postId))) {
            throw new CustomException(ACCESS_DENIED);
        }

        postService.delete(postId);
        return new Response("OK", "게시글 삭제에 성공했습니다");
    }

    @ApiOperation(value = "게시글 리스트 조회 (무한 스크롤)", notes = "게시글 리스트 조회 (무한 스크롤)")
    @GetMapping("/list")
    public Slice<BriefPostInfoDto> getPostList(Long cursor, String keyword, @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {

        if (StringUtils.hasText(keyword)) {
            return postService.getPostList(cursor, new PostReadCondition(keyword), pageRequest);
        }
        return postService.getPostList(cursor, new PostReadCondition(), pageRequest);
    }

}
