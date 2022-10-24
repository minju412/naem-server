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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Like;
import naem.server.domain.comment.Comment;
import naem.server.domain.post.Post;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.ROLE_ADMIN;

    @Column(unique = true)
    private String username;

    private String password;

    @Size(min = 1, max = 10)
    @Column(unique = true)
    private String nickname;

    private String introduction;

    private String recommenderCode; // 추천인 코드

    private Boolean isAuthorized = false;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberTag> memberTags = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>(); // 사용자가 좋아요한 게시글

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;

    private Boolean isDeleted = Boolean.FALSE;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Builder
    public Member(MemberType memberType, String username, String password, String nickname /*MemberRole role*/) {
        this.memberType = memberType;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        //    this.role = role;
    }

    /**
     * 비밀번호를 암호화
     * @param passwordEncoder 암호화 할 인코더 클래스
     * @return 변경된 유저 Entity
     */
    public Member hashPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    /**
     * 패스워드 일치하는지 확인
     * @param passwordEncoder 패스워드 인코더
     * @param checkPassword 검사할 비밀번호
     * @return
     */
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return passwordEncoder.matches(checkPassword, getPassword());
    }

    //==연관관계 메서드==//
    public void addMemberTag(MemberTag memberTag) {
        memberTags.add(memberTag);
        memberTag.setMember(this);
    }

    //==수정 메서드==//
    public void updateAuthorization() {
        this.isAuthorized = true;
        this.role = MemberRole.ROLE_USER;
    }

    //==수정 메서드==//
    public void updateMemberInfo(String nickname, String introduction, List<MemberTag> memberTags) {

        if (StringUtils.isNotBlank(nickname)) {
            this.nickname = nickname;
        }
        if (StringUtils.isNotBlank(introduction)) {
            this.introduction = introduction;
        }
        if (memberTags != null) {
            for (MemberTag memberTag : memberTags) {
                this.addMemberTag(memberTag);
            }
        }
    }

    // 장애인 인증 완료
    public void grantDisabledAuthReq(String recommenderCode) {
        this.role = MemberRole.ROLE_USER;
        this.isAuthorized = true;
        this.recommenderCode = recommenderCode;
    }

}
