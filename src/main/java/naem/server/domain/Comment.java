package naem.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글이 하나의 게시글에 속한다.
    @JoinColumn(name = "post_id") // FK의 이름이 post_id 된다.
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글이 한 명의 회원에 속한다.
    @JoinColumn(name = "member_id") // FK의 이름이 member_id가 된다.
    private Member member;

    private String content;
}
