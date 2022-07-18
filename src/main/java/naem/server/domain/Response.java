package naem.server.domain;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

    private HttpStatus status;
    private String response;
    private String message;
    private Object data;

    public Response(HttpStatus status, String response, String message, Object data) {
        this.status = status;
        this.response = response;
        this.message = message;
        this.data = data;
    }

}
