package naem.server.web;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Member;
import naem.server.domain.Response;
import naem.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class MemberController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody Member member) {
        log.info("signUpUser Start");
        try {
            authService.signUpUser(member);
            return new Response("success", "회원가입을 성공적으로 완료했습니다.", null);
        } catch (Exception e) {
            return new Response("error", "회원가입을 하는 도중 오류가 발생했습니다.", null);
        }
    }

}
