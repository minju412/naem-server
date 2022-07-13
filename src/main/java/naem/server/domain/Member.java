package naem.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String password;

    private String email;

    @OneToMany(mappedBy = "member") // 한 명의 회원이 여러 게시글 작성 // Post 테이블에 있는 member가 연관관계 주인이고, 나는 거울이다. (여기에 값을 넣어도 FK가 변경되지 않는다.)
    private List<Post> posts = new ArrayList<>();

}
