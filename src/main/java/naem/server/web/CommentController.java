package naem.server.web;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.BoardType;
import naem.server.domain.Response;
import naem.server.domain.comment.dto.CommentReadCondition;
import naem.server.domain.comment.dto.CommentResDto;
import naem.server.domain.comment.dto.CommentSaveDto;
import naem.server.domain.comment.dto.CommentUpdateDto;
import naem.server.domain.member.Member;
import naem.server.domain.post.dto.BriefPostInfoDto;
import naem.server.domain.post.dto.PostReadCondition;
import naem.server.service.CommentService;
import naem.server.service.MemberService;
import naem.server.service.util.SecurityUtil;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/comment")
@Slf4j
public class CommentController {

    private final MemberService memberService;
    private final CommentService commentService;

    @ApiOperation(value = "댓글 등록", notes = "댓글 등록")
    @PostMapping("/{id}")
    public Response commentSave(@PathVariable("id") Long postId, @Valid @RequestBody CommentSaveDto commentSaveDto) {
        commentService.save(postId, commentSaveDto);
        return new Response("OK", "댓글 등록에 성공했습니다");
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제")
    @DeleteMapping("/{id}")
    public Response commentDelete(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return new Response("OK", "댓글 삭제에 성공했습니다");
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글 수정")
    @PatchMapping("/{id}")
    public Response updateDelete(@PathVariable("id") Long commentId,
        @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        commentService.updateComment(commentId, commentUpdateDto);
        return new Response("OK", "댓글 수정에 성공했습니다");
    }

    @ApiOperation(value = "내가 작성한 댓글 조회", notes = "내가 작성한 댓글 조회")
    @GetMapping("/my")
    public Slice<CommentResDto> getMyCommentList(@AuthenticationPrincipal UserDetails userDetails, Long cursor,
        @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {

        Member commentAuthor = memberService.getLoginMember(userDetails);
        return commentService.getMyCommentList(cursor, new CommentReadCondition(commentAuthor), pageRequest);
    }
}
