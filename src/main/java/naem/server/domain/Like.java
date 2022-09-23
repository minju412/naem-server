package naem.server.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.member.Member;
import naem.server.domain.post.Post;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;

    private int clickCnt = 0; // 좋아요 클릭 횟수 (홀수 -> like / 짝수 -> dislike)
    private Boolean isLike; // like(1) 인지 dislike(0) 인지

    //==생성 메서드==//
    public static Like createLike(Member member, Post post) {

        Like like = new Like();
        like.setMember(member);
        like.setPost(post);
        return like;
    }
}
