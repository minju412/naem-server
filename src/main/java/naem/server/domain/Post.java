package naem.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import naem.server.domain.member.Member;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 게시글이 한 명의 회원에 속한다.
    @JoinColumn(name = "member_id")
    private Member member; // 외래키가 있는 곳을 연관관계의 주인으로 정한다.

    private String title;
    private String content;
    private Integer like_cnt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // 연관관계 거울
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // 연관관계 거울
    private List<Comment> comments = new ArrayList<>();

}
