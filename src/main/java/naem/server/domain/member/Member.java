package naem.server.domain.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import naem.server.domain.Comment;
import naem.server.domain.Post;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.ROLE_NOT_PERMITTED;

    @NotBlank
    private String phoneNumber;

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

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
