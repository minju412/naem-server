package naem.server.domain.post.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Tag;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveReqDto {

    private String title;
    private String content;
    private List<Tag> tag;
}
