package naem.server.config;

import static naem.server.exception.ErrorCode.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import naem.server.exception.ErrorCode;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        try {
            sendErrprResponse(response, UNAUTHORIZED_USER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 한글 출력을 위해 getWriter() 사용
    private void sendErrprResponse(HttpServletResponse response, ErrorCode errorCode) throws
        IOException,
        JSONException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("error", errorCode.getHttpStatus());
        responseJson.put("message", errorCode.getMessage());

        response.getWriter().print(responseJson);
    }
}
