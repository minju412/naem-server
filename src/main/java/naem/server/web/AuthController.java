package naem.server.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.DisabledMemberInfo;
import naem.server.domain.member.dto.DisabledMemberAuthReq;
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.TokenDto;
import naem.server.service.AuthService;
import naem.server.service.S3Service;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final S3Service s3Service;

    @ApiOperation(value = "회원가입", notes = "회원가입")
    @PostMapping("/signUp")
    public Response signUp(@Valid @RequestBody SignUpReq signUpReq) {
        authService.signUp(signUpReq);
        return new Response("OK", "회원가입에 성공했습니다");
    }

    @ApiOperation(value = "아이디 중복 체크", notes = "아이디 중복 체크")
    @GetMapping("/checkid")
    public Response isConflict(@RequestParam(value = "username")String username) {
        authService.isConflict(username);
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

    @ApiOperation(value = "장애인 인증 요청", notes = "Amazon S3에 파일 업로드")
    @PostMapping(value = "/disabled", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response disabledMemberAuth(@RequestPart @Valid DisabledMemberAuthReq disabledAuthReq,
        @ApiParam("파일들 (여러 파일 업로드 가능)") @RequestPart(required = false) List<MultipartFile> multipartFile) {

        DisabledMemberInfo disabledMemberInfo = authService.disabledMemberAuth(disabledAuthReq);
        if (multipartFile != null) {
            s3Service.uploadDisabledAuthImage(multipartFile, "DisabledAuthImage", disabledMemberInfo);
        }
        return new Response("OK", "장애인 인증 요청에 성공했습니다");
    }
}
