package naem.server.service;

import org.springframework.http.ResponseEntity;

import naem.server.domain.member.dto.MemberConflictCheckDto;
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.TokenDto;

public interface AuthService {

    void signUp(SignUpReq signUpReq);

    void isConflict(MemberConflictCheckDto memberConflictCheckDto);

    ResponseEntity<TokenDto> signIn(SignInReq signInReq);

    ResponseEntity<TokenDto> regenerateToken(RegenerateTokenDto refreshTokenDto);

}
