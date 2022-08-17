package naem.server.service.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    // 로그인한 유저가 있으면 반환, 없으면 예외 발생
    public static String getLoginUsername() {
        UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}