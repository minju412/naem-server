package naem.server.service;

import org.springframework.http.ResponseEntity;

import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.SignUpRes;
import naem.server.domain.member.dto.TokenDto;

public interface AuthService {

    /**
     * 유저의 정보로 회원가입
     * @param signUpReq 가입할 유저의 정보 Dto
     * @return 가입된 유저 정보
     */
    SignUpRes signUp(SignUpReq signUpReq);

    /**
     * 유저 정보로 로그인
     * @param signInReq 유저의 이메일과 비밀번호
     * @return json web token
     */
    ResponseEntity<TokenDto> signIn(SignInReq signInReq);

    ResponseEntity<TokenDto> regenerateToken(RegenerateTokenDto refreshTokenDto);

}
