package naem.server.web;

import static naem.server.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.Member;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.exception.CustomException;
import naem.server.service.MemberService;
import naem.server.service.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/board")
@Slf4j
public class BoardController {

    private final MemberService memberService;
    private final PostService postService;

    // 게시글 등록
    @PostMapping("/save")
    public Response save(@RequestBody PostSaveReqDto requestDto) {
        postService.save(requestDto);
        return new Response("OK", "게시글 등록에 성공했습니다");
    }

    // 게시글 단건 조회
    @GetMapping("/detail/{id}")
    public PostResDto detail(@PathVariable("id") long id) {
        return postService.getPost(id);
    }

    // 게시글 수정
    @PatchMapping("/update/{id}")
    public Response update(@PathVariable("id") long id, @RequestBody PostSaveReqDto requestDto,
        @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Member> oUserDetail = memberService.findByUsername(userDetails.getUsername());
        if (oUserDetail.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member userDetail = oUserDetail.get();

        if (!userDetail.getId().equals(postService.getAuthorId(id))) {
            throw new CustomException(ACCESS_DENIED);
        }

        postService.update(id, requestDto);
        return new Response("OK", "게시글 수정에 성공했습니다");
    }

}
