package naem.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.User;
import naem.server.service.AuthService;

@RestController
@RequestMapping
    ("/user")
@Slf4j
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody User user) {
        try {
            authService.signUpUser(user);
            return new Response(HttpStatus.OK, "success", "회원가입을 성공적으로 완료했습니다.", null);
        } catch (Exception e) {
	    log.info("error={}", e);
            return new Response(HttpStatus.BAD_GATEWAY, "error", "회원가입을 하는 도중 오류가 발생했습니다.", null);
        }
    }

}
