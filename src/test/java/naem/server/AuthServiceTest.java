package naem.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.User;
import naem.server.domain.UserType;
import naem.server.service.AuthService;

@SpringBootTest
@Slf4j
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void signUp() {
        User user = new User();
        user.setUserType(UserType.IN_PERSON);
        user.setPhoneNumber("01012341234");
        user.setLoginId("user333");
        user.setPassword("a12345");
        user.setNickname("유저닉네임");
        authService.signUpUser(user);
    }

}
