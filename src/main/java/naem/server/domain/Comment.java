package naem.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글이 하나의 게시글에 속한다.
    @JoinColumn(name = "post_id") // FK의 이름이 post_id 된다.
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글이 한 명의 회원에 속한다.
    @JoinColumn(name = "user_id") // FK의 이름이 user_id가 된다.
    private User user;

    private String content;
}
