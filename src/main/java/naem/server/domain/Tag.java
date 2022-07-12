package naem.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 태그가 하나의 게시글에 속한다.
    @JoinColumn(name = "post_id") // FK의 이름이 post_id 된다.
    private Post post;

    @Enumerated(EnumType.STRING) // 반드시 STRING으로 사용해야 한다.
    private TagType type;
}
