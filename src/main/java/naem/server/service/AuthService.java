package naem.server.service;

import org.springframework.http.ResponseEntity;

import naem.server.domain.member.DisabledMemberInfo;
import naem.server.domain.member.dto.DisabledMemberAuthReq;
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.TokenDto;

public interface AuthService {

    void signUp(SignUpReq signUpReq);

    void isConflict(String username);

    ResponseEntity<TokenDto> signIn(SignInReq signInReq);

    ResponseEntity<TokenDto> regenerateToken(RegenerateTokenDto refreshTokenDto);

    DisabledMemberInfo disabledMemberAuth(DisabledMemberAuthReq disabledAuthReq);
}
