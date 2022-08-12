package naem.server.web;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.SignUpRes;
import naem.server.domain.member.dto.TokenDto;
import naem.server.service.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public SignUpRes signUp(@Valid @RequestBody SignUpReq signUpReq) {
        return authService.signUp(signUpReq);
    }

    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInReq signInReq) {
        return authService.signIn(signInReq);
    }

    @PostMapping("/regenerateToken")
    public ResponseEntity<TokenDto> regenerateToken(@Valid @RequestBody RegenerateTokenDto refreshTokenDto) {
        return authService.regenerateToken(refreshTokenDto);
    }
}
