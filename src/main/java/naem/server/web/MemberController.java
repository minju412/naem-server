package naem.server.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.RequestMemberDto;
import naem.server.service.AuthService;
import naem.server.service.util.CookieUtil;
import naem.server.service.util.JwtUtil;
import naem.server.service.util.RedisUtil;

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

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody Member member) {
        try {
            authService.signUpMember(member);
            return new Response(HttpStatus.OK, "success", "회원가입을 성공적으로 완료했습니다.", null);
        } catch (Exception e) {
            return new Response(HttpStatus.BAD_GATEWAY, "error", "회원가입을 하는 도중 오류가 발생했습니다.", e.getMessage());
        }
    }

    @PostMapping("/login")
    public Response login(@RequestBody RequestMemberDto requestMemberDto,
        HttpServletRequest req,
        HttpServletResponse res) {
        try {
            final Member member = authService.loginMember(requestMemberDto.getUsername(),
                requestMemberDto.getPassword());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);

            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

            redisUtil.setDataExpire(refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);

            return new Response(HttpStatus.OK, "success", "로그인에 성공했습니다.", token);

        } catch (Exception e) {
            return new Response(HttpStatus.BAD_GATEWAY, "error", "로그인에 실패했습니다.", e.getMessage());
        }
    }

    @GetMapping("/info")
    // @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Response memberInfo(HttpServletRequest request) {
        return new Response(HttpStatus.OK, "success", "정보 조회에 성공했습니다.", authService.getMyUserWithAuthorities());
    }

}
