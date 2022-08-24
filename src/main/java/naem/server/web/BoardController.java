package naem.server.web;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.PostResDto;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.service.ImageService;
import naem.server.service.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/board")
@Slf4j
public class BoardController {

    private final PostService postService;
    private final ImageService imageService;

    // 게시글 등록
    @PostMapping("/save")
    public Response save(@RequestBody PostSaveReqDto requestDto) {
        postService.save(requestDto);
        return new Response("OK", "게시글 등록에 성공했습니다");
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public PostResDto detail(@PathVariable("id") long id) {
        return postService.getPost(id);
    }

    // 이미지 포함 게시글 등록
    @PostMapping(value = "/save/image", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response savePost(@RequestPart @Valid PostSaveReqDto requestDto, @RequestPart MultipartFile image) throws
        IOException {

        Post post = postService.save(requestDto);
        imageService.saveImage(image, "test", post);

        return new Response("OK", "게시글 등록에 성공했습니다");
    }
}
