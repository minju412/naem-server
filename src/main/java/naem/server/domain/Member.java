package naem.server.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String loginId;  // id

    @NotBlank
    private String name; // 사용자 이름

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @OneToMany(mappedBy = "member") // 한 명의 회원이 여러 게시글 작성 // Post 테이블에 있는 member가 연관관계 주인이고, 나는 거울이다. (여기에 값을 넣어도 FK가 변경되지 않는다.)
    private List<Post> posts = new ArrayList<>();

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.ROLE_NOT_PERMITTED;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salt_id")
    private Salt salt;

}
