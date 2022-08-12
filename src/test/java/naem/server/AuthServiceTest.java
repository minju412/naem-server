package naem.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberType;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.service.AuthService;

@SpringBootTest
@Slf4j
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    SignUpReq member;

    @BeforeEach()
    public void initMember() {
        this.member = new SignUpReq();
        this.member.setMemberType(MemberType.IN_PERSON);
        this.member.setPhoneNumber("01012341234");
        this.member.setUsername("333");
        this.member.setPassword("q2w3");
        this.member.setNickname("333");
    }

    @Test
    public void signUp() {
        try {
            authService.signUp(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void login() {
    //     LoginMemberDto requestMemberDto = new LoginMemberDto(member.getUsername(), member.getPassword());
    //     try {
    //         authService.loginMember(requestMemberDto.getUsername(), requestMemberDto.getPassword());
    //         log.info("로그인 성공");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}
