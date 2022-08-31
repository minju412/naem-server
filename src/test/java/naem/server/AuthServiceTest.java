package naem.server;

import static naem.server.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*; // assertThatThrownBy
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.MemberType;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.exception.CustomException;
import naem.server.exception.ErrorCode;
import naem.server.repository.MemberRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class AuthServiceTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private MemberRepository memberRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    private MemberType memberType = MemberType.IN_PERSON;
    private String phoneNumber = "010-0000-0002";
    private String username = "test2";
    private String password = "qwerQWER12!";
    private String nickName = "테스트2";

    private static String SIGN_UP_URL = "/auth/signUp";
    private static String SIGN_IN_URL = "/auth/signIn";

    @BeforeEach()
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
            .alwaysDo(print())
            .build();
    }

    // mock 회원가입
    private void signUp(String signUpData) throws Exception {
        mockMvc.perform(
                post(SIGN_UP_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(signUpData))
            .andExpect(status().isOk());
    }

    /**
     * 회원가입
     *    회원가입 시 회원 종류, 전화번호, 아이디, 비밀번호, 닉네임을 입력하지 않으면 오류
     *    이미 존재하는 아이디가 있으면 오류
     */
    @Test
    public void 회원가입_성공() throws Exception {
        // given
        String signUpData = objectMapper.writeValueAsString(
            new SignUpReq(memberType, phoneNumber, username, password, nickName));

        // when
        signUp(signUpData);

        // then
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member.getNickname()).isEqualTo(nickName);
    }
}
