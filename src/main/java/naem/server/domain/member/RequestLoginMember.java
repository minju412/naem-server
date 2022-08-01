package naem.server.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLoginMember {
    private String username;
    private String password;

    public RequestLoginMember() {
        super();
    }

    public RequestLoginMember(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
