package naem.server.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.LoginMemberDto;
import naem.server.domain.member.dto.PatchMemberDto;
import naem.server.service.AuthService;
import naem.server.service.MemberService;
import naem.server.service.util.CookieUtil;
import naem.server.service.util.JwtUtil;
import naem.server.service.util.RedisUtil;

@RequiredArgsConstructor
@RestController
@RequestMapping
    ("/member")
@Slf4j
public class MemberController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthService authService;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RedisUtil redisUtil;

    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public Response signUpUser(@RequestBody Member member) {
        authService.signUpMember(member);
        return new Response(HttpStatus.OK, "success", "로그인에 성공했습니다.", null);
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginMemberDto loginMemberDto,
        HttpServletRequest req,
        HttpServletResponse res) {

        final Member member = authService.loginMember(loginMemberDto.getUsername(),
            loginMemberDto.getPassword());
        final String token = jwtUtil.generateToken(member);
        final String refreshJwt = jwtUtil.generateRefreshToken(member);

        Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
        Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

        redisUtil.setDataExpire(refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        res.addCookie(accessToken);
        res.addCookie(refreshToken);

        return new Response(HttpStatus.OK, "success", "로그인에 성공했습니다.", token);
    }

    @GetMapping("/info")
    // @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response memberInfo() {
        return new Response(HttpStatus.OK, "success", "정보 조회에 성공했습니다.", authService.getMyUserWithAuthorities());
    }

    @PatchMapping("/{id}")
    public Response memberPatch(@PathVariable("id") long id, @RequestBody PatchMemberDto patchMemberDto) {

        if (memberService.patch(id, patchMemberDto) > 0) {
            return new Response(HttpStatus.OK, "success", "정보 수정에 성공했습니다.", null);
        } else {
            return new Response(HttpStatus.OK, "success", "일치하는 회원 정보가 없습니다. 사용자 id를 확인해주세요.", null);
        }
    }

}
