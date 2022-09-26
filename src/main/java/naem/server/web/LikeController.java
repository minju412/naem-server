package naem.server.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.Member;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.service.LikeService;
import naem.server.service.MemberService;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LikeController {

    private final LikeService likeService;
    private final MemberService memberService;

    @ApiOperation(value = "게시글 좋아요", notes = "한번 더 누르면 좋아요가 취소됩니다")
    @PatchMapping("/post/{id}/like")
    public Response postLike(@PathVariable("id") long postId) {
        likeService.postsLike(postId);
        return new Response("OK", "게시글 좋아요에 성공했습니다");
    }


    @ApiOperation(value = "내가 좋아요한 게시글 조회", notes = "내가 좋아요한 게시글 조회")
    @GetMapping("/my/post/like")
    public Slice<BriefPostInfoDto> getMyLikedPostList(@AuthenticationPrincipal UserDetails userDetails, Long cursor,
        @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {

        Member member = memberService.getLoginMember(userDetails);
        return likeService.getMyLikedPostList(cursor, new PostReadCondition(member), pageRequest);
    }
}
