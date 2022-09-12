package naem.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Tag;
import naem.server.domain.comment.Comment;
import naem.server.domain.comment.dto.CommentSaveDto;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.MemberType;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.post.Post;
import naem.server.domain.post.dto.PostSaveReqDto;
import naem.server.repository.CommentRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class CommentServiceImplTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostService postService;

    @Autowired
    AuthService authService;

    @Autowired
    EntityManager em;

    private MemberType memberType = MemberType.IN_PERSON;
    private String username = "test2";
    private String password = "qwerQWER12!";
    private String nickName = "테스트2";

    private void clear() {
        em.flush();
        em.clear();
    }

    @BeforeEach()
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
            .alwaysDo(print())
            .build();
    }

    @BeforeEach()
    public void signUpAndSetAuthentication() throws Exception {
        authService.signUp(new SignUpReq(memberType, username, password, nickName));
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(
                User.builder()
                    .username(username)
                    .password(password)
                    .roles(MemberRole.USER.toString())
                    .build(),
                null)
        );
        SecurityContextHolder.setContext(emptyContext);
        clear();
    }

    private Long savePost() {
        String title = "제목";
        String content = "내용";
        List<Tag> tags = new ArrayList<>();
        PostSaveReqDto postSaveDto = new PostSaveReqDto(title, content, tags);
        Post save = postService.save(postSaveDto);
        clear();
        return save.getId();
    }

    @Test
    public void 댓글_저장_성공() {
        //given
        Long postId = savePost();
        CommentSaveDto commentSaveDto = new CommentSaveDto("댓글");

        //when
        commentService.save(postId, commentSaveDto);
        clear();

        //then
        List<Comment> resultList = em.createQuery("select c from Comment c order by c.createAt desc ", Comment.class)
            .getResultList();
        assertThat(resultList.size()).isEqualTo(3);
    }

}