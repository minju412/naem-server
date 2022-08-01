package naem.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberType;
import naem.server.service.AuthService;

@SpringBootTest
@Slf4j
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void signUp() {
        Member member = new Member();
        member.setMemberType(MemberType.IN_PERSON);
        member.setPhoneNumber("01012341234");
        member.setUsername("userId123");
        member.setPassword("a12345");
        member.setNickname("유저닉네임");
        authService.signUpMember(member);
    }

}
