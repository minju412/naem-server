package naem.server.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.service.LikeService;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @ApiOperation(value = "게시글 좋아요", notes = "한번 더 누르면 좋아요가 취소됩니다")
    @PostMapping("/post/{id}/like")
    public Response postLike(@PathVariable("id") long postId) {
        likeService.postLike(postId);
        return new Response("OK", "게시글 좋아요에 성공했습니다");
    }

}
