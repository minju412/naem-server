package naem.server.web;

import static naem.server.exception.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Response uploadFile(@RequestPart @Valid PostSaveReqDto requestDto,
        @ApiParam("파일들 (여러 파일 업로드 가능)") @RequestPart(required = false) List<MultipartFile> multipartFile) {

        Post post = postService.save(requestDto);
        if (multipartFile != null) {
            s3Service.uploadImage(multipartFile, "test2", post);
        }

        return new Response("OK", "게시글 등록에 성공했습니다");
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글 단건 조회")
    @GetMapping("/detail/{id}")
    public PostResDto detail(@PathVariable("id") long id) {
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
    public Response delete(@PathVariable("id") long id,
        @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Member> oUserDetail = memberService.findByUsername(userDetails.getUsername());
        if (oUserDetail.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member userDetail = oUserDetail.get();

        if (!userDetail.getId().equals(postService.getAuthorId(id))) {
            throw new CustomException(ACCESS_DENIED);
        }

        postService.delete(id);
        return new Response("OK", "게시글 삭제에 성공했습니다");
    }

}
