package naem.server.domain.post.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Board;
import naem.server.domain.Tag;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSaveReqDto {

    @NotNull(message = "게시판 타입은 필수 선택 값입니다.")
    private Board board;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    private List<Tag> tag = new ArrayList<>();
}
