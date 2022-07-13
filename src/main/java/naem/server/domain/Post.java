package naem.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 게시글이 한 명의 회원에 속한다.
    @JoinColumn(name = "member_id") // FK의 이름이 member_id가 된다.
    private Member member; // 외래키가 있는 곳을 연관관계의 주인으로 정한다.

    private String title;
    private String content;
    private String like;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // 연관관계 거울
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // 연관관계 거울
    private List<Comment> comments = new ArrayList<>();

}
