package naem.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberType;
import naem.server.domain.member.dto.RequestMemberDto;
import naem.server.service.AuthService;

@SpringBootTest
@Slf4j
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    Member member;

    @BeforeEach()
    public void initMember() {
        this.member = new Member();
        this.member.setMemberType(MemberType.IN_PERSON);
        this.member.setPhoneNumber("01012341234");
        this.member.setUsername("userId22");
        this.member.setPassword("q2w3");
        this.member.setNickname("유저닉네임22");
    }

    @Test
    public void signUp() {
        authService.signUpMember(member);
    }

    @Test
    public void login() {
        RequestMemberDto requestMemberDto = new RequestMemberDto(member.getUsername(), member.getPassword());
        try {
            authService.loginMember(requestMemberDto.getUsername(), requestMemberDto.getPassword());
            log.info("로그인 성공");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
