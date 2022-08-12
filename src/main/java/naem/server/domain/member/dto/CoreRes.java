package naem.server.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoreRes {

    private boolean ok;
    private String error;
}
