package naem.server.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.comment.dto.CommentSaveDto;
import naem.server.service.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/comment")
@Slf4j
public class CommentController {

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
}
