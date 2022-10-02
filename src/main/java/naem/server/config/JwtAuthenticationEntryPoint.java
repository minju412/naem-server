package naem.server.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException ex
    ) throws IOException {
        log.error("UnAuthorized -- message : " + ex.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
        // response.sendRedirect("/auth/signIn"); // 로그인 페이지로 리다이렉트
    }

}
