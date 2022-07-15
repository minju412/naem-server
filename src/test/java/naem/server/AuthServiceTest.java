package naem.server;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Member;
import naem.server.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void signUp(){
        Member member = new Member();
        member.setUsername("user333");
        member.setPassword("a12345");
        member.setName("유저이름");
        member.setEmail("kk@kakao.com");
        authService.signUpUser(member);
    }

}
