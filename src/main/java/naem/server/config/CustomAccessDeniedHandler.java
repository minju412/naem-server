package naem.server.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Response;
import naem.server.domain.member.MemberRole;
import naem.server.domain.member.SecurityMember;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        AccessDeniedException e) throws
        IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Response response = new Response(HttpStatus.FORBIDDEN, "error", "접근가능한 권한을 가지고 있지 않습니다.", null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember member = (SecurityMember)authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = member.getAuthorities();

        if (hasRole(authorities, MemberRole.ROLE_NOT_PERMITTED.name())) {
            response.setMessage("인증되지 않은 사용자입니다.");
            // response.setMessage("사용자 인증메일을 받지 않았습니다.");
        }

        PrintWriter out = httpServletResponse.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(response);
        out.print(jsonResponse);

    }

    private boolean hasRole(Collection<GrantedAuthority> authorites, String role) {
        return authorites.contains(new SimpleGrantedAuthority(role));
    }

}
