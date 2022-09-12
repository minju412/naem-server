package naem.server.web;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.dto.MemberConflictCheckDto;
import naem.server.domain.member.dto.ProtectorAuthDto;
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.TokenDto;
import naem.server.service.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping("/signUp")
    public Response signUp(@Valid @RequestBody SignUpReq signUpReq) {
        authService.signUp(signUpReq);
        return new Response("OK", "회원가입에 성공했습니다");
    }

    @ApiOperation(value = "아이디 중복 체크", notes = "아이디 중복 체크")
    @PostMapping("/check")
    public Response isConflict(@Valid @RequestBody MemberConflictCheckDto memberConflictCheckDto) {
        authService.isConflict(memberConflictCheckDto);
        return new Response("OK", "사용할 수 있는 아이디입니다");
    }

    @ApiOperation(value = "로그인", notes = "로그인")
    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> signIn(@Valid @RequestBody SignInReq signInReq) {
        return authService.signIn(signInReq);
    }

    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급")
    @PostMapping("/regenerateToken")
    public ResponseEntity<TokenDto> regenerateToken(@Valid @RequestBody RegenerateTokenDto refreshTokenDto) {
        return authService.regenerateToken(refreshTokenDto);
    }

    @ApiOperation(value = "보호자 인증 요청", notes = "보호자 인증 요청")
    @PatchMapping("/protector")
    public Response protectorAuthorization(@Valid @RequestBody ProtectorAuthDto protectorAuthDto) {
        authService.protectorAuthorization(protectorAuthDto);
        return new Response("OK", "보호자 인증에 성공했습니다");
    }
}
