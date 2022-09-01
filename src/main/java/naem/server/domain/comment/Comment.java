package naem.server.domain.comment;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import naem.server.domain.member.Member;
import naem.server.domain.post.Image;
import naem.server.domain.post.Post;
import naem.server.domain.post.PostTag;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글이 하나의 게시글에 속한다.
    @JoinColumn(name = "post_id") // FK의 이름이 post_id 된다.
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글이 한 명의 회원에 속한다.
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;

    private Boolean isDeleted;

    private LocalDateTime deleteAt;

    //==생성 메서드==//
    public static Comment createComment(Post post, Member member, String content) {

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setContent(content);
        comment.setPost(post);
        post.getComments().add(comment); // 해당 게시글에 댓글 추가
        return comment;
    }

    //==삭제 메서드==//
    public void deleteComment(Post post) {
        this.setIsDeleted(true);
        this.setDeleteAt(LocalDateTime.now());
        post.getComments().remove(this); // 해당 게시글의 댓글 리스트에서 댓글 제거
    }
}
