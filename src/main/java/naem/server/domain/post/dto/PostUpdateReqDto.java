package naem.server.domain.post.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Board;
import naem.server.domain.Tag;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateReqDto {

    private Board board = null;
    private String title;
    private String content;
    private List<Tag> tag = new ArrayList<>();
}
